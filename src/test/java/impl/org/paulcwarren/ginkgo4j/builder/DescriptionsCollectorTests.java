package impl.org.paulcwarren.ginkgo4j.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;

import java.lang.annotation.Annotation;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import org.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.org.paulcwarren.ginkgo4j.junit.JunitDescriptionsCollector;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class DescriptionsCollectorTests {
	
	private TestWalker walker;
	private JunitDescriptionsCollector collector;
	private Description description;
	{
		Describe("Descriptions Collector", () -> {
			BeforeEach(() -> {
				description = Description.createSuiteDescription(TestClass.class.getName(), (Annotation[])null);
				collector = new JunitDescriptionsCollector(description);
				walker = new TestWalker(TestClass.class);
				walker.walk(collector);
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
}
