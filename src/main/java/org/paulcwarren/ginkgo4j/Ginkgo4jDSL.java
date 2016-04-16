package org.paulcwarren.ginkgo4j;

import impl.org.paulcwarren.ginkgo4j.builder.SpecVisitor; 

public class Ginkgo4jDSL {

	private static SpecVisitor visitor = null;
	
	public static synchronized void setVisitor(SpecVisitor b) {
		visitor = b;
	}

	public static synchronized void unsetVisitor(SpecVisitor b) {
		if (b != visitor) {
			throw new IllegalStateException("Mismatched set/unset builder calls");
		}
		visitor = null;
	}

	public static void Describe(String text, ExecutableBlock block) {
//		failIfNoSuiteBuilderAvailable("describe");
		if (visitor != null) {
			visitor.describe(text, block);
		}
 	}

	public static void Context(String text, ExecutableBlock block) {
//		failIfNoSuiteBuilderAvailable("describe");
		if (visitor != null) {
			visitor.context(text, block);
		}
	}

	public static void BeforeEach(ExecutableBlock block) {
//		failIfNoSuiteBuilderAvailable("describe");
		if (visitor != null) {
			visitor.beforeEach(block);
		}
	}

	public static void JustBeforeEach(ExecutableBlock block) {
//		failIfNoSuiteBuilderAvailable("describe");
		if (visitor != null) {
			visitor.justBeforeEach(block);
		}
	}

	public static void It(String text, ExecutableBlock block) {
//		failIfNoSuiteBuilderAvailable("it");
		if (visitor != null) {
			visitor.it(text, block);
		}
	}

	public static void AfterEach(ExecutableBlock block) {
//		failIfNoSuiteBuilderAvailable("describe");
		//afterEachs.put(text, block);
		if (visitor != null) {
			visitor.afterEach(block);
		}
	}
}
