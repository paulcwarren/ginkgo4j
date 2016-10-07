package com.github.paulcwarren.ginkgo4j;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import impl.com.github.paulcwarren.ginkgo4j.builder.TestWalker;
import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;
import impl.com.github.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollector;
import impl.com.github.paulcwarren.ginkgo4j.junit.JunitRunnerListener;
import impl.com.github.paulcwarren.ginkgo4j.runner.RunnerListener;

public class Ginkgo4jSpringRunner extends SpringJUnit4ClassRunner {

	Class<?> testClass;
	private Map<String, Description> descriptions = new HashMap<>();
	private Description description;
	
	public Ginkgo4jSpringRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		testClass = clazz;
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

	/**
     * Returns a {@link Statement}: Call {@link #runChild(Object, RunNotifier)}
     * on each object returned by {@link #getChildren()} (subject to any imposed
     * filter and sort)
     */
    protected Statement childrenInvoker(final RunNotifier notifier) {
		final TestContextManager mgr = this.getTestContextManager();
        
		return new Statement() {
            @Override
            public void evaluate() {
        		// maven's sure-fire plug-in doesn't call getDescription so call it here
        		// to ensure setup happens
        		getDescription();
        		
        		List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(testClass);
        		
        		for (ExecutableChain chain : chains) {
        			try {
						mgr.prepareTestInstance(chain.getTestObject());
					} catch (Exception e) {
						e.printStackTrace();
					}
        		}
        		
        		RunnerListener listener = new JunitRunnerListener(notifier, descriptions);
        		List<Runnable> runners = Ginkgo4jRunner.calculateWorkerThreads(chains, listener);

        		Ginkgo4jRunner.threadExecute(runners, Ginkgo4jRunner.getThreads(testClass));
            }
        };
    }
}
