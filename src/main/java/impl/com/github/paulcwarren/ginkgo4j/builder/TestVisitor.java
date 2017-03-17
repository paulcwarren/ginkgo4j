package impl.com.github.paulcwarren.ginkgo4j.builder;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

public interface TestVisitor {

	void test(Object test);
	void describe(String text, ExecutableBlock block, boolean isFocused) throws Throwable;
	void context(String text, ExecutableBlock block, boolean isFocused) throws Throwable;
	void beforeEach(ExecutableBlock block) throws Throwable;
	void justBeforeEach(ExecutableBlock block) throws Throwable;
	void it(String text, ExecutableBlock block, boolean isFocused) throws Throwable;
	void afterEach(ExecutableBlock block) throws Throwable;
	
}
