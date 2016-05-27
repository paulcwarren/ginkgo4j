package impl.com.github.paulcwarren.ginkgo4j.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class JunitSpecSkipperThread extends Thread {

	private RunNotifier notifier;
	private Description desc;
	
	public JunitSpecSkipperThread(RunNotifier notifier, Description desc) {
		this.notifier = notifier;
		this.desc = desc;
	}
	
	@Override
	public void run() {
		notifier.fireTestIgnored(desc);
	}
}
