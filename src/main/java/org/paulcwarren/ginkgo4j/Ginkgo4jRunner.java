package org.paulcwarren.ginkgo4j;

import java.io.Serializable;
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

import impl.org.paulcwarren.ginkgo4j.Context;
import impl.org.paulcwarren.ginkgo4j.Spec;
import impl.org.paulcwarren.ginkgo4j.builder.ExecutableChainBuilder;
import impl.org.paulcwarren.ginkgo4j.builder.SpecBuilder;
import impl.org.paulcwarren.ginkgo4j.runner.RunnerThread;

public class Ginkgo4jRunner extends Runner {

	private Class<?> testClass;
	private Map<String, Description> descriptions = new HashMap<>();
	private Description description;
	private List<ExecutableChain> chains;
	
	public Ginkgo4jRunner(Class<?> testClass) throws InitializationError {
		this.testClass = testClass;
	}

	@Override
	public Description getDescription() {
		if (description == null) {
			SpecBuilder specCollector = new SpecBuilder();
			collectSpecs(specCollector);
			
			description = Description.createSuiteDescription(getName(), (Annotation[])null);
			List<Context> contexts = specCollector.getContexts();
			for (Context context : contexts) {
				Serializable id = context.getId();
				Description desc = Description.createSuiteDescription(context.getDescription(), id, (Annotation[])null);
				descriptions.put(context.getId(), desc);
				description.addChild(desc);
				describeContext(context, desc);
			}
			
			chains = calculateExecutionChains(specCollector.getSpecs());
		}
	
        return description;
	}

	private void collectSpecs(SpecBuilder specCollector) {
		try {

			Ginkgo4jDSL.setVisitor(specCollector);
			try {
				testClass.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			Ginkgo4jDSL.unsetVisitor(specCollector);
		}
	}

	private List<ExecutableChain> calculateExecutionChains(List<Spec> specs) {
		List<ExecutableChain> chains = new ArrayList<>();
    	for (Spec spec : specs) {

    		ExecutableChainBuilder bldr = new ExecutableChainBuilder(spec.getId());
    		Ginkgo4jDSL.setVisitor(bldr);
    		try {
    			testClass.newInstance();
    		} catch (Throwable t) {}
    		finally {
    			Ginkgo4jDSL.setVisitor(null);
    		}
    		
    		chains.add(bldr.getExecutableChain());
    	}
    	return chains;
	}

	@Override
	public void run(RunNotifier notifier) {
        notifier.fireTestStarted(description);
        runDescribes(notifier);
        notifier.fireTestFinished(description);
   	}
	
    private void runDescribes(RunNotifier notifier) {
    		
		List<RunnerThread> workers = new ArrayList<>();
		for(ExecutableChain chain : chains) {
			workers.add(new RunnerThread(chain, notifier, Collections.unmodifiableMap(descriptions)));
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
        	notifier.fireTestAssumptionFailed(new Failure(description, e));;
        } catch (Throwable e) {
        	notifier.fireTestFailure(new Failure(description, e));
        } finally {
        	notifier.fireTestFinished(description);
        }
    }

	private int getThreads() {
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

	protected Description describeContext(Context context, Description desc) {

		for (Context childCtxt : context.getChildren()) {
			Description childDesc = Description.createSuiteDescription(childCtxt.getDescription(), childCtxt.getId(), (Annotation[])null);
			descriptions.put(childCtxt.getId(), childDesc);
			desc.addChild(childDesc);
			describeContext(childCtxt, childDesc);
		}
		
		for (Spec spec : context.getSpecs()) {
			Description childDesc = Description.createTestDescription((String)null, spec.getDescription(), spec.getId());
			descriptions.put(spec.getId(), childDesc);
			desc.addChild(childDesc);
		}
		
		return desc;
	}
}
