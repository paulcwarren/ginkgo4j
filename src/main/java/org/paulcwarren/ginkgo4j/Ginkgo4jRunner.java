package org.paulcwarren.ginkgo4j;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import impl.org.paulcwarren.ginkgo4j.Spec;
import impl.org.paulcwarren.ginkgo4j.builder.TestWalker;
import impl.org.paulcwarren.ginkgo4j.chains.ExecutableChain;
import impl.org.paulcwarren.ginkgo4j.chains.ExecutableChainBuilder;
import impl.org.paulcwarren.ginkgo4j.chains.SpecsCollector;
import impl.org.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollector;
import impl.org.paulcwarren.ginkgo4j.junit.JunitSpecRunnerThread;
import impl.org.paulcwarren.ginkgo4j.junit.JunitSpecSkipperThread;
import impl.org.paulcwarren.ginkgo4j.runner.SpecRunner;
import impl.org.paulcwarren.ginkgo4j.runner.SpecSkipper;

public class Ginkgo4jRunner extends Runner {

	private Class<?> testClass;
	private Map<String, Description> descriptions = new HashMap<>();
	private Description description;
	
	public Ginkgo4jRunner(Class<?> testClass) throws InitializationError {
		this.testClass = testClass;
	}

	@Override
	public Description getDescription() {
		if (description == null) {
			description = Description.createSuiteDescription(testClass.getName(), (Annotation[])null);

			JunitDescriptionsCollector descCollector = new JunitDescriptionsCollector(description);
			new TestWalker(testClass).walk(descCollector);
			descriptions = descCollector.getDescriptions();
		}
	
        return description;
	}

	@Override
	public void run(RunNotifier notifier) {
		// maven's sure-fire plug-in doesn't call getDescription so call it here
		// to ensure setup happens
		this.getDescription();
		
		List<ExecutableChain> chains = calculateExecutionChains(testClass);

		List<impl.org.paulcwarren.ginkgo4j.runner.Runner> runners = calculateWorkerThreads(notifier, chains);

		List<Thread> workers = threadWrap(runners, notifier, descriptions);
		
		threadExecute(workers, getThreads(testClass), notifier, description);
   	}

	static List<ExecutableChain> calculateExecutionChains(Class<?> testClass) {
		SpecsCollector specCollector = new SpecsCollector();
		new TestWalker(testClass).walk(specCollector);

		List<ExecutableChain> chains = new ArrayList<>();
    	for (Spec spec : specCollector.getSpecs()) {
    		ExecutableChainBuilder bldr = new ExecutableChainBuilder(spec.getId());
    		new TestWalker(testClass).walk(bldr);
    		chains.add(bldr.getExecutableChain());
    	}
    	return chains;
	}

	static List<impl.org.paulcwarren.ginkgo4j.runner.Runner> calculateWorkerThreads(RunNotifier notifier, List<ExecutableChain> chains) {
		
		List<impl.org.paulcwarren.ginkgo4j.runner.Runner> skippedWorkers = new ArrayList<>();
		List<impl.org.paulcwarren.ginkgo4j.runner.Runner> focusedWorkers = new ArrayList<>();
		List<impl.org.paulcwarren.ginkgo4j.runner.Runner> workers = new ArrayList<>();

		for(ExecutableChain chain : chains) {
			if (chain.isFocused()) {
				focusedWorkers.add(new SpecRunner(chain));
			} else {
				workers.add(new SpecRunner(chain));
				skippedWorkers.add(new SpecSkipper(chain));
			}
		}
		
		if (focusedWorkers.size() > 0) {
			workers = new ArrayList<>();
			workers.addAll(focusedWorkers);
			workers.addAll(skippedWorkers);
			return workers;
		} else {
			return workers;
		}
	}
	
	static void threadExecute(List<Thread> workers, int threads, RunNotifier notifier, Description description) {
		notifier.fireTestStarted(description);
        try {
            ExecutorService executor = Executors.newFixedThreadPool(threads);
            for (Thread runner : workers) {
                executor.execute(runner);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            	Thread.sleep(100);
            }

        } catch (AssumptionViolatedException e) {
        	notifier.fireTestAssumptionFailed(new Failure(description, e));
        } catch (Throwable e) {
        	notifier.fireTestFailure(new Failure(description, e));
        } finally {
        	notifier.fireTestFinished(description);
        }
	}

	static List<Thread> threadWrap(List<impl.org.paulcwarren.ginkgo4j.runner.Runner> runners, RunNotifier notifier, Map<String,Description> descriptions) {
		List<Thread> workers = new ArrayList<>();
		for (impl.org.paulcwarren.ginkgo4j.runner.Runner runner : runners) {
			if (runner instanceof SpecRunner) {
				Description desc = descriptions.get(runner.getChain().getId());
				if (desc == null) throw new IllegalStateException(String.format("Unable to find description for '%s'", runner.getChain().getId()));
				
				Thread t = new JunitSpecRunnerThread(runner, notifier, desc);
				workers.add(t);
			} else if (runner instanceof SpecSkipper) {
				Description desc = descriptions.get(runner.getChain().getId());
				if (desc == null) throw new IllegalStateException(String.format("Unable to find description for '%s'", runner.getChain().getId()));
				
				Thread t = new JunitSpecSkipperThread(notifier, desc);
				workers.add(t);
			}
		}
		return workers;
	}

	static int getThreads(Class<?> testClass) {
		Ginkgo4jConfiguration config = testClass.getAnnotation(Ginkgo4jConfiguration.class);
		if (config != null) {
			return config.threads();
		} else {
			return Ginkgo4jConfiguration.DEFAULT_THREADS;
		}
	}
}
