package org.paulcwarren.ginkgo4j;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;

import impl.org.paulcwarren.ginkgo4j.Spec;

@RunWith(Ginkgo4jRunner.class)
public class Ginkgo4jRunnerTests {

	private Ginkgo4jRunner runner;
	List<Spec> specs;
	{
		Describe("#calculateExecutionChains", () -> {
			Context("when called with at least one focussed spec", () -> {
				BeforeEach(() -> {
					runner = new Ginkgo4jRunner(TestClass.class);
					specs = new ArrayList<>();
					specs.add(new Spec("test1", () -> {}, false));
					specs.add(new Spec("test2", () -> {}, true));
					specs.add(new Spec("test3", () -> {}, false));
				});
				It("should return a chain with just the foccused spec", () -> {
					List<ExecutableChain> chains = runner.calculateExecutionChains(specs);
					assertThat(chains, is(not(nullValue())));
					assertThat(chains.size(), is(1));
					assertThat(chains.get(0).getId(), is("test2"));
				});
			});
		});
	}
	
	public static class TestClass {
	}
}
