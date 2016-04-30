package impl.org.paulcwarren.ginkgo4j.runner;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class SpecSkipperThread extends Thread {

	private RunNotifier notifier;
	private Description desc;
	
	public SpecSkipperThread(RunNotifier notifier, Description desc) {
		this.notifier = notifier;
		this.desc = desc;
	}
	
	@Override
	public void run() {
		notifier.fireTestIgnored(desc);
	}
}
