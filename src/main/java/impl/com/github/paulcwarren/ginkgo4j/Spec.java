package impl.com.github.paulcwarren.ginkgo4j;

import com.github.paulcwarren.ginkgo4j.ExecutableBlock;

public class Spec {

	private String id;
	
	private boolean isFocused;
	
	private String description;
	private ExecutableBlock block;
	
	public Spec(String description, ExecutableBlock block, boolean isFocused) {
		this.id = description;
		this.description = description;
		this.block = block;
		this.isFocused = isFocused;
	}
	
	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public boolean isFocused() {
		return isFocused;
	}
	
	public ExecutableBlock getExecutableBlock() {
		return block;
	}

	@Override
	public String toString() {
		return "Spec [" + id + "]";
	}
}
