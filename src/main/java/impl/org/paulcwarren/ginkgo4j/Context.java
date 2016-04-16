package impl.org.paulcwarren.ginkgo4j;

import java.util.ArrayList;
import java.util.List;

import org.paulcwarren.ginkgo4j.ExecutableBlock;

public class Context {

	private String id;
	
	private String description;
	private ExecutableBlock block;
	
	private Context parent;
	
	private ExecutableBlock beforeEach;
	private ExecutableBlock justBeforeEach;
	private List<Context> children = new ArrayList<>();
	private List<Spec> specs = new ArrayList<>();
	private ExecutableBlock afterEach;
	
	public Context(String description, ExecutableBlock block, Context parent) {
		this.id = (parent != null) ? parent.getId() + "." + description : description;
		this.description = description;
		this.block = block;
		this.parent = parent;
	}
	
	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public ExecutableBlock getExecutableBlock() {
		return block;
	}

	public Context getParent() {
		return parent;
	}

	public ExecutableBlock getBeforeEach() {
		return beforeEach;
	}
	
	public ExecutableBlock getJustBeforeEach() {
		return justBeforeEach;
	}
	
	public void setBeforeEach(ExecutableBlock beforeEach) {
		this.beforeEach = beforeEach;
	}
	
	public void setJustBeforeEach(ExecutableBlock justBeforeEach) {
		this.justBeforeEach = justBeforeEach;
	}
	
	public List<Context> getChildren() {
		return children;
	}
	
	public List<Spec> getSpecs() {
		return specs;
	}
	
	public ExecutableBlock getAfterEach() {
		return afterEach;
	}
	
	public void setAfterEach(ExecutableBlock afterEach) {
		this.afterEach = afterEach;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Context other = (Context) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Context [" + id + "]";
	}
	
}
