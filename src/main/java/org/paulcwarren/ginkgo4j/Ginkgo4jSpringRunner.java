package org.paulcwarren.ginkgo4j;

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

import impl.org.paulcwarren.ginkgo4j.builder.DescriptionsCollector;
import impl.org.paulcwarren.ginkgo4j.builder.TestWalker;

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

			DescriptionsCollector descCollector = new DescriptionsCollector(description);
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
        		
        		List<impl.org.paulcwarren.ginkgo4j.runner.Runner> runners = Ginkgo4jRunner.calculateWorkerThreads(notifier, chains);

        		List<Thread> workers = Ginkgo4jRunner.threadWrap(runners, notifier, descriptions);
        		
        		Ginkgo4jRunner.threadExecute(workers, Ginkgo4jRunner.getThreads(testClass), notifier, description);
            }
        };
    }
}
