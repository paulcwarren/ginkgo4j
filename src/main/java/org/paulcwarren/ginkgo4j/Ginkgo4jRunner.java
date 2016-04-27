package org.paulcwarren.ginkgo4j;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
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
import impl.org.paulcwarren.ginkgo4j.builder.DescriptionsCollector;
import impl.org.paulcwarren.ginkgo4j.builder.ExecutableChainBuilder;
import impl.org.paulcwarren.ginkgo4j.builder.SpecsCollector;
import impl.org.paulcwarren.ginkgo4j.builder.TestWalker;
import impl.org.paulcwarren.ginkgo4j.runner.RunnerThread;

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
			description = Description.createSuiteDescription(getName(), (Annotation[])null);

			DescriptionsCollector descCollector = new DescriptionsCollector(description);
			new TestWalker(testClass).walk(descCollector);
			descriptions = descCollector.getDescriptions();
		}
	
        return description;
	}

	@Override
	public void run(RunNotifier notifier) {
		// maven's surefire plugin doesn't call getDescription so call it here
		// to ensure setup happens
		this.getDescription();
		
		SpecsCollector specCollector = new SpecsCollector();
		new TestWalker(testClass).walk(specCollector);
		
		List<ExecutableChain> chains = calculateExecutionChains(specCollector.getSpecs());

		List<RunnerThread> workers = new ArrayList<>();
		for(ExecutableChain chain : chains) {
			Description desc = descriptions.get(chain.getId());
			if (desc == null) throw new IllegalStateException(String.format("Unable to find description for '%s'", chain.getId()));
			workers.add(new RunnerThread(chain, notifier, desc));
		}

		notifier.fireTestStarted(description);
        try {
            ExecutorService executor = Executors.newFixedThreadPool(getThreads());
            for (RunnerThread runner : workers) {
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
	
	protected List<ExecutableChain> calculateExecutionChains(List<Spec> specs) {
		List<ExecutableChain> chains = new ArrayList<>();
    	for (Spec spec : specs) {
    		ExecutableChainBuilder bldr = new ExecutableChainBuilder(spec.getId());
    		new TestWalker(testClass).walk(bldr);
    		chains.add(bldr.getExecutableChain());
    	}
    	return chains;
	}

	protected int getThreads() {
		Ginkgo4jConfiguration config = testClass.getAnnotation(Ginkgo4jConfiguration.class);
		if (config != null) {
			return config.threads();
		} else {
			return Ginkgo4jConfiguration.DEFAULT_THREADS;
		}
	}

	/**
     * Returns a name used to describe this Runner
     */
    protected String getName() {
        return testClass.getName();
    }

    /**
     * @return the annotations that should be attached to this runner's
     *         description.
     */
    protected Annotation[] getRunnerAnnotations() {
        return testClass.getAnnotations();
    }
}
