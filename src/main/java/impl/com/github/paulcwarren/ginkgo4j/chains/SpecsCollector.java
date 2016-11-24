package impl.com.github.paulcwarren.ginkgo4j.chains;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.Spec;
import impl.com.github.paulcwarren.ginkgo4j.builder.TestVisitor;

public class SpecsCollector implements TestVisitor {
	
	private Stack<String> context = new Stack<>();
	private List<Spec> specs = new ArrayList<>();

	public List<Spec> getSpecs() {
		return specs;
	}
	
	public void describe(String text, ExecutableBlock block, boolean isFocused) {
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
		Spec spec = new Spec(getId(text), block, isFocused);
		specs.add(spec);
	}
	
	public void afterEach(ExecutableBlock block) {
	}

	String getId(String text) {
		StringBuilder builder = new StringBuilder();
		int i;
		for (i=0; i < context.size(); i++) {
			if (i > 0) {
				builder.append(".");
			}
			builder.append(context.elementAt(i));
		}
		if (i > 0) {
			builder.append(".");
		}
		builder.append(text);
		return builder.toString();
	}

	@Override
	public void test(Object test) {
		// TODO Auto-generated method stub
		
	}
}
