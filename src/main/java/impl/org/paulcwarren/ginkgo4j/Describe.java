package impl.org.paulcwarren.ginkgo4j;

import org.paulcwarren.ginkgo4j.ExecutableBlock;

public class Describe extends Context {

	public Describe(String description, ExecutableBlock block, Context parent) {
		super(description, block, parent);
	}

}
