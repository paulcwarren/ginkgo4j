package impl.org.paulcwarren.ginkgo4j.runner;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.paulcwarren.ginkgo4j.ExecutableBlock;
import org.paulcwarren.ginkgo4j.ExecutableChain;

public class SpecRunnerThread extends Thread {

	private ExecutableChain chain;
	private RunNotifier notifier;
	private Description desc;
	
	public SpecRunnerThread(ExecutableChain chain, RunNotifier notifier, Description desc) {
		this.chain = chain;
		this.notifier = notifier;
		this.desc = desc;
	}
	
	@Override
	public void run() {

		notifier.fireTestStarted(desc);

		try {
			for (ExecutableBlock block : chain.getBeforeEachs()) {
    			block.invoke();
			}
		
			for (ExecutableBlock block : chain.getJustBeforeEachs()) {
    			block.invoke();
			}
		
			chain.getSpec().invoke();

			for (ExecutableBlock block : chain.getAfterEachs()) {
    			block.invoke();
	    	}
        } catch (AssumptionViolatedException e) {
        	notifier.fireTestAssumptionFailed(new Failure(desc, e));;
        } catch (Throwable e) {
        	notifier.fireTestFailure(new Failure(desc, e));
        } finally {
        	notifier.fireTestFinished(desc);
        }
	}
}
