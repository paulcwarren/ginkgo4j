package com.github.paulcwarren.ginkgo4j;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import impl.com.github.paulcwarren.ginkgo4j.Spec;
import impl.com.github.paulcwarren.ginkgo4j.builder.TestWalker;
import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;
import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChainBuilder;
import impl.com.github.paulcwarren.ginkgo4j.chains.SpecsCollector;
import impl.com.github.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollector;
import impl.com.github.paulcwarren.ginkgo4j.junit.JunitRunnerListener;
import impl.com.github.paulcwarren.ginkgo4j.runner.RunnerListener;
import impl.com.github.paulcwarren.ginkgo4j.runner.SpecRunner;
import impl.com.github.paulcwarren.ginkgo4j.runner.SpecSkipper;

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
			description = Description.createSuiteDescription(testClass.getName(), new Annotation[]{});

			JunitDescriptionsCollector descCollector = new JunitDescriptionsCollector(description);
			// collect as many descriptions as we can
			try {
				new TestWalker(testClass).walk(descCollector);
			} finally {
				descriptions = descCollector.getDescriptions();
			}
		}
	
        return description;
	}

	@Override
	public void run(RunNotifier notifier) {
		// maven's sure-fire plug-in doesn't call getDescription so call it here
		// to ensure setup happens
		this.getDescription();
		
		try {
			notifier.fireTestStarted(description);
	
			List<ExecutableChain> chains = calculateExecutionChains(testClass);
	
			RunnerListener listener = new JunitRunnerListener(notifier, descriptions);
			List<Runnable> runners = calculateWorkerThreads(chains, listener);
	
			threadExecute(runners, getThreads(testClass));
		} catch (Exception e) {
			notifier.fireTestFailure(new Failure(description, e));
		} finally {
			notifier.fireTestFinished(description);
		}
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

	static List<impl.com.github.paulcwarren.ginkgo4j.runner.Runner> calculateWorkerThreads(List<ExecutableChain> chains) {
		
		List<impl.com.github.paulcwarren.ginkgo4j.runner.Runner> skippedWorkers = new ArrayList<>();
		List<impl.com.github.paulcwarren.ginkgo4j.runner.Runner> focusedWorkers = new ArrayList<>();
		List<impl.com.github.paulcwarren.ginkgo4j.runner.Runner> workers = new ArrayList<>();

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
	
	static List<Runnable> calculateWorkerThreads(List<ExecutableChain> chains, RunnerListener listener) {
		
		List<Runnable> skippedWorkers = new ArrayList<>();
		List<Runnable> focusedWorkers = new ArrayList<>();
		List<Runnable> workers = new ArrayList<>();

		for(ExecutableChain chain : chains) {
			if (chain.isFocused()) {
				focusedWorkers.add(new SpecRunner(chain, listener));
			} else {
				workers.add(new SpecRunner(chain, listener));
				skippedWorkers.add(new SpecSkipper(chain, listener));
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
	
	static void threadExecute(List<Runnable> workers, int threads) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(threads);
            for (Runnable runner : workers) {
                executor.execute(runner);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            	Thread.sleep(100);
            }
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
        }
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
