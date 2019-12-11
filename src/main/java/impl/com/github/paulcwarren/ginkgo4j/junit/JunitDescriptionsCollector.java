package impl.com.github.paulcwarren.ginkgo4j.junit;

import java.lang.annotation.Annotation;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.runner.Description;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.IdBuilder;
import impl.com.github.paulcwarren.ginkgo4j.TitleBuilder;
import impl.com.github.paulcwarren.ginkgo4j.builder.TestVisitor;

public class JunitDescriptionsCollector implements TestVisitor {
	
	private Map<String, Description> descriptions = new HashMap<>();
	private Stack<Description> descContext = new Stack<>();
	private Stack<String> idContext = new Stack<>();
	private Description description;
	
	public JunitDescriptionsCollector(Description description) {
		this.description = description;
	}

	public Map<String,Description> getDescriptions() {
		return descriptions;
	}
	
	public void describe(String text, ExecutableBlock block, boolean isFocused) {

		text = TitleBuilder.title(text);

		String id = IdBuilder.id(text);
		String fqid = IdBuilder.fqid(text, idContext);
		
		Description desc = Description.createSuiteDescription(text, fqid, new Annotation[]{});
		descriptions.put(fqid, desc);
		if (descContext.isEmpty()) {
			description.addChild(desc);
		} else  {
			safePeek(desc);
		}
		descContext.push(desc);
		idContext.push(id);
		try {
			block.invoke();
		} catch (Throwable e) {}
		finally {
			descContext.pop();
			idContext.pop();
		}
	}
	
	public void context(String text, ExecutableBlock block, boolean isFocused) {

		text = TitleBuilder.title(text);

		String id = IdBuilder.id(text);
		String fqid = IdBuilder.fqid(text, idContext);
	
		Description childDesc = Description.createSuiteDescription(text, fqid, new Annotation[]{});
		descriptions.put(fqid, childDesc);
		if (descContext.isEmpty()) {
			description.addChild(childDesc);
		} else  {
			safePeek(childDesc);
		}
		descContext.push(childDesc);
		idContext.push(id);
		try {
			block.invoke();
		} catch (Throwable e) {}
		finally {
			descContext.pop();
			idContext.pop();
		}
	}

	public void beforeEach(ExecutableBlock block) {
	}

	public void justBeforeEach(ExecutableBlock block) {
	}

	public void it(String text, ExecutableBlock block, boolean isFocused) {

		text = TitleBuilder.title(text);

		String id = IdBuilder.fqid(text, idContext);
	
		Description itDesc = Description.createTestDescription("It", text, id);
		descriptions.put(id, itDesc);
		try {
			safePeek(itDesc);
		} catch (EmptyStackException ese) {}
	}
	
	public void afterEach(ExecutableBlock block) {
	}

	private void safePeek(Description childDesc) {
		try {
			descContext.peek().addChild(childDesc);
		} catch (EmptyStackException ese) {}
	}

	@Override
	public void test(Object test) {
	}
}

