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

import org.junit.runner.RunWith;
import org.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class SpecCollectorTests {
	
	private TestWalker walker;
	private SpecsCollector collector;
	{
		Describe("Spec Collector", () -> {
			BeforeEach(() -> {
				collector = new SpecsCollector();
				walker = new TestWalker(TestClass.class);
				walker.walk(collector);
			});
			It("should collect all specs", () -> {
				assertThat(collector.getSpecs(), is(not(nullValue())));
				assertThat(collector.getSpecs().size(), is(1));
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
