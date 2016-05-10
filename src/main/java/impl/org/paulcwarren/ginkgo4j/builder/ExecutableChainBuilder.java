package impl.org.paulcwarren.ginkgo4j.builder;

import org.paulcwarren.ginkgo4j.ExecutableBlock;
import org.paulcwarren.ginkgo4j.ExecutableChain;

public class ExecutableChainBuilder implements TestVisitor {

	private String filter;
	private ExecutableChain chain;
	
	public ExecutableChainBuilder(String filter) {
		this.filter = filter;
		this.chain = new ExecutableChain(filter);
	}
	
	public ExecutableChain getExecutableChain() {
		return chain;
	}

	@Override
	public void describe(String text, ExecutableBlock block) {
		if (filter.startsWith(text)) {
			filter = filter.replaceFirst(text, "");
			if (filter.startsWith(".")) {
				filter = filter.substring(1, filter.length());
			}
			try {
				block.invoke();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void context(String text, ExecutableBlock block, boolean isFocused) {
		if (filter.startsWith(text)) {
			filter = filter.replaceFirst(text, "");
			if (filter.startsWith(".")) {
				filter = filter.substring(1, filter.length());
			}
			chain.setIsFocused(isFocused);
			try {
				block.invoke();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void beforeEach(ExecutableBlock block) {
		chain.getBeforeEachs().add(block);
	}

	@Override
	public void justBeforeEach(ExecutableBlock block) {
		chain.getJustBeforeEachs().add(block);
	}

	@Override
	public void it(String text, ExecutableBlock block, boolean isFocused) {
		if (filter.startsWith(text)) {
			filter = filter.replaceFirst(text, "");
			if (filter.startsWith(".")) {
				filter = filter.substring(1, filter.length());
			}
			try {
				chain.setSpec(block);
				chain.setIsFocused(isFocused |= chain.isFocused());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void afterEach(ExecutableBlock block) {
		chain.getAfterEachs().add(0, block);
	}

}
