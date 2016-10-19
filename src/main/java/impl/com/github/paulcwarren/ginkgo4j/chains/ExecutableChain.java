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
	
	private List<ExecutableBlock> beforeEachs = new ArrayList<>();
	private List<ExecutableBlock> justBeforeEachs = new ArrayList<>();
	private ExecutableBlock specBlock = null;
	private List<ExecutableBlock> afterEachs = new ArrayList<>();
	
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
	
	public void execute() throws Exception {

		for (Context c : context) {
			try {
				if (c.getBeforeEach() != null) {
					c.getBeforeEach().invoke();
				}
			} catch (Exception e) {
				if (c.getAfterEach() != null) {
					c.getAfterEach().invoke();
				}
				throw e;
			}
		};
		
		for (Context c : context) {
			try {
				if (c.getJustBeforeEach() != null) {
					c.getJustBeforeEach().invoke();
				}
			} catch (Exception e) {
				if (c.getAfterEach() != null) {
					c.getAfterEach().invoke();
				}
				throw e;
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
