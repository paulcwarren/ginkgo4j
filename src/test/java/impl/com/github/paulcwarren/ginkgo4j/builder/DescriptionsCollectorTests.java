package impl.com.github.paulcwarren.ginkgo4j.builder;

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

import impl.com.github.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollector;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class DescriptionsCollectorTests {
	
	private TestWalker walker;
	private JunitDescriptionsCollector collector;
	private Description description;
	{
		Describe("Descriptions Collector", () -> {
			JustBeforeEach(() -> {
				walker.walk(collector);
			});
			Context("given a test class with a conventional Describe->Context->It structure", () -> {
				BeforeEach(() -> {
					description = Description.createSuiteDescription(TestClass.class.getName(), (Annotation[])null);
					collector = new JunitDescriptionsCollector(description);
					walker = new TestWalker(TestClass.class);
				});
				It("should collect all descriptions into a tree structure under the root description", () -> {
					assertThat(description.getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getDisplayName(), is("Test Class"));
					assertThat(description.getChildren().get(0).getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getChildren().get(0).getDisplayName(), is("A context"));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().get(0).getDisplayName(), is("should do something(It)"));
				});
				It("should collect all descriptions into a quick access map", () -> {
					assertThat(collector.getDescriptions(), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class"), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context"), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context.should do something"), is(not(nullValue())));
				});
			});
			Context("given a test class with a top-level Context", () -> {
				BeforeEach(() -> {
					description = Description.createSuiteDescription(TestClass.class.getName(), (Annotation[])null);
					collector = new JunitDescriptionsCollector(description);
					walker = new TestWalker(TestClass.class);
				});
				It("should collect all descriptions into a tree structure under the root description", () -> {
					assertThat(description.getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getDisplayName(), is("Test Class"));
					assertThat(description.getChildren().get(0).getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getChildren().get(0).getDisplayName(), is("A context"));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().get(0).getDisplayName(), is("should do something(It)"));
				});
				It("should collect all descriptions into a quick access map", () -> {
					assertThat(collector.getDescriptions(), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class"), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context"), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context.should do something"), is(not(nullValue())));
				});
			});
			Context("given a test class with a nested Describe", () -> {
				BeforeEach(() -> {
					description = Description.createSuiteDescription(TestClass.class.getName(), (Annotation[])null);
					collector = new JunitDescriptionsCollector(description);
					walker = new TestWalker(NestedDescribeTestClass.class);
				});
				It("should collect all descriptions into a tree structure under the root description", () -> {
					assertThat(description.getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getDisplayName(), is("Test Class"));
					assertThat(description.getChildren().get(0).getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getChildren().get(0).getDisplayName(), is("A context"));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().get(0).getDisplayName(), is("A nested describe"));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().size(), is(1));
					assertThat(description.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getDisplayName(), is("should do something(It)"));
				});
				It("should collect all descriptions into a quick access map", () -> {
					assertThat(collector.getDescriptions(), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class"), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context"), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context.A nested describe"), is(not(nullValue())));
					assertThat(collector.getDescriptions().get("Test Class.A context.A nested describe.should do something"), is(not(nullValue())));
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

	static class TopLevelContextTestClass {{
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

	static class NestedDescribeTestClass {{
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
}
