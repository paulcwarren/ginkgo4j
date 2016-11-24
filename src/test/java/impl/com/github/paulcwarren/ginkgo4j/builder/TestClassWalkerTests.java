package impl.com.github.paulcwarren.ginkgo4j.builder;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.runner.RunWith;
import org.mockito.InOrder;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class TestClassWalkerTests  {

	private TestWalker walker;
	private TestVisitor visitor;
	private TestVisitor visitorSpy;

	{
		Describe("TestWalker", () -> {
			Context("when walking a test class", () -> {
				BeforeEach(() -> {
					visitor = new TestVisitorImpl();
					visitorSpy = spy(visitor);
					walker = new TestWalker(TestClass.class);
					walker.walk(visitorSpy);
				});
				
				It("should visit all nodes in the tree", () -> {
					InOrder order = inOrder(visitorSpy);
					order.verify(visitorSpy).describe(anyString(), anyObject(), eq(false));
					order.verify(visitorSpy).justBeforeEach(anyObject());
					order.verify(visitorSpy).context(anyString(), anyObject(), eq(false));
					order.verify(visitorSpy).beforeEach(anyObject());
					order.verify(visitorSpy).it(anyString(), anyObject(), eq(false));
					order.verify(visitorSpy).afterEach(anyObject());
					order.verify(visitorSpy).test(anyObject());
					verifyNoMoreInteractions(visitorSpy);
				});
			});
		});
	}
	
	static class TestClass {{
		Describe("Test Class", () -> {
			JustBeforeEach(() -> {
			});
			Context("A context", () -> {
				BeforeEach(() -> {
				});
				It("should do something", () -> {
				});
			});
			AfterEach(() -> {
			});
		});
	}}
	
	static class TestVisitorImpl implements TestVisitor {
		@Override
		public void describe(String text, ExecutableBlock block, boolean isFocused) {
			try {
				block.invoke();
			} catch (Throwable e) {}
		}
		@Override
		public void context(String text, ExecutableBlock block, boolean isFocused) {
			try {
				block.invoke();
			} catch (Throwable e) {}
		}
		@Override
		public void beforeEach(ExecutableBlock block) {
		}
		@Override
		public void justBeforeEach(ExecutableBlock block) {
		}
		@Override
		public void it(String text, ExecutableBlock block, boolean isFocused) {
		}
		@Override
		public void afterEach(ExecutableBlock block) {
		}
		@Override
		public void test(Object test) {
		}
	}
}
