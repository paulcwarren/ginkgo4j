package impl.org.paulcwarren.ginkgo4j.runner;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;

import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.paulcwarren.ginkgo4j.ExecutableBlock;
import org.paulcwarren.ginkgo4j.ExecutableChain;
import org.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class SpecRunnerThreadTests {

	// test classes
	private SpecRunnerThread runner;
	private ExecutableChain chain;

	// mocks
	private ExecutableBlock before;
	private ExecutableBlock justBefore;
	private ExecutableBlock it;
	private ExecutableBlock after;
	
	{
		Describe("RunnerThread", () -> {
			
			BeforeEach(() -> {
				chain = new ExecutableChain("some id");
			});

			JustBeforeEach(() -> {
				runner = new SpecRunnerThread(chain);
				try {
					runner.run();
				} catch (Exception e) {}
			});
			
			Context("when run with a simple passing spec", () -> {

				BeforeEach(() ->{
					this.setupOneBeforeOneAfterSpec();
				});
				
				It("should call before each, the spec and then after each", () -> {
					InOrder order = inOrder(before, it, after);
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					verifyNoMoreInteractions(/*notifier, */before, it, after);
				});
				
			});
			
			Context("when a before each throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(before).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(/*notifier, */before, it, after);
					order.verify(before).invoke();
					verifyNoMoreInteractions(/*notifier, */before, it, after);
				});
				
			});

			Context("when an it throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(it).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(/*notifier, */before, it, after);
					order.verify(before).invoke();
					order.verify(it).invoke();
					verifyNoMoreInteractions(/*notifier, */before, it, after);
				});
				
			});


			Context("when an after throws an Exception", () -> {

				BeforeEach(() ->{
					setupOneBeforeOneAfterSpec();
					doThrow(Exception.class).when(after).invoke();
				});
				
				It("should call before each but not the spec or afters", () -> {
					InOrder order = inOrder(/*notifier, */before, it, after);
					order.verify(before).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					verifyNoMoreInteractions(/*notifier, */before, it, after);
				});
				
			});
			
			Context("when a spec has a BeforeEach", () -> {

				BeforeEach(() ->{
					this.setupOneBeforeOneAfterSpec();
					justBefore = mock(ExecutableBlock.class);
					chain.getJustBeforeEachs().add(justBefore);
				});
				
				It("should run after all Before's", () -> {
					InOrder order = inOrder(/*notifier, */before, justBefore, it, after);
					order.verify(before).invoke();
					order.verify(justBefore).invoke();
					order.verify(it).invoke();
					order.verify(after).invoke();
					verifyNoMoreInteractions(/*notifier, */before, justBefore, it, after);
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
