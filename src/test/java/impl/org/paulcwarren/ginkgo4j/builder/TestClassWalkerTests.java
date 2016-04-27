package impl.org.paulcwarren.ginkgo4j.builder;

import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.*;

import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.paulcwarren.ginkgo4j.ExecutableBlock;
import org.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@SuppressWarnings("unused")
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
					order.verify(visitorSpy).describe(anyString(), anyObject());
					order.verify(visitorSpy).justBeforeEach(anyObject());
					order.verify(visitorSpy).context(anyString(), anyObject());
					order.verify(visitorSpy).beforeEach(anyObject());
					order.verify(visitorSpy).it(anyString(), anyObject());
					order.verify(visitorSpy).afterEach(anyObject());
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
				It("should do somehting", () -> {
				});
			});
			AfterEach(() -> {
			});
		});
	}}
	
	static class TestVisitorImpl implements TestVisitor {
		@Override
		public void describe(String text, ExecutableBlock block) {
			try {
				block.invoke();
			} catch (Exception e) {}
		}
		@Override
		public void context(String text, ExecutableBlock block) {
			try {
				block.invoke();
			} catch (Exception e) {}
		}
		@Override
		public void beforeEach(ExecutableBlock block) {
		}
		@Override
		public void justBeforeEach(ExecutableBlock block) {
		}
		@Override
		public void it(String text, ExecutableBlock block) {
		}
		@Override
		public void afterEach(ExecutableBlock block) {
		}
	}
}
