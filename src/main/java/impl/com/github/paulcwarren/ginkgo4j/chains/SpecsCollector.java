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
	
	public void describe(String text, ExecutableBlock block, boolean isFocused) {
		text = IdBuilder.id(text);
		
		context.push(text);
		try {
			block.invoke();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			context.pop();
		}
	}
	
	public void context(String text, ExecutableBlock block, boolean isFocused) {
		text = IdBuilder.id(text);
		
		context.push(text);
		try {
			block.invoke();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			context.pop();
		}
	}
	
	public void beforeEach(ExecutableBlock block) {
	}

	public void justBeforeEach(ExecutableBlock block) {
	}

	public void it(String text, ExecutableBlock block, boolean isFocused) {
		String fqid = IdBuilder.fqid(text, context);
		
		Spec spec = new Spec(fqid, block, isFocused);
		specs.add(spec);
	}
	
	public void afterEach(ExecutableBlock block) {
	}

	@Override
	public void test(Object test) {
	}
}
