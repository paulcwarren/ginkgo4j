package impl.com.github.paulcwarren.ginkgo4j;

public class TitleBuilder {

	private static final String EMPTY_TEXT = " ";
	
	private TitleBuilder() {}
	
	public static String title(String title) {
		if (title == null || title.trim().length() == 0) {
			return EMPTY_TEXT; 
		}
		return title;
	}
}
