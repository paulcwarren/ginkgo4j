package impl.com.github.paulcwarren.ginkgo4j.watch;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class FileWatcherTests {

	FileWatcher watcher;
	FileWatcherListener listener;
	
	File testdir;
	File testFile;
	{
		Describe("FileWatcher", () -> {
			Context("#watch", () -> {
				BeforeEach(() -> {
					testdir = mktestdir();
					System.out.println(testdir);
				});
				AfterEach(() -> {
					FileUtils.deleteDirectory(testdir);
				});
				Context("when the watcher is watching a test file", () -> {
					BeforeEach(() -> {
						testFile = mock(File.class);
						listener = mock(FileWatcherListener.class);
					});
					JustBeforeEach(() -> {
						new Thread(() -> {
							FileWatcher.watch(Collections.singletonList(testFile), listener);
						}).start();
					});
					Context("when that test file is modified", () -> {
						BeforeEach(() -> {
							when(testFile.lastModified())
							.thenReturn(10L)
							.thenReturn(20L);
						});
						It("should inform the listener", () -> {
							await().atMost(1, SECONDS).until(() -> verify(listener).testChanged());
						});
					});
					Context("when that test file is not modified", () -> {
						It("should inform the listener", () -> {
							await().atMost(1, SECONDS).until(() -> verifyNoMoreInteractions(listener));
						});
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
