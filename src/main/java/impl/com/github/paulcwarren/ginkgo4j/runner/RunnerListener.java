package impl.com.github.paulcwarren.ginkgo4j.runner;

public interface RunnerListener {
	void testStarted(String specId);
	void testException(String specId, Throwable t);
	void testFinished(String specId);
	void testSkipped(String specId);
}
