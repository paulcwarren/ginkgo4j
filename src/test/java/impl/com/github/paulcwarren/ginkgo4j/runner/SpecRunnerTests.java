package impl.com.github.paulcwarren.ginkgo4j.runner;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.runner.RunWith;
import org.mockito.InOrder;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;

@RunWith(Ginkgo4jRunner.class)
public class SpecRunnerTests {

	// test classes
	private SpecRunner runner;
	private ExecutableChain chain;

	// mocks
	private RunnerListener listener;
	private ExecutableBlock before;
	private ExecutableBlock justBefore;
	private ExecutableBlock it;
	private ExecutableBlock after;
	
	{
		Describe("SpecRunner", () -> {
			
			BeforeEach(() -> {
				chain = new ExecutableChain("some id");
				listener = mock(RunnerListener.class);
			});

			JustBeforeEach(() -> {
				runner = new SpecRunner(chain, listener);
				try {
					runner.run();
				} catch (Exception e) {}
			});
			
			Context("when run with a simple passing spec", () -> {

				BeforeEach(() ->{
					this.setupOneBeforeOneAfterSpec();
				});
				
				It("should call before each, the spec and then after each", () -> {
					InOrder order = inOrder(listener, before, it, after);
					order.verify(listener).testStarted(eq("some id"));
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(listener).testFinished(eq("some id"));
					verifyNoMoreInteractions(listener, before, it, after);
				});
				
			});
			
			Context("when a before each throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(before).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(listener, before, it, after);
					order.verify(listener).testStarted("some id");
					order.verify(before).invoke();
					order.verify(listener).testException(eq("some id"), isA(Exception.class));
					order.verify(listener).testFinished("some id");
					verifyNoMoreInteractions(listener, before, it, after);
				});
				
			});

			Context("when a JustBeforeEach throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					justBefore = mock(ExecutableBlock.class);
					chain.getJustBeforeEachs().add(justBefore);
					doThrow(Exception.class).when(justBefore).invoke();
				});

				It("should call Before, JustBefore and After", () -> {
					InOrder order = inOrder(listener, before, justBefore, after);
					order.verify(listener).testStarted("some id");
					order.verify(before).invoke();
					order.verify(justBefore).invoke();
					order.verify(after).invoke();
					order.verify(listener).testException(eq("some id"), isA(Exception.class));
					order.verify(listener).testFinished("some id");
					verifyNoMoreInteractions(listener, before, justBefore, after);
				});

			});

			Context("when an it throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(it).invoke();
				});
				
				It("it should still call AfterEach after the spec", () -> {
					InOrder order = inOrder(listener, before, it, after);
					order.verify(listener).testStarted("some id");
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(listener).testException(eq("some id"), isA(Exception.class));
					order.verify(listener).testFinished("some id");
					verifyNoMoreInteractions(listener, before, it, after);
				});
			});


			Context("when an after throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(after).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(listener, before, it, after);
					order.verify(listener).testStarted("some id");
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(listener).testException(eq("some id"), isA(Exception.class));
					order.verify(listener).testFinished("some id");
					verifyNoMoreInteractions(listener, before, it, after);
				});
				
			});
			
			Context("when a spec has a JustBeforeEach", () -> {

				BeforeEach(() ->{
					this.setupOneBeforeOneAfterSpec();
					justBefore = mock(ExecutableBlock.class);
					chain.getJustBeforeEachs().add(justBefore);
				});
				
				It("should run after all Before's", () -> {
					InOrder order = inOrder(listener, before, justBefore, it, after);
					order.verify(listener).testStarted("some id");
					order.verify(before).invoke();
					order.verify(justBefore).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(listener).testFinished("some id");
					verifyNoMoreInteractions(listener, before, justBefore, it, after);
				});
				
			});
			
		});
			
	}

	protected void setupOneBeforeOneAfterSpec() {
		before = mock(ExecutableBlock.class);
		chain.getBeforeEachs().add(before);

		it = mock(ExecutableBlock.class);
		chain.setSpec(it);
		
		after = mock(ExecutableBlock.class);
		chain.getAfterEachs().add(after);
	}

}
