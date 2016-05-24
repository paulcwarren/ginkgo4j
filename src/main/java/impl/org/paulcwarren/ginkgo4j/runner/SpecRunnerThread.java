package impl.org.paulcwarren.ginkgo4j.runner;

import org.paulcwarren.ginkgo4j.ExecutableBlock;
import org.paulcwarren.ginkgo4j.ExecutableChain;

public class SpecRunnerThread implements Runner {

	private ExecutableChain chain;
	
	public SpecRunnerThread(ExecutableChain chain) {
		this.chain = chain;
	}
	
	public ExecutableChain getChain() {
		return this.chain;
	}
	
	public void run() throws Exception {

		for (ExecutableBlock block : chain.getBeforeEachs()) {
			block.invoke();
		}
	
		for (ExecutableBlock block : chain.getJustBeforeEachs()) {
			block.invoke();
		}
	
		chain.getSpec().invoke();

		for (ExecutableBlock block : chain.getAfterEachs()) {
			block.invoke();
    	}
	}
}
