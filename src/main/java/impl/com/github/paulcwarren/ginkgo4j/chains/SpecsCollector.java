package impl.com.github.paulcwarren.ginkgo4j.chains;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.IdBuilder;
import impl.com.github.paulcwarren.ginkgo4j.Spec;
import impl.com.github.paulcwarren.ginkgo4j.builder.TestVisitor;

public class SpecsCollector implements TestVisitor {
	
	private Stack<String> context = new Stack<>();
	private List<Spec> specs = new ArrayList<>();

	public List<Spec> getSpecs() {
		return specs;
	}
	
	public void describe(String text, ExecutableBlock block, boolean isFocused) throws Throwable {
		text = IdBuilder.id(text);
		
		context.push(text);
		try {
			block.invoke();
		} finally {
			context.pop();
		}
	}
	
	public void context(String text, ExecutableBlock block, boolean isFocused) throws Throwable {
		text = IdBuilder.id(text);
		
		context.push(text);
		try {
			block.invoke();
		} finally {
			context.pop();
		}
	}
	
	public void beforeEach(ExecutableBlock block) throws Throwable {
	}

	public void justBeforeEach(ExecutableBlock block) throws Throwable {
	}

	public void it(String text, ExecutableBlock block, boolean isFocused) {
		String fqid = IdBuilder.fqid(text, context);
		
		Spec spec = new Spec(fqid, block, isFocused);
		specs.add(spec);
	}
	
	public void afterEach(ExecutableBlock block) throws Throwable {
	}

	@Override
	public void test(Object test) {
	}
}
