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

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import impl.com.github.paulcwarren.ginkgo4j.chains.ExecutableChainBuilder;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class ExecutableChainBuilderTests {
	
	private ExecutableChainBuilder bldr;
	private It it1 = new It();
	private It it2 = new It();
	
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
					assertThat(bldr.getExecutableChain().getContext().size(), is(1));
					assertThat(bldr.getExecutableChain().getContext().get(0).getBeforeEach(), is(not(nullValue())));
					assertThat(bldr.getExecutableChain().getContext().get(0).getJustBeforeEach(), is(not(nullValue())));
					assertThat(bldr.getExecutableChain().getSpec(), is(not(nullValue())));
					assertThat(bldr.getExecutableChain().getContext().get(0).getAfterEach(), is(not(nullValue())));
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
					assertThat(bldr.getExecutableChain().getContext().size(), is(0));
					assertThat(bldr.getExecutableChain().getSpec(), is(nullValue()));
				});
			});

			Context("when a describe block has two similar Describes", () -> {

				BeforeEach(() -> {
					bldr = new ExecutableChainBuilder("describe something similar.context.it");
					Ginkgo4jDSL.setVisitor(bldr);
					Ginkgo4jDSL.Describe("describe", () -> {
						Context("context", () -> {
							It("it", it1);
						});
					});
					Ginkgo4jDSL.Describe("describe something similar", () -> {
						Context("context", () -> {
							It("it", it2);
						});
					});
					Ginkgo4jDSL.unsetVisitor(bldr);
				});

				It("should capture the correct It", () -> {
					assertThat(bldr.getExecutableChain().getSpec(), is(it2));
				});
			});

			Context("when a describe block has two similar Contexts", () -> {

				BeforeEach(() -> {
					bldr = new ExecutableChainBuilder("describe.does something similar.it");
					Ginkgo4jDSL.setVisitor(bldr);
					Ginkgo4jDSL.Describe("describe", () -> {
						Context("does something", () -> {
							It("it", it1);
						});
						Context("does something similar", () -> {
							It("it", it2);
						});
					});
					Ginkgo4jDSL.unsetVisitor(bldr);
				});

				It("should capture the correct It", () -> {
					assertThat(bldr.getExecutableChain().getSpec(), is(it2));
				});
			});

			Context("when a describe block has two similar Its", () -> {

				BeforeEach(() -> {
					bldr = new ExecutableChainBuilder("describe.does something similar");
					Ginkgo4jDSL.setVisitor(bldr);
					Ginkgo4jDSL.Describe("describe", () -> {
						It("does something", it1);
						It("does something similar", it2);
					});
					Ginkgo4jDSL.unsetVisitor(bldr);
				});

				It("should capture the correct It", () -> {
					assertThat(bldr.getExecutableChain().getSpec(), is(it2));
				});
			});

			Context("when block descriptions contain regex special characters", () -> {

				Context("when the describe contains regex special characters", ()-> {
					BeforeEach(() -> {
						bldr = new ExecutableChainBuilder("/{special}/{characters}/.context.it");
						Ginkgo4jDSL.setVisitor(bldr);
						Ginkgo4jDSL.Describe("/{special}/{characters}/", () -> {
							Context("context", () -> {
								It("it", it1);
							});
						});
						Ginkgo4jDSL.unsetVisitor(bldr);
					});

					It("should capture the It", () -> {
						assertThat(bldr.getExecutableChain().getSpec(), is(it1));
					});
				});
				
				Context("when the context contains regex special characters", ()-> {
					BeforeEach(() -> {
						bldr = new ExecutableChainBuilder("describe./{special}/{characters}/.it");
						Ginkgo4jDSL.setVisitor(bldr);
						Ginkgo4jDSL.Describe("describe", () -> {
							Context("/{special}/{characters}/", () -> {
								It("it", it1);
							});
						});
						Ginkgo4jDSL.unsetVisitor(bldr);
					});

					It("should capture the It", () -> {
						assertThat(bldr.getExecutableChain().getSpec(), is(it1));
					});
				});
				
				Context("when the it contains regex special characters", ()-> {
					BeforeEach(() -> {
						bldr = new ExecutableChainBuilder("describe.context./{special}/{characters}/");
						Ginkgo4jDSL.setVisitor(bldr);
						Ginkgo4jDSL.Describe("describe", () -> {
							Context("context", () -> {
								It("/{special}/{characters}/", it1);
							});
						});
						Ginkgo4jDSL.unsetVisitor(bldr);
					});

					It("should capture the It", () -> {
						assertThat(bldr.getExecutableChain().getSpec(), is(it1));
					});
				});
			});
		});
	}
	
	public class It implements ExecutableBlock {
		@Override public void invoke() throws Exception {}
	}
}
