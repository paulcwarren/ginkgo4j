package impl.com.github.paulcwarren.ginkgo4j.junit;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.HashMap;
import java.util.Map;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.InOrder;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.com.github.paulcwarren.ginkgo4j.runner.SpecRunner;

@RunWith(Ginkgo4jRunner.class)
public class JunitRunnerListenerTests {

	// test classes
	private JunitRunnerListener listener;

	// mocks
	private RunNotifier notifier;
	private Description description;
	private SpecRunner runner;
	
	{
		Describe("JunitRunnerListener", () -> {

			BeforeEach(() -> {
				runner = mock(SpecRunner.class);
				notifier = mock(RunNotifier.class);
				description = mock(Description.class);
				Map<String,Description> descriptions = new HashMap<>();
				descriptions.put("some id", description);
				
				listener = new JunitRunnerListener(notifier, descriptions);
			});

			Context("when listening to a starting test", () -> {
				BeforeEach(() -> {
					listener.testStarted("some id");
				});
				
				It("should notify junit", () -> {
					InOrder order = inOrder(notifier);
					order.verify(notifier).fireTestStarted(eq(description));
					verifyNoMoreInteractions(notifier);
				});
			});

			Context("when listening to a failing test", () -> {
				BeforeEach(() -> {
					listener.testException("some id", new AssumptionViolatedException(""));
				});
				
				It("should notify junit", () -> {
					InOrder order = inOrder(notifier);
					order.verify(notifier).fireTestAssumptionFailed(isA(Failure.class));
					verifyNoMoreInteractions(notifier);
				});
			});

			Context("when listening to an erroring test", () -> {
				BeforeEach(() -> {
					listener.testException("some id", new IllegalStateException(""));
				});
				
				It("should notify junit", () -> {
					InOrder order = inOrder(notifier);
					order.verify(notifier).fireTestFailure(isA(Failure.class));
					verifyNoMoreInteractions(notifier);
				});
			});

			Context("when listening to a finishing test", () -> {
				BeforeEach(() -> {
					listener.testFinished("some id");
				});
				
				It("should notify junit", () -> {
					InOrder order = inOrder(notifier);
					order.verify(notifier).fireTestFinished(eq(description));
					verifyNoMoreInteractions(notifier);
				});
			});

			Context("when listening to a test being skipped", () -> {
				BeforeEach(() -> {
					listener.testSkipped("some id");
				});
				
				It("should notify junit", () -> {
					InOrder order = inOrder(notifier);
					order.verify(notifier).fireTestIgnored(eq(description));
					verifyNoMoreInteractions(notifier);
				});
			});
		});
	}
}
