package impl.com.github.paulcwarren.ginkgo4j.watch;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;

public final class TestScanner {

	private TestScanner() {}

	public static List<File> scan(File directory, boolean recursive) {
		FileFilter fileFilter = new WildcardFileFilter(new String[] {"*Test.java", "*Tests.java"});
		File[] files = directory.listFiles(fileFilter);
		List<File> tests = new ArrayList<>();
		for (File f : files) {
			if (f.isFile()) {
				tests.add(f);
			}
		}
		return tests;
	}
}
