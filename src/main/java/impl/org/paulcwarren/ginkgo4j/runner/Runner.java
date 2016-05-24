package impl.org.paulcwarren.ginkgo4j.runner;

import org.paulcwarren.ginkgo4j.ExecutableChain;

public interface Runner {
	ExecutableChain getChain();
	void run() throws Exception;
}
