package impl.com.github.paulcwarren.ginkgo4j.builder;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

public interface TestVisitor {

	void test(Object test);
	void describe(String text, ExecutableBlock block, boolean isFocused);
	void context(String text, ExecutableBlock block, boolean isFocused);
	void beforeEach(ExecutableBlock block);
	void justBeforeEach(ExecutableBlock block);
	void it(String text, ExecutableBlock block, boolean isFocused);
	void afterEach(ExecutableBlock block);
	
}
