package impl.com.github.paulcwarren.ginkgo4j.junit;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.annotation.Annotation;

import org.junit.runner.Description;
import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.com.github.paulcwarren.ginkgo4j.builder.TestWalker;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class JunitDescriptionsCollectorTests {
	
	private TestWalker walker;
	private JunitDescriptionsCollector collector;
	private Description description;
	{
		Describe("JunitDescriptionsCollector", () -> {
			JustBeforeEach(() -> {
				walker.walk(collector);
			});
			Context("given a test class with a conventional Describe->Context->It structure", () -> {
				BeforeEach(() -> {
					description = Description.createSuiteDescription(TestClass.class.getName(), (Annotation[])null);
					collector = new JunitDescriptionsCollector(TestClass.class, description);
					walker = new TestWalker(TestClass.class);
				});
				It("should collect all descriptions into a tree structure under the root description", () -> {
					assertThat(description.getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getDisplayName(), is("Test Class.A context.should do something(impl.com.github.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollectorTests$TestClass)"));
				});
				It("should collect all descriptions into a quick access map", () -> {
					assertThat(collector.getDescriptions(), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context.should do something"), is(not(nullValue())));
				});
			});
			Context("given a test class with a top-level Context", () -> {
				BeforeEach(() -> {
					description = Description.createSuiteDescription(TestClass.class.getName(), (Annotation[])null);
					collector = new JunitDescriptionsCollector(TestClass.class, description);
					walker = new TestWalker(TestClass.class);
				});
				It("should collect all descriptions into a tree structure under the root description", () -> {
					assertThat(description.getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getDisplayName(), is("Test Class.A context.should do something(impl.com.github.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollectorTests$TestClass)"));
				});
				It("should collect all descriptions into a quick access map", () -> {
					assertThat(collector.getDescriptions(), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context.should do something"), is(not(nullValue())));
				});
			});
			Context("given a test class with a nested Describe", () -> {
				BeforeEach(() -> {
					description = Description.createSuiteDescription(TestClass.class.getName(), (Annotation[])null);
					collector = new JunitDescriptionsCollector(TestClass.class, description);
					walker = new TestWalker(NestedDescribeTestClass.class);
				});
				It("should collect all descriptions into a tree structure under the root description", () -> {
					assertThat(description.getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getDisplayName(), is("Test Class.A context.A nested describe.should do something(impl.com.github.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollectorTests$TestClass)"));
				});
				It("should collect all descriptions into a quick access map", () -> {
					assertThat(collector.getDescriptions(), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context.A nested describe.should do something"), is(not(nullValue())));
				});
			});
			Context("given a test class with empty labels", () -> {
				BeforeEach(() -> {
					description = Description.createSuiteDescription(EmptyLabelsTestClass.class.getName(), (Annotation[])null);
					collector = new JunitDescriptionsCollector(EmptyLabelsTestClass.class, description);
					walker = new TestWalker(EmptyLabelsTestClass.class);
				});
				It("should use spaces for the description display names", () -> {
					assertThat(description.getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getDisplayName(), is("_EMPTY_._EMPTY_._EMPTY_(impl.com.github.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollectorTests$EmptyLabelsTestClass)"));
				});
				It("should use a placeholder for the description fully-qualified IDs", () -> {
					assertThat(collector.getDescriptions(), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("_EMPTY_._EMPTY_._EMPTY_"), is(not(nullValue())));
				});
			});
		});
	}
	
	public static class TestClass {{
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

	public static class TopLevelContextTestClass {{
		Context("Test Class", () -> {
			JustBeforeEach(() -> {
			});
			Describe("A context", () -> {
				BeforeEach(() -> {
				});
				It("should do something", () -> {
				});
			});
			AfterEach(() -> {
			});
		});
	}}

	public static class NestedDescribeTestClass {{
		Describe("Test Class", () -> {
			JustBeforeEach(() -> {
			});
			Context("A context", () -> {
				BeforeEach(() -> {
				});
				Describe("A nested describe", () -> {
					It("should do something", () -> {
					});
				});
			});
			AfterEach(() -> {
			});
		});
	}}

	public static class EmptyLabelsTestClass {{
		Describe("", () -> {
			Context("", () -> {
				It("", () -> {
				});
			});
		});
	}}
}
