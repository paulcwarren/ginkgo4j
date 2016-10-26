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

import impl.com.github.paulcwarren.ginkgo4j.Context;
import impl.com.github.paulcwarren.ginkgo4j.Describe;
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
				
				It("should call BeforeEach, It and then AfterEach", () -> {
					InOrder order = inOrder(listener, before, it, after);
					order.verify(listener).testStarted(eq("some id"));
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(listener).testFinished(eq("some id"));
					verifyNoMoreInteractions(listener, before, it, after);
				});
				
			});
			
			Context("when a BeforeEach throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(before).invoke();
				});
				
				It("should call AfterEach but not the It", () -> {
					InOrder order = inOrder(listener, before, it, after);
					order.verify(listener).testStarted("some id");
					order.verify(before).invoke();
					order.verify(after).invoke();
					order.verify(listener).testException(eq("some id"), isA(Exception.class));
					order.verify(listener).testFinished("some id");
					verifyNoMoreInteractions(listener, before, it);
				});
				
			});

			Context("when a JustBeforeEach throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					justBefore = mock(ExecutableBlock.class);
					chain.getContext().add(new Context(""));
					chain.getContext().get(0).setJustBeforeEach(justBefore);
					doThrow(Exception.class).when(justBefore).invoke();
				});

				It("should call BeforeEach, JustBeforeEach and AfterEach", () -> {
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

			Context("when an It throws an Exception", () -> {

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


			Context("when an AfterEach throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(after).invoke();
				});
				
				It("should call the BeforeEach, the It and the AfterEach", () -> {
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
			
			Context("when a It has a JustBeforeEach", () -> {

				BeforeEach(() ->{
					this.setupOneBeforeOneAfterSpec();
					justBefore = mock(ExecutableBlock.class);
					chain.getContext().add(new Context(""));
					chain.getContext().get(0).setJustBeforeEach(justBefore);
				});
				
				It("should run after all BeforeEach's", () -> {
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
			
			Context("when a Context has a Describe", () -> {

				BeforeEach(() ->{
					chain.getContext().add(new Context("parent"));
					chain.getContext().add(new Describe("child"));
					
					justBefore = mock(ExecutableBlock.class);
					chain.getContext().get(0).setJustBeforeEach(justBefore);

					before = mock(ExecutableBlock.class);
					chain.getContext().get(1).setBeforeEach(before);

					it = mock(ExecutableBlock.class);
					chain.setSpec(it);
				});
				
				It("should run after all BeforeEach's", () -> {
					InOrder order = inOrder(listener, before, justBefore, it);
					order.verify(listener).testStarted("some id");
					order.verify(before).invoke();
					order.verify(justBefore).invoke();
					order.verify(it).invoke();
					order.verify(listener).testFinished("some id");
					verifyNoMoreInteractions(listener, before, justBefore, it);
				});
			});
		});
	}

	protected void setupOneBeforeOneAfterSpec() {
		chain.getContext().add(new Context(""));
		
		before = mock(ExecutableBlock.class);
		chain.getContext().get(0).setBeforeEach(before);

		it = mock(ExecutableBlock.class);
		chain.setSpec(it);
		
		after = mock(ExecutableBlock.class);
		chain.getContext().get(0).setAfterEach(after);
	}

}
