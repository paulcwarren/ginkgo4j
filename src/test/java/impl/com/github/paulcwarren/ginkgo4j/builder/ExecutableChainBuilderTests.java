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

import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChainBuilder;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class ExecutableChainBuilderTests {
	
	private ExecutableChainBuilder bldr;
	{
		Describe("ExecutableChainBuilder Tests", () -> {
			
			Context("when a describe block is invoked", () -> {

				BeforeEach(() -> {
					bldr = new ExecutableChainBuilder("describe.does something");
					Ginkgo4jDSL.setVisitor(bldr);
					Ginkgo4jDSL.Describe("describe", () -> {
						BeforeEach(() -> {});
						JustBeforeEach(() -> {});
						It("does something", () -> {});
						AfterEach(() -> {});
					});
					Ginkgo4jDSL.unsetVisitor(bldr);
				});

				It("should capture the test", () -> {
					assertThat(bldr.getExecutableChain().getBeforeEachs().size(), is(1));
					assertThat(bldr.getExecutableChain().getJustBeforeEachs().size(), is(1));
					assertThat(bldr.getExecutableChain().getSpec(), is(not(nullValue())));
					assertThat(bldr.getExecutableChain().getAfterEachs().size(), is(1));
				});
			});

			Context("when a describe block not matching the 'it' filter is invoked", () -> {

				BeforeEach(() -> {
					bldr = new ExecutableChainBuilder("describe.does something");
					Ginkgo4jDSL.setVisitor(bldr);
					Ginkgo4jDSL.Describe("not describe", () -> {
						BeforeEach(() -> {});
						It("does something", () -> {});
						AfterEach(() -> {});
					});
					Ginkgo4jDSL.unsetVisitor(bldr);
				});

				It("should ignore the describe", () -> {
					assertThat(bldr.getExecutableChain().getBeforeEachs().size(), is(0));
					assertThat(bldr.getExecutableChain().getSpec(), is(nullValue()));
					assertThat(bldr.getExecutableChain().getAfterEachs().size(), is(0));
				});
			});
		});
	}
}
