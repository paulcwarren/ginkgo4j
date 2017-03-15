package impl.com.github.paulcwarren.ginkgo4j;

import java.util.Stack;

public class IdBuilder {

	private static final String EMPTY_ID = "_EMPTY_";
	
	private IdBuilder() {}
	
	
	public static String id(String id) {
		if (id == null || id.trim().length() == 0) {
			id = EMPTY_ID; 
		}
		return id;
	}
	
	public static String fqid(String id, Stack<?> context) {
		if (id == null || id.trim().length() == 0) {
			id = EMPTY_ID; 
		}
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
		builder.append(id);
		return builder.toString();
	}
}
