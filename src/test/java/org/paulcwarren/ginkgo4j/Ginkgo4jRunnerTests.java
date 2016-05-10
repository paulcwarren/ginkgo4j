package org.paulcwarren.ginkgo4j;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.FContext;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.FIt;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;

import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;

import impl.org.paulcwarren.ginkgo4j.Spec;
import impl.org.paulcwarren.ginkgo4j.runner.SpecRunnerThread;
import impl.org.paulcwarren.ginkgo4j.runner.SpecSkipperThread;

@SuppressWarnings("unchecked")
@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class Ginkgo4jRunnerTests {

	private Ginkgo4jRunner runner;
	List<Spec> specs;
	{
		Describe("#calculateExecutionChains", () -> {
			Context("when called with no focussed specs", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(TestClass.class);
				});
				It("should return a full chain with no focus", () -> {
					List<ExecutableChain> chains = runner.calculateExecutionChains();
					assertThat(chains, is(not(nullValue())));
					assertThat(chains.size(), is(3));
					assertThat(getExecutableChain("test1", chains).isFocused(), is(false));
					assertThat(getExecutableChain("test2", chains).isFocused(), is(false));
					assertThat(getExecutableChain("test3", chains).isFocused(), is(false));
				});
			});
			Context("when called with at least one focussed spec", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(FittedTestClass.class);
				});
				It("should return a full chain with just the focused spec", () -> {
					List<ExecutableChain> chains = runner.calculateExecutionChains();
					assertThat(chains, is(not(nullValue())));
					assertThat(chains.size(), is(3));
					assertThat(getExecutableChain("test1", chains).isFocused(), is(false));
					assertThat(getExecutableChain("test2", chains).isFocused(), is(true));
					assertThat(getExecutableChain("test3", chains).isFocused(), is(false));
				});
			});
		});

		Describe("#calculateWorkerThreads", () -> {
			Context("when called with no focussed specs", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(TestClass.class);
					runner.getDescription();
				});
				It("should return all SpecRunnerThreads", () -> {
					List<ExecutableChain> chains = runner.calculateExecutionChains();
					List<Thread> workers = runner.calculateWorkerThreads(mock(RunNotifier.class), chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(3));
					assertThat(workers, containsInAnyOrder(is(instanceOf(SpecRunnerThread.class)), is(instanceOf(SpecRunnerThread.class)), is(instanceOf(SpecRunnerThread.class))));
				});
			});
			Context("when called with at least one focussed spec", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(FittedTestClass.class);
					runner.getDescription();
				});
				It("should return one SpecRunnerThread and two SpecSkipperThreads", () -> {
					List<ExecutableChain> chains = runner.calculateExecutionChains();
					List<Thread> workers = runner.calculateWorkerThreads(mock(RunNotifier.class), chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(3));
					assertThat(workers, containsInAnyOrder(is(instanceOf(SpecSkipperThread.class)), is(instanceOf(SpecRunnerThread.class)), is(instanceOf(SpecSkipperThread.class))));
				});
			});
			Context("when called with at a focussed context", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(FittedContextClass.class);
					runner.getDescription();
				});
				It("should return a SpecRunnerThread for all tests in the fitted context", () -> {
					List<ExecutableChain> chains = runner.calculateExecutionChains();
					List<Thread> workers = runner.calculateWorkerThreads(mock(RunNotifier.class), chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(5));
					assertThat(runnerThreads(workers), is(2));
				});
				It("should return SpecSkipperThreads for everything else", () -> {
					List<ExecutableChain> chains = runner.calculateExecutionChains();
					List<Thread> workers = runner.calculateWorkerThreads(mock(RunNotifier.class), chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(5));
					assertThat(skipperThreads(workers), is(3));
				});
			});
		});
	}
	
	private ExecutableChain getExecutableChain(String name, List<ExecutableChain> chains) {
		for (ExecutableChain chain : chains) {
			if (chain.getId().equals(name)) {
				return chain;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private Object runnerThreads(List<Thread> workers) {
		int i=0;
		for (Thread thread : workers) {
			if (thread instanceof SpecRunnerThread) {
				i++;
			}
		}
		return i;
	}

	private Object skipperThreads(List<Thread> workers) {
		int i=0;
		for (Thread thread : workers) {
			if (thread instanceof SpecSkipperThread) {
				i++;
			}
		}
		return i;
	}

	public static class TestClass {{
		It("test1", () -> {});
		It("test2", () -> {});
		It("test3", () -> {});
	}}

	public static class FittedTestClass {{
		It("test1", () -> {});
		FIt("test2", () -> {});
		It("test3", () -> {});
	}}

	public static class FittedContextClass {{
		It("test1", () -> {});
		It("test2", () -> {});
		It("test3", () -> {});
		FContext("a context", () -> {
			It("test4", () -> {});
			It("test5", () -> {});
		});
	}}
}
