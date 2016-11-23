package impl.com.github.paulcwarren.ginkgo4j.runner;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;

public class SpecRunner implements Runner {

	private ExecutableChain chain;
	private RunnerListener listener;
	
	public SpecRunner(ExecutableChain chain) {
		this.chain = chain;
		this.listener = new RunnerListener() {
			@Override public void testStarted(String specId) {}
			@Override public void testException(String specId, Throwable t) {}
			@Override public void testFinished(String specId) {}
			@Override public void testSkipped(String specId) {}
		};
	}
	
	public SpecRunner(ExecutableChain chain, RunnerListener listener) {
		this.chain = chain;
		this.listener = listener;
	}
	
	public ExecutableChain getChain() {
		return this.chain;
	}
	
	public void run() {
		
		listener.testStarted(this.getChain().getId());
		try {
			chain.execute();
		} catch (Throwable t) {
			listener.testException(this.getChain().getId(), t);
		} finally {
			listener.testFinished(this.getChain().getId());
		}
	}
}
