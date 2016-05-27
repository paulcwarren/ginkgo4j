package impl.com.github.paulcwarren.ginkgo4j.runner;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;

public class SpecSkipper implements Runner {

	private ExecutableChain chain;
	
	public SpecSkipper(ExecutableChain chain) {
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
