package com.github.paulcwarren.ginkgo4j;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.FContext;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.FDescribe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.FIt;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static com.github.paulcwarren.ginkgo4j.matchers.Ginkgo4jMatchers.eventually;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.ArgumentCaptor;

import impl.com.github.paulcwarren.ginkgo4j.Spec;
import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChain;
import impl.com.github.paulcwarren.ginkgo4j.runner.Runner;
import impl.com.github.paulcwarren.ginkgo4j.runner.SpecRunner;
import impl.com.github.paulcwarren.ginkgo4j.runner.SpecSkipper;

@SuppressWarnings("unchecked")
@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class Ginkgo4jRunnerTests {

	private Ginkgo4jRunner runner;
	private Throwable t;
	
	//mocks
	private RunNotifier notifier;
	
	List<Spec> specs;
	{
		Describe("#run", () -> {
			JustBeforeEach(() -> {
				try {
					runner.run(notifier);
				} catch (Throwable t) {}
			});
			BeforeEach(() -> {
				notifier = mock(RunNotifier.class);
			});
			Context("when called with a passing test", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(TestClass.class);
				});
				It("should execute successfully", () -> {
					assertThat(t, is(nullValue()));
					verify(notifier, times(3)).fireTestStarted(anyObject());
					verify(notifier, times(3)).fireTestFinished(anyObject());
				});
			});
			Context("when a test that throws an NPE", () -> {
//				Context("from a Describe block", () -> {
//					BeforeEach(() -> {
//						NPEThrowingTestClass.describe = null;
//						NPEThrowingTestClass.context = "";
//						NPEThrowingTestClass.jbe = "";
//						NPEThrowingTestClass.be = "";
//						NPEThrowingTestClass.ae = "";
//						runner = new Ginkgo4jRunner(NPEThrowingTestClass.class);
//					});
//					It("should report failure to junit's notifier", () -> {
//						ArgumentCaptor<Failure> failureCaptor = ArgumentCaptor.forClass(Failure.class);
//						eventually(
//							() -> {
//								try {
//									return failureCaptor.getValue();
//								} catch (Throwable t) {}
//								return null;
//							},
//							(result) -> {
//								assertThat(result, is(not(nullValue())));
//								assertThat(result.getException(), is(notNullValue()));
//								assertThat(result.getException().getStackTrace()[0].getLineNumber(), is(18));
//							}
//						);
//					});
//				});
				Context("from a BeforeEach block", () -> {
					BeforeEach(() -> {
						NPEThrowingTestClass.describe = "";
						NPEThrowingTestClass.context = "";
						NPEThrowingTestClass.jbe = "";
						NPEThrowingTestClass.be = null;
						NPEThrowingTestClass.ae = "";
						runner = new Ginkgo4jRunner(NPEThrowingTestClass.class);
					});
					It("should report failure to junit's notifier", () -> {
						verify(notifier, times(1)).fireTestStarted(anyObject());
						
						ArgumentCaptor<Failure> failureCaptor = ArgumentCaptor.forClass(Failure.class);
						verify(notifier, times(1)).fireTestFailure(failureCaptor.capture());
						assertThat(failureCaptor.getValue().getException().getStackTrace()[0].getLineNumber(), is(20));
						
						verify(notifier, times(1)).fireTestFinished(anyObject());
					});
				});
				Context("from a JustBeforeEach block", () -> {
					BeforeEach(() -> {
						NPEThrowingTestClass.describe = "";
						NPEThrowingTestClass.context = "";
						NPEThrowingTestClass.jbe = null;
						NPEThrowingTestClass.be = "";
						NPEThrowingTestClass.ae = "";
						runner = new Ginkgo4jRunner(NPEThrowingTestClass.class);
					});
					It("should report failure to junit's notifier", () -> {
						verify(notifier, times(1)).fireTestStarted(anyObject());
						
						ArgumentCaptor<Failure> failureCaptor = ArgumentCaptor.forClass(Failure.class);
						verify(notifier, times(1)).fireTestFailure(failureCaptor.capture());
						assertThat(failureCaptor.getValue().getException().getStackTrace()[0].getLineNumber(), is(23));
						
						verify(notifier, times(1)).fireTestFinished(anyObject());
					});
				});
//				Context("from a Context block", () -> {
//					BeforeEach(() -> {
//						NPEThrowingTestClass.describe = "";
//						NPEThrowingTestClass.context = null;
//						NPEThrowingTestClass.jbe = "";
//						NPEThrowingTestClass.be = "";
//						NPEThrowingTestClass.ae = "";
//						runner = new Ginkgo4jRunner(NPEThrowingTestClass.class);
//					});
//					It("should report failure to junit's notifier", () -> {
//						verify(notifier, times(1)).fireTestStarted(anyObject());
//
//						ArgumentCaptor<Failure> failureCaptor = ArgumentCaptor.forClass(Failure.class);
//						verify(notifier, times(1)).fireTestFailure(failureCaptor.capture());
//						assertThat(failureCaptor.getValue().getException().getStackTrace()[0].getLineNumber(), is(26));
//
//						verify(notifier, times(1)).fireTestFinished(anyObject());
//					});
//				});
				Context("from an AfterEach block", () -> {
					BeforeEach(() -> {
						NPEThrowingTestClass.describe = "";
						NPEThrowingTestClass.context = "";
						NPEThrowingTestClass.jbe = "";
						NPEThrowingTestClass.be = "";
						NPEThrowingTestClass.ae = null;
						runner = new Ginkgo4jRunner(NPEThrowingTestClass.class);
					});
					It("should report failure to junit's notifier", () -> {
						verify(notifier, times(1)).fireTestStarted(anyObject());
						
						ArgumentCaptor<Failure> failureCaptor = ArgumentCaptor.forClass(Failure.class);
						verify(notifier, times(1)).fireTestFailure(failureCaptor.capture());
						assertThat(failureCaptor.getValue().getException().getStackTrace()[0].getLineNumber(), is(30));
						
						verify(notifier, times(1)).fireTestFinished(anyObject());
					});
				});
			});
			Context("when a test calls method that declare 'throws Throwable'", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(DeclaresThrowableTestClass.class);
				});
				It("should compile", () -> {
					// see DeclaresThrowableTestClass definition below
					assertThat(true, is(true));
				});
			});
		});

		Describe("#calculateExecutionChains", () -> {
			Context("when called with no focussed specs", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(TestClass.class);
				});
				It("should return a full chain with no focus", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(TestClass.class);
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
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(FittedTestClass.class);
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
				It("should return all SpecRunners", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(TestClass.class);
					List<Runner> workers = Ginkgo4jRunner.calculateWorkerThreads(chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(3));
					assertThat(workers, containsInAnyOrder(is(instanceOf(SpecRunner.class)), is(instanceOf(SpecRunner.class)), is(instanceOf(SpecRunner.class))));
				});
			});
			Context("when called with at least one focused spec", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(FittedTestClass.class);
					runner.getDescription();
				});
				It("should return one SpecRunnerThread and two SpecSkipperThreads", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(FittedTestClass.class);
					List<Runner> workers = Ginkgo4jRunner.calculateWorkerThreads(chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(3));
					assertThat(workers, containsInAnyOrder(is(instanceOf(SpecSkipper.class)), is(instanceOf(SpecRunner.class)), is(instanceOf(SpecSkipper.class))));
				});
			});
			Context("when called with at a focused context", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(FittedContextClass.class);
					runner.getDescription();
				});
				It("should return a SpecRunnerThread for all tests in the fitted context", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(FittedContextClass.class);
					List<Runner> workers = Ginkgo4jRunner.calculateWorkerThreads(chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(5));
					assertThat(runnerThreads(workers), is(2));
				});
				It("should return SpecSkipperThreads for everything else", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(FittedContextClass.class);
					List<Runner> workers = Ginkgo4jRunner.calculateWorkerThreads(chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(5));
					assertThat(skipperThreads(workers), is(3));
				});
			});
			Context("when called with at a focused describe", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(FittedDescribeClass.class);
					runner.getDescription();
				});
				It("should return a SpecRunnerThread for all tests in the fitted describe", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(FittedDescribeClass.class);
					List<Runner> workers = Ginkgo4jRunner.calculateWorkerThreads(chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(7));
					assertThat(runnerThreads(workers), is(2));
				});
				It("should return SpecSkipperThreads for everything else", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(FittedDescribeClass.class);
					List<Runner> workers = Ginkgo4jRunner.calculateWorkerThreads(chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(7));
					assertThat(skipperThreads(workers), is(5));
				});
			});
			Context("when called with an empty describe label", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(EmptyDescribeClass.class);
					runner.getDescription();
				});
				It("should return a SpecRunnerThread for all tests in the fitted describe", () -> {
					List<ExecutableChain> chains = Ginkgo4jRunner.calculateExecutionChains(EmptyDescribeClass.class);
					List<Runner> workers = Ginkgo4jRunner.calculateWorkerThreads(chains);

					assertThat(workers, is(not(nullValue())));
					assertThat(workers.size(), is(1));
					assertThat(runnerThreads(workers), is(1));
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
	
	private Object runnerThreads(List<Runner> workers) {
		int i=0;
		for (Runner thread : workers) {
			if (thread instanceof SpecRunner) {
				i++;
			}
		}
		return i;
	}

	private Object skipperThreads(List<Runner> workers) {
		int i=0;
		for (Runner thread : workers) {
			if (thread instanceof SpecSkipper) {
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

	public static class FittedDescribeClass {{
		It("test1", () -> {});
		It("test2", () -> {});
		FDescribe("a describe", () -> {
			It("test6", () -> {});
			It("test7", () -> {});
		});
		It("test3", () -> {});
		Context("a context", () -> {
			It("test4", () -> {});
			It("test5", () -> {});
		});
	}}
	
	public static class EmptyDescribeClass {{
		Describe("", () -> {
		    It("stuff", () -> {});
		}); 
	}}
	
	/*
	 * For compilation checks ONLY
	 */
	public static class DeclaresThrowableTestClass {{
		Describe("describe", () -> {
			declaresThrowable();
			Context("context", () -> {
				declaresThrowable();
				JustBeforeEach(() -> {
					declaresThrowable();
				});
				BeforeEach(() -> {
					declaresThrowable();
				});
				It("it", () -> {
					declaresThrowable();
				});
				AfterEach(() -> {
					declaresThrowable();
				});
			}); 
		}); 
		FDescribe("fdescribe", () -> {
			declaresThrowable();
			FContext("fcontext", () -> {
				declaresThrowable();
				FIt("fit", () -> {
					declaresThrowable();
				});
			});
		});
	}}

	public static void declaresThrowable() throws Throwable {
	}
}
