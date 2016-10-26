package impl.com.github.paulcwarren.ginkgo4j.junit;

import java.lang.annotation.Annotation;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.runner.Description;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.builder.TestVisitor;

public class JunitDescriptionsCollector implements TestVisitor {
	
	private Map<String, Description> descriptions = new HashMap<>();
	private Stack<Description> context = new Stack<>();
	private Description description;
	
	public JunitDescriptionsCollector(Description description) {
		this.description = description;
	}

	public Map<String,Description> getDescriptions() {
		return descriptions;
	}
	
	public void describe(String text, ExecutableBlock block, boolean isFocused) {
		String id = this.getId(text);
		Description desc = Description.createSuiteDescription(text, id, (Annotation[])null);
		descriptions.put(id, desc);
		if (context.isEmpty()) {
			description.addChild(desc);
		} else  {
			safePeek(desc);
		}
		context.push(desc);
		try {
			block.invoke();
		} catch (Exception e) {}
		finally {
			context.pop();
		}
	}
	
	public void context(String text, ExecutableBlock block, boolean isFocused) {
		String id = this.getId(text);
		Description childDesc = Description.createSuiteDescription(text, id, (Annotation[])null);
		descriptions.put(id, childDesc);
		if (context.isEmpty()) {
			description.addChild(childDesc);
		} else  {
			safePeek(childDesc);
		}
		context.push(childDesc);
		try {
			block.invoke();
		} catch (Exception e) {}
		finally {
			context.pop();
		}
	}

	public void beforeEach(ExecutableBlock block) {
	}

	public void justBeforeEach(ExecutableBlock block) {
	}

	public void it(String text, ExecutableBlock block, boolean isFocused) {
		String id = this.getId(text);
		Description itDesc = Description.createTestDescription("It", text, id);
		descriptions.put(id, itDesc);
		try {
			safePeek(itDesc);
		} catch (EmptyStackException ese) {}
	}
	
	public void afterEach(ExecutableBlock block) {
	}

	private String getId(String text) {
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

	private void safePeek(Description childDesc) {
		try {
			context.peek().addChild(childDesc);
		} catch (EmptyStackException ese) {}
	}

	@Override
	public void test(Object test) {
		// TODO Auto-generated method stub
		
	}
}

