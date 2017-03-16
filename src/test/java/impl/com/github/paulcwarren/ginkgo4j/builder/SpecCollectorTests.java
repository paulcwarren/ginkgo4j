package impl.com.github.paulcwarren.ginkgo4j.builder;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.FIt;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.com.github.paulcwarren.ginkgo4j.chains.SpecsCollector;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class SpecCollectorTests {
	
	private TestWalker walker;
	private SpecsCollector collector;
	private Throwable t;
	{
		Describe("Spec Collector", () -> {
			BeforeEach(() -> {
				collector = new SpecsCollector();
				walker = new TestWalker(TestClass.class);
			});
			
			JustBeforeEach(() -> {
				try {
					walker.walk(collector);
				} catch (Throwable t) {
					this.t = t;
				}
			});
			
			It("should collect all specs", () -> {
				assertThat(collector.getSpecs(), is(not(nullValue())));
				assertThat(collector.getSpecs().size(), is(1));
			});
			
			Context("when specs are focused", () -> {
				BeforeEach(() -> {
					walker = new TestWalker(FItTestClass.class);
				});
				
				It("should collect focussed specs only", () -> {
					assertThat(collector.getSpecs(), is(not(nullValue())));
					assertThat(collector.getSpecs().size(), is(2));
					assertThat(collector.getSpecs().get(0).getId(), is("Test Class.A context.focussed"));
					assertThat(collector.getSpecs().get(0).isFocused(), is(true));
					assertThat(collector.getSpecs().get(1).getId(), is("Test Class.A context.not focussed"));
					assertThat(collector.getSpecs().get(1).isFocused(), is(false));
				});
			});
			
			Context("when specs contain empty labels", () -> {
				BeforeEach(() -> {
					walker = new TestWalker(EmptyLabelsTestClass.class);
				});
				
				It("should collect specs with parsed IDs", () -> {
					assertThat(collector.getSpecs(), is(not(nullValue())));
					assertThat(collector.getSpecs().size(), is(1));
					assertThat(collector.getSpecs().get(0).getId(), is("_EMPTY_._EMPTY_._EMPTY_"));
				});
			});
			
			Context("when a describe throws an exception", () -> {
				BeforeEach(() -> {
					walker = new TestWalker(ExceptionThrowingDescribeTestClass.class);
				});
				
				It("should throw the exception", () -> {
					assertThat(t, instanceOf(NullPointerException.class));
				});
			});
			
			Context("when a context throws an exception", () -> {
				BeforeEach(() -> {
					walker = new TestWalker(ExceptionThrowingContextTestClass.class);
				});
				
				It("should throw the exception", () -> {
					assertThat(t, instanceOf(NullPointerException.class));
				});
			});
			
			Context("when a beforeeach throws an exception", () -> {
				BeforeEach(() -> {
					walker = new TestWalker(ExceptionThrowingBeforeEachTestClass.class);
				});
				
				It("should throw the exception", () -> {
					assertThat(t, is(nullValue()));
				});
			});
			
			Context("when a justbeforeeach throws an exception", () -> {
				BeforeEach(() -> {
					walker = new TestWalker(ExceptionThrowingJustBeforeEachTestClass.class);
				});
				
				It("should not throw the exception", () -> {
					assertThat(t, is(nullValue()));
				});
			});
			
			Context("when an aftereach throws an exception", () -> {
				BeforeEach(() -> {
					walker = new TestWalker(ExceptionThrowingAfterEachTestClass.class);
				});
				
				It("should not throw the exception", () -> {
					assertThat(t, is(nullValue()));
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

	static class FItTestClass {{
		Describe("Test Class", () -> {
			JustBeforeEach(() -> {
			});
			Context("A context", () -> {
				BeforeEach(() -> {
				});
				FIt("focussed", () -> {
				});
				It("not focussed", () -> {
				});
			});
			AfterEach(() -> {
			});
		});
	}}

	static class EmptyLabelsTestClass {{
		Describe("", () -> {
			Context("", () -> {
				It("", () -> {
				});
			});
			AfterEach(() -> {
			});
		});
	}}

	static class ExceptionThrowingDescribeTestClass {
		private String something;
		{
			Describe("A describe", () -> {
				something.length();
				Context("A context", () -> {
					It("A test", () -> {
					});
				});
			});
		}
	}

	static class ExceptionThrowingContextTestClass {
		private String something;
		{
			Describe("A describe", () -> {
				Context("A context", () -> {
					something.length();
					It("A test", () -> {
					});
				});
			});
		}
	}

	static class ExceptionThrowingBeforeEachTestClass {
		private String something;
		{
			Describe("A describe", () -> {
				BeforeEach(() -> {
					something.length();
				});
				Context("A context", () -> {
					It("A test", () -> {
					});
				});
			});
		}
	}

	static class ExceptionThrowingJustBeforeEachTestClass {
		private String something;
		{
			Describe("A describe", () -> {
				JustBeforeEach(() -> {
					something.length();
				});
				Context("A context", () -> {
					It("A test", () -> {
					});
				});
			});
		}
	}

	static class ExceptionThrowingAfterEachTestClass {
		private String something;
		{
			Describe("A describe", () -> {
				Context("A context", () -> {
					It("A test", () -> {
					});
				});
				AfterEach(() -> {
					something.length();
				});
			});
		}
	}
}
