package org.paulcwarren.ginkgo4j;

import java.util.ArrayList;
import java.util.List;

public class ExecutableChain {
	
	private String id;
	
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
	
}
