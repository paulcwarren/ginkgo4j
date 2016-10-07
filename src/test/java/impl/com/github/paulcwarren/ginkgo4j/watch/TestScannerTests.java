package impl.com.github.paulcwarren.ginkgo4j.watch;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class TestScannerTests {

	File testdir;
	{
		Describe("TestScanner", () -> {
			Context("#scan", () -> {
				BeforeEach(() -> {
					testdir = mktestdir();
				});
				AfterEach(() -> {
					FileUtils.deleteDirectory(testdir);
				});
				Context("when the directory contains *Test.java tests", () -> {
					BeforeEach(() -> {
						new File(testdir, "MyClassTest.java").createNewFile();
						new File(testdir, "HisClassTest.java").createNewFile();
					});
					It("should return 2 tests", () -> {
						List<File> tests = TestScanner.scan(testdir, false);
						assertThat(tests, is(not(nullValue())));
						assertThat(tests.size(), is(2));
					});
				});
				Context("when the directory contains *Tests.java tests", () -> {
					BeforeEach(() -> {
						new File(testdir, "MyClassTests.java").createNewFile();
						new File(testdir, "HisClassTests.java").createNewFile();
					});
					It("should return 2 tests", () -> {
						List<File> tests = TestScanner.scan(testdir, false);
						assertThat(tests, is(not(nullValue())));
						assertThat(tests.size(), is(2));
					});
					});
				Context("when the directory contains non-test files", () -> {
					BeforeEach(() -> {
						new File(testdir, "MyClass.java").createNewFile();
					});
					It("should return an empty list", () -> {
						List<File> tests = TestScanner.scan(testdir, false);
						assertThat(tests, is(not(nullValue())));
						assertThat(tests.size(), is(0));
					});
				});
				Context("when the directory contains a directory", () -> {
					BeforeEach(() -> {
						new File(testdir, "MyDirectoryTest.java").mkdirs();
					});
					It("should return an empty list", () -> {
						List<File> tests = TestScanner.scan(testdir, false);
						assertThat(tests, is(not(nullValue())));
						assertThat(tests.size(), is(0));
					});
				});
			});
		});
	}
	
	static File mktestdir() {
		File testdir = null;
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = "testdir-" + System.currentTimeMillis() + "-";

		for (int counter = 0; counter < Integer.MAX_VALUE; counter++) {
			testdir = new File(baseDir, baseName + counter);
			if (testdir.mkdir()) {
				return testdir;
			}
		}
		return baseDir;
	}
}
