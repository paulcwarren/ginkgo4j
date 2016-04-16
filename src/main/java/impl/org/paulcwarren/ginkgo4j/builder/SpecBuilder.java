package impl.org.paulcwarren.ginkgo4j.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.org.paulcwarren.ginkgo4j.Context;
import impl.org.paulcwarren.ginkgo4j.Describe;
import impl.org.paulcwarren.ginkgo4j.Spec;

public class SpecBuilder implements SpecVisitor {
	
	private Stack<Context> stack = new Stack<>();
	private List<Context> contexts = new ArrayList<>();
	private List<Spec> specs = new ArrayList<>();

	public List<Context> getContexts() {
		return contexts;
	}
	
	public List<Spec> getSpecs() {
		return specs;
	}
	
	public void describe(String text, ExecutableBlock block) {
		Context parent = currentContext();
		Context describe = new Describe(text, block, parent);
		if (currentContext() != null) {
			currentContext().getChildren().add(describe);
		}
		contexts.add(describe);

		stack.push(describe);
		try {
			describe.getExecutableBlock().invoke();
		} catch (Exception e) {}
		stack.pop();
	}
	
	public void context(String text, ExecutableBlock block) {
		Context parent = currentContext();
		Context context = new Context(text, block, parent);
		if (currentContext() != null) {
			currentContext().getChildren().add(context);
		}

		stack.push(context);
		try {
			context.getExecutableBlock().invoke();
		} catch (Exception e) {}
		stack.pop();
	}
	
	public void beforeEach(ExecutableBlock block) {
		stack.peek().setBeforeEach(block);
	}

	public void justBeforeEach(ExecutableBlock block) {
		stack.peek().setJustBeforeEach(block);
	}

	public void it(String text, ExecutableBlock block) {
		Spec spec = new Spec(text, block, (!stack.empty()) ? stack.firstElement() : null, currentContext());
		if (currentContext() != null) {
			currentContext().getSpecs().add(spec);
		}
		specs.add(spec);
	}
	
	public void afterEach(ExecutableBlock block) {
		stack.peek().setAfterEach(block);
	}

	protected Context currentContext() {
		return (!stack.empty()) ? stack.peek() : null;
	}

}

