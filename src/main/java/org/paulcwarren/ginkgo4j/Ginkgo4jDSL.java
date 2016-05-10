package org.paulcwarren.ginkgo4j;

import impl.org.paulcwarren.ginkgo4j.builder.TestVisitor; 

public class Ginkgo4jDSL {

	private static TestVisitor visitor = null;
	
	public static synchronized void setVisitor(TestVisitor b) {
		visitor = b;
	}

	public static synchronized void unsetVisitor(TestVisitor b) {
		if (b != visitor) {
			throw new IllegalStateException("Mismatched set/unset builder calls");
		}
		visitor = null;
	}

	public static void Describe(String text, ExecutableBlock block) {
		assertVisitor("Describe");
		if (visitor != null) {
			visitor.describe(text, block);
		}
 	}

	public static void Context(String text, ExecutableBlock block) {
		assertVisitor("Context");
		if (visitor != null) {
			visitor.context(text, block, false);
		}
	}

	public static void FContext(String text, ExecutableBlock block) {
		assertVisitor("FContext");
		if (visitor != null) {
			visitor.context(text, block, true);
		}
	}

	public static void BeforeEach(ExecutableBlock block) {
		assertVisitor("BeforeEach");
		if (visitor != null) {
			visitor.beforeEach(block);
		}
	}

	public static void JustBeforeEach(ExecutableBlock block) {
		assertVisitor("JustBeforeEach");
		if (visitor != null) {
			visitor.justBeforeEach(block);
		}
	}

	public static void It(String text, ExecutableBlock block) {
		assertVisitor("It");
		if (visitor != null) {
			visitor.it(text, block, false);
		}
	}

	public static void FIt(String text, ExecutableBlock block) {
		assertVisitor("FIt");
		if (visitor != null) {
			visitor.it(text, block, true);
		}
	}

	public static void AfterEach(ExecutableBlock block) {
		assertVisitor("AfterEach");
		if (visitor != null) {
			visitor.afterEach(block);
		}
	}

	private static void assertVisitor(String string) {
		if (visitor == null) {
			throw new IllegalStateException(string);
		}
	}
}
