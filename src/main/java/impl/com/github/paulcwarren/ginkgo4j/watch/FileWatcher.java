package impl.com.github.paulcwarren.ginkgo4j.watch;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileWatcher {

	public static void watch(List<File> tests, FileWatcherListener... listeners) {
		Map<File, Long> timestamps = new HashMap<>();
		
		while (true) {
			tests.forEach((f) -> {
				if (timestamps.containsKey(f) == false) {
					timestamps.put(f, f.lastModified());
				}
				
				if (f.lastModified() > timestamps.get(f)) {
					for (FileWatcherListener l : listeners) {
						l.testChanged();
					}
					timestamps.put(f, f.lastModified());
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
}
