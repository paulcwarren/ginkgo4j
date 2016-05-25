package impl.org.paulcwarren.ginkgo4j.runner;

import impl.org.paulcwarren.ginkgo4j.chains.ExecutableChain;

public interface Runner {
	ExecutableChain getChain();
	void run() throws Exception;
}
