package impl.org.paulcwarren.ginkgo4j.builder;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.*;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
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
