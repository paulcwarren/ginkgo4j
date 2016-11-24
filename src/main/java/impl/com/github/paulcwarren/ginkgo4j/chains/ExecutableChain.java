package impl.com.github.paulcwarren.ginkgo4j.chains;

import java.util.ArrayList;
import java.util.List;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

import impl.com.github.paulcwarren.ginkgo4j.Context;

public class ExecutableChain {
	
	private String id;
	private Object testObject;
	private boolean isFocused;

	private List<Context> context = new ArrayList<>();
	
	public ExecutableChain(String id) {
		this.id = id;
	}
	
	private ExecutableBlock specBlock = null;
	
	public String getId() {
		return this.id;
	}
	
	public Object getTestObject() {
		return testObject;
	}

	public void setTestObject(Object testObject) {
		this.testObject = testObject;
	}
	
	public List<Context> getContext() {
		return this.context;
	}

	public ExecutableBlock getSpec() {
		return specBlock;
	}
	
	public void setSpec(ExecutableBlock spec) {
		this.specBlock = spec;
	}
	
	public boolean isFocused() {
		return isFocused;
	}
	
	public void setIsFocused(boolean isFocused) {
		this.isFocused = isFocused;
	}
	
	public void execute() throws Throwable {

		for (Context c : context) {
			try {
				if (c.getBeforeEach() != null) {
					c.getBeforeEach().invoke();
				}
			} catch (Throwable t) {
				if (c.getAfterEach() != null) {
					c.getAfterEach().invoke();
				}
				throw t;
			}
		};
		
		for (Context c : context) {
			try {
				if (c.getJustBeforeEach() != null) {
					c.getJustBeforeEach().invoke();
				}
			} catch (Throwable t) {
				if (c.getAfterEach() != null) {
					c.getAfterEach().invoke();
				}
				throw t;
			}
		};
		
		try {
			this.getSpec().invoke();
		} finally {
			for (int i = context.size() - 1; i >= 0; i--) {
				ExecutableBlock block = context.get(i).getAfterEach();
				if (block != null) {
					block.invoke();
				}
			}
		}
	}
}
