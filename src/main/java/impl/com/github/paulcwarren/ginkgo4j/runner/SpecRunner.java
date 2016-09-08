package impl.com.github.paulcwarren.ginkgo4j.runner;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;

public class SpecRunner implements Runner {

	private ExecutableChain chain;
	
	public SpecRunner(ExecutableChain chain) {
		this.chain = chain;
	}
	
	public ExecutableChain getChain() {
		return this.chain;
	}
	
	public void run() throws Exception {

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
