package impl.org.paulcwarren.ginkgo4j.junit;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.InOrder;
import org.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.org.paulcwarren.ginkgo4j.runner.SpecRunnerThread;

@RunWith(Ginkgo4jRunner.class)
public class JunitSpecRunnerThreadTests {

	// test classes
	private JunitSpecRunnerThread junitRunner;

	// mocks
	private RunNotifier notifier;
	private Description description;
	private SpecRunnerThread runner;
	
	{
		Describe("RunnerThread", () -> {

			BeforeEach(() -> {
				runner = mock(SpecRunnerThread.class);
				notifier = mock(RunNotifier.class);
				description = mock(Description.class);
			});

			JustBeforeEach(() -> {
				junitRunner = new JunitSpecRunnerThread(runner, notifier, description);
				junitRunner.run();
			});
			
			Context("when run with a passing test", () -> {

				It("should notify junit before and after the runner is called", () -> {
					InOrder order = inOrder(notifier, runner);
					order.verify(notifier).fireTestStarted(eq(description));
					order.verify(runner).run();
					order.verify(notifier).fireTestFinished(eq(description));
					verifyNoMoreInteractions(notifier, runner);
				});
				
			});
			
			Context("when run with a failing test", () -> {

				BeforeEach(() -> {
					doThrow(new AssumptionViolatedException("whatever")).when(runner).run();
				});

				It("should notify before and exception after the runner is called", () -> {
					InOrder order = inOrder(notifier, runner);
					order.verify(notifier).fireTestStarted(eq(description));
					order.verify(runner).run();
					order.verify(notifier).fireTestAssumptionFailed(isA(Failure.class));
					order.verify(notifier).fireTestFinished(eq(description));
					verifyNoMoreInteractions(notifier, runner);
				});
			});

			Context("when run with an erroring test", () -> {

				BeforeEach(() -> {
					doThrow(new Exception()).when(runner).run();
				});

				It("should notify before and exception after the runner is called", () -> {
					InOrder order = inOrder(notifier, runner);
					order.verify(notifier).fireTestStarted(eq(description));
					order.verify(runner).run();
					order.verify(notifier).fireTestFailure(isA(Failure.class));
					order.verify(notifier).fireTestFinished(eq(description));
					verifyNoMoreInteractions(notifier, runner);
				});
			});
		});
	}
}
