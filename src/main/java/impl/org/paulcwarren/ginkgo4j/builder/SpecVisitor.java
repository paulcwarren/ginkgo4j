package impl.org.paulcwarren.ginkgo4j.builder;

import org.paulcwarren.ginkgo4j.ExecutableBlock;

public interface SpecVisitor {
	
	void describe(String text, ExecutableBlock block);
	void context(String text, ExecutableBlock block);
	void beforeEach(ExecutableBlock block);
	void justBeforeEach(ExecutableBlock block);
	void it(String text, ExecutableBlock block);
	void afterEach(ExecutableBlock block);
	
}
