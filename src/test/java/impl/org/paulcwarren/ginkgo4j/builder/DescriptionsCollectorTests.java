package impl.org.paulcwarren.ginkgo4j.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class DescriptionsCollectorTests {
	
	private TestWalker walker;
	private DescriptionsCollector collector;
	private Description description;
	{
		Describe("Descriptions Collector", () -> {
			BeforeEach(() -> {
				description = mock(Description.class);
				collector = new DescriptionsCollector(description);
				walker = new TestWalker(TestClass.class);
				walker.walk(collector);
			});
			It("should collect all descriptions", () -> {
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
