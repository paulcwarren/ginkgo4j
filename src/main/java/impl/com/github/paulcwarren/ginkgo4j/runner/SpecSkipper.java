package impl.com.github.paulcwarren.ginkgo4j.runner;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;

public class SpecSkipper implements Runner {

	private ExecutableChain chain;
	private RunnerListener listener;
	
	public SpecSkipper(ExecutableChain chain) {
		this.chain = chain;
		this.listener = new RunnerListener() {
			@Override public void testStarted(String specId) {}
			@Override public void testException(String specId, Exception e) {}
			@Override public void testFinished(String specId) {}
			@Override public void testSkipped(String specId) {}
		};
	}

	public SpecSkipper(ExecutableChain chain, RunnerListener listener) {
		this.chain = chain;
		this.listener = listener;
	}

	@Override
	public ExecutableChain getChain() {
		return this.chain;
	}
	
	@Override
	public void run() {
		listener.testSkipped(this.getChain().getId());
	}
}
