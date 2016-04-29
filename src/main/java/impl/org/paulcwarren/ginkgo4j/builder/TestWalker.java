package impl.org.paulcwarren.ginkgo4j.builder;

import org.paulcwarren.ginkgo4j.ExecutableBlock;
import org.paulcwarren.ginkgo4j.Ginkgo4jDSL;

public class TestWalker implements TestVisitor {
	
	private Class<?> testClass;
	private TestVisitor[] visitors;
	
	public TestWalker(Class<?> testClass, TestVisitor visitor) {
		this.testClass = testClass;
	}
	
	public TestWalker(Class<?> testClass) {
		this.testClass = testClass;
	}
	
	public void walk() {
		try {
			Ginkgo4jDSL.setVisitor(this);
			try {
				testClass.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			Ginkgo4jDSL.unsetVisitor(this);
		}
	}

	public void walk(TestVisitor...visitors) {
		this.visitors = visitors;
		try {
			Ginkgo4jDSL.setVisitor(this);
			try {
				testClass.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			Ginkgo4jDSL.unsetVisitor(this);
		}
	}

	@Override
	public void describe(String text, ExecutableBlock block) {
		for (TestVisitor visitor : visitors) {
			visitor.describe(text, block);
		}
	}

	@Override
	public void context(String text, ExecutableBlock block) {
		for (TestVisitor visitor : visitors) {
			visitor.context(text, block);
		}
	}

	@Override
	public void beforeEach(ExecutableBlock block) {
		for (TestVisitor visitor : visitors) {
			visitor.beforeEach(block);
		}
	}

	@Override
	public void justBeforeEach(ExecutableBlock block) {
		for (TestVisitor visitor : visitors) {
			visitor.justBeforeEach(block);
		}
	}

	@Override
	public void it(String text, ExecutableBlock block, boolean isFocused) {
		for (TestVisitor visitor : visitors) {
			visitor.it(text, block, isFocused);
		}
	}

	@Override
	public void afterEach(ExecutableBlock block) {
		for (TestVisitor visitor : visitors) {
			visitor.afterEach(block);
		}
	}
}
