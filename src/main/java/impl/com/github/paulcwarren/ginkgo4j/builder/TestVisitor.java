package impl.com.github.paulcwarren.ginkgo4j.builder;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

public interface TestVisitor {

	void test(Object test);
	void describe(String text, ExecutableBlock block, boolean isFocused) throws Exception;
	void context(String text, ExecutableBlock block, boolean isFocused) throws Exception;
	void beforeEach(ExecutableBlock block) throws Exception;
	void justBeforeEach(ExecutableBlock block) throws Exception;
	void it(String text, ExecutableBlock block, boolean isFocused);
	void afterEach(ExecutableBlock block) throws Exception;
	
}
