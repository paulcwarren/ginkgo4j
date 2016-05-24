package impl.org.paulcwarren.ginkgo4j.junit;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import impl.org.paulcwarren.ginkgo4j.runner.Runner;

public class JunitSpecRunnerThread extends Thread {

	private Runner runner;
	private RunNotifier notifier;
	private Description desc;
	
	public JunitSpecRunnerThread(Runner runner, RunNotifier notifier, Description desc) {
		this.runner = runner;
		this.notifier = notifier;
		this.desc = desc;
	}
	
	@Override
	public void run() {

		notifier.fireTestStarted(desc);

		try {
			runner.run();
        } catch (AssumptionViolatedException e) {
        	notifier.fireTestAssumptionFailed(new Failure(desc, e));;
        } catch (Throwable e) {
        	notifier.fireTestFailure(new Failure(desc, e));
        } finally {
        	notifier.fireTestFinished(desc);
        }
	}
}
