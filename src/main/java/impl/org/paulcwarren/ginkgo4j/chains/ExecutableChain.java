package impl.org.paulcwarren.ginkgo4j.chains;

import java.util.ArrayList;
import java.util.List;

import org.paulcwarren.ginkgo4j.ExecutableBlock;

public class ExecutableChain {
	
	private String id;
	private Object testObject;
	private boolean isFocused;
	
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

	public List<ExecutableBlock> getBeforeEachs() {
		return beforeEachs;
	}
	
	public List<ExecutableBlock> getJustBeforeEachs() {
		return justBeforeEachs;
	}
	
	public ExecutableBlock getSpec() {
		return specBlock;
	}
	
	public void setSpec(ExecutableBlock spec) {
		this.specBlock = spec;
	}
	
	public List<ExecutableBlock> getAfterEachs() {
		return afterEachs;
	}
	
	public boolean isFocused() {
		return isFocused;
	}
	
	public void setIsFocused(boolean isFocused) {
		this.isFocused = isFocused;
	}
	
	public void execute() throws Exception {

		for (ExecutableBlock block : this.getBeforeEachs()) {
			block.invoke();
		}
	
		for (ExecutableBlock block : this.getJustBeforeEachs()) {
			block.invoke();
		}
	
		this.getSpec().invoke();

		for (ExecutableBlock block : this.getAfterEachs()) {
			block.invoke();
    	}
	}
}
