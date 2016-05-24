package impl.org.paulcwarren.ginkgo4j.runner;

import org.paulcwarren.ginkgo4j.ExecutableChain;

public class SpecSkipperThread implements Runner {

	private ExecutableChain chain;
	
	public SpecSkipperThread(ExecutableChain chain) {
		this.chain = chain;
	}

	@Override
	public ExecutableChain getChain() {
		return this.chain;
	}
	
	@Override
	public void run() {
		// do nothing!
	}
}
