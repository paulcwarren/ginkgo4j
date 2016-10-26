package impl.com.github.paulcwarren.ginkgo4j;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

public class Context {
	
	private String id;
	
	private ExecutableBlock be;
	private ExecutableBlock jbe;
	private ExecutableBlock a;
	
	public Context(String id) {
		this.id = id;
	}
	
	public Object getId() {
		return this.id;
	}
	
	public void setBeforeEach(ExecutableBlock be) {
		this.be = be;
	}

	public ExecutableBlock getBeforeEach() {
		return this.be;
	}

	public void setJustBeforeEach(ExecutableBlock jbe) {
		this.jbe = jbe;
	}

	public ExecutableBlock getJustBeforeEach() {
		return this.jbe;
	}

	public void setAfterEach(ExecutableBlock a) {
		this.a = a;
	}

	public ExecutableBlock getAfterEach() {
		return this.a;
	}
}

