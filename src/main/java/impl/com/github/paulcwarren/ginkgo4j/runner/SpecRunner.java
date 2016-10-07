package impl.com.github.paulcwarren.ginkgo4j.runner;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;

public class SpecRunner implements Runner {

	private ExecutableChain chain;
	private RunnerListener listener;
	
	public SpecRunner(ExecutableChain chain) {
		this.chain = chain;
		this.listener = new RunnerListener() {
			@Override public void testStarted(String specId) {}
			@Override public void testException(String specId, Exception e) {}
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
			executeChain();
		} catch (Exception e) {
			listener.testException(this.getChain().getId(), e);
		} finally {
			listener.testFinished(this.getChain().getId());
		}
	}

	void executeChain() throws Exception {
		
		for (ExecutableBlock block : chain.getBeforeEachs()) {
			block.invoke();
		}

		try {
			for (ExecutableBlock block : chain.getJustBeforeEachs()) {
				block.invoke();
			}

			chain.getSpec().invoke();
		} finally {
			for (ExecutableBlock block : chain.getAfterEachs()) {
				block.invoke();
			}
		}
	}
}
