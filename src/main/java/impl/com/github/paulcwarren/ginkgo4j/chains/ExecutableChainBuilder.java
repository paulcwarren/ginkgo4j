package impl.com.github.paulcwarren.ginkgo4j.chains;

import java.util.regex.Pattern;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.builder.TestVisitor;

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
	public void describe(String text, ExecutableBlock block, boolean isFocused) {
		if (filter.startsWith(text + ".")) {
			filter = splitFilter(filter, text);
			chain.setIsFocused(isFocused);
			try {
				block.invoke();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void context(String text, ExecutableBlock block, boolean isFocused) {
		if (filter.startsWith(text + ".")) {
			filter = splitFilter(filter, text);
			chain.setIsFocused(isFocused |= chain.isFocused());
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
		if (filter.equals(text)) {
			filter = splitFilter(filter, text);
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

	private String splitFilter(String filter, String text) {
		String newFilter = filter.replaceFirst(Pattern.quote(text), "");
		if (newFilter.startsWith(".")) {
			newFilter = newFilter.substring(1, newFilter.length());
		}
		return newFilter;
	}

	@Override
	public void test(Object test) {
		chain.setTestObject(test);
	}

}
