package impl.com.github.paulcwarren.ginkgo4j.runner;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;

public interface Runner extends Runnable {
	ExecutableChain getChain();
	void run();
}
