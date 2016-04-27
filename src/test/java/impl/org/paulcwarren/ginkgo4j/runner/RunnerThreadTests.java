package impl.org.paulcwarren.ginkgo4j.runner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.mockito.InOrder;
import org.paulcwarren.ginkgo4j.ExecutableBlock;
import org.paulcwarren.ginkgo4j.ExecutableChain;
import org.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class RunnerThreadTests {

	// test classes
	private RunnerThread runner;
	private ExecutableChain chain;

	// mocks
	private RunNotifier notifier;
	private Description description;
	
	private ExecutableBlock before;
	private ExecutableBlock justBefore;
	private ExecutableBlock it;
	private ExecutableBlock after;
	
	{
		Describe("RunnerThread", () -> {
			
			BeforeEach(() -> {
				description = mock(Description.class);
				chain = new ExecutableChain("some id");
				notifier = mock(RunNotifier.class);
			});

			JustBeforeEach(() -> {
				runner = new RunnerThread(chain, notifier, description);
				runner.run();
			});
			
			Context("when run with a simple passing spec", () -> {

				BeforeEach(() ->{
					this.setupOneBeforeOneAfterSpec();
				});
				
				It("should call before each, the spec and then after each", () -> {
					InOrder order = inOrder(notifier, before, it, after);
					order.verify(notifier).fireTestStarted(any());
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(notifier).fireTestFinished(any());
					verifyNoMoreInteractions(notifier, before, it, after);
				});
				
			});
			
			Context("when a before each throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(before).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(notifier, before, it, after);
					order.verify(notifier).fireTestStarted(any());
					order.verify(before).invoke();
					order.verify(notifier).fireTestFailure(any());
					order.verify(notifier).fireTestFinished(any());
					verifyNoMoreInteractions(notifier, before, it, after);
				});
				
			});

			Context("when an it throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(it).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(notifier, before, it, after);
					order.verify(notifier).fireTestStarted(any());
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(notifier).fireTestFailure(any());
					order.verify(notifier).fireTestFinished(any());
					verifyNoMoreInteractions(notifier, before, it, after);
				});
				
			});


			Context("when an after throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(after).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(notifier, before, it, after);
					order.verify(notifier).fireTestStarted(any());
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(notifier).fireTestFailure(any());
					order.verify(notifier).fireTestFinished(any());
					verifyNoMoreInteractions(notifier, before, it, after);
				});
				
			});
			
			Context("when a spec has a BeforeEach", () -> {

				BeforeEach(() ->{
					this.setupOneBeforeOneAfterSpec();
					justBefore = mock(ExecutableBlock.class);
					chain.getJustBeforeEachs().add(justBefore);
				});
				
				It("should run after all Before's", () -> {
					InOrder order = inOrder(notifier, before, justBefore, it, after);
					order.verify(notifier).fireTestStarted(any());
					order.verify(before).invoke();
					order.verify(justBefore).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					order.verify(notifier).fireTestFinished(any());
					verifyNoMoreInteractions(notifier, before, justBefore, it, after);
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
