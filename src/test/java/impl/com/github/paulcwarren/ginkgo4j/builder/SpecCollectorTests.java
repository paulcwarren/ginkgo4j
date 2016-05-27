package impl.com.github.paulcwarren.ginkgo4j.builder;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.FIt;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.com.github.paulcwarren.ginkgo4j.builder.TestWalker;
import impl.com.github.paulcwarren.ginkgo4j.chains.SpecsCollector;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class SpecCollectorTests {
	
	private TestWalker walker;
	private SpecsCollector collector;
	{
		Describe("Spec Collector", () -> {
			BeforeEach(() -> {
				collector = new SpecsCollector();
				walker = new TestWalker(TestClass.class);
			});
			
			JustBeforeEach(() -> {
				walker.walk(collector);
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
}
