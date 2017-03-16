package com.github.paulcwarren.ginkgo4j;

import java.util.Stack;

import impl.com.github.paulcwarren.ginkgo4j.builder.TestVisitor; 

public class Ginkgo4jDSL {

	private static Stack<TestVisitor> stack = new Stack<>();
	
	public static synchronized void setVisitor(TestVisitor b) {
		stack.push(b);
	}

	public static synchronized void unsetVisitor(TestVisitor b) {
		if (b != stack.peek()) {
			throw new IllegalStateException("Mismatched set/unset builder calls");
		}
		stack.pop();
	}

	public static void Describe(String text, ExecutableBlock block) {
		assertVisitor("Describe");
		if (stack.peek() != null) {
			try {
				stack.peek().describe(text, block, false);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new RuntimeException("Unable to execute test", e);
			}
		}
 	}

	public static void FDescribe(String text, ExecutableBlock block) {
		assertVisitor("FDescribe");
		if (stack.peek() != null) {
			try {
				stack.peek().describe(text, block, true);
			} catch (Throwable e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new RuntimeException("Unable to execute test", e);
			}
		}
 	}

	public static void Context(String text, ExecutableBlock block) {
		assertVisitor("Context");
		if (stack.peek() != null) {
			try {
				stack.peek().context(text, block, false);
			} catch (Throwable e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new RuntimeException("Unable to execute test", e);
			}
		}
	}

	public static void FContext(String text, ExecutableBlock block) {
		assertVisitor("FContext");
		if (stack.peek() != null) {
			try {
				stack.peek().context(text, block, true);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new RuntimeException("Unable to execute test", e);
			}
		}
	}

	public static void BeforeEach(ExecutableBlock block) {
		assertVisitor("BeforeEach");
		if (stack.peek() != null) {
			try {
				stack.peek().beforeEach(block);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new RuntimeException("Unable to execute test", e);
			}
		}
	}

	public static void JustBeforeEach(ExecutableBlock block) {
		assertVisitor("JustBeforeEach");
		if (stack.peek() != null) {
			try {
				stack.peek().justBeforeEach(block);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new RuntimeException("Unable to execute test", e);
			}
		}
	}

	public static void It(String text, ExecutableBlock block) {
		assertVisitor("It");
		if (stack.peek() != null) {
			stack.peek().it(text, block, false);
		}
	}

	public static void FIt(String text, ExecutableBlock block) {
		assertVisitor("FIt");
		if (stack.peek() != null) {
			stack.peek().it(text, block, true);
		}
	}

	public static void AfterEach(ExecutableBlock block) {
		assertVisitor("AfterEach");
		if (stack.peek() != null) {
			try {
				stack.peek().afterEach(block);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				}
				throw new RuntimeException("Unable to execute test", e);
			}
		}
	}

	private static void assertVisitor(String string) {
		if (stack.peek() == null) {
			throw new IllegalStateException(string);
		}
	}
}
