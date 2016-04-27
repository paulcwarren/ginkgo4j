package impl.org.paulcwarren.ginkgo4j;

import org.junit.runners.model.Statement;
import org.paulcwarren.ginkgo4j.ExecutableBlock;

public class Spec extends Statement {

	private String id;
	
	private Context origin;
	private Context parent;
	
	private String description;
	private ExecutableBlock block;
	
	public Spec(String description, ExecutableBlock block, Context origin, Context parent) {
		this.id = (parent != null) ? parent.getId() + "." + description : description;
		this.description = description;
		this.block = block;
		this.origin = origin;
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

	public Context getOrigin() {
		return origin;
	}

	public Context getParent() {
		return parent;
	}

	@Override
	public void evaluate() throws Throwable {
		this.getExecutableBlock().invoke();
	}

	@Override
	public String toString() {
		return "Spec [" + id + "]";
	}
}
