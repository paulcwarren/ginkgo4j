package impl.com.github.paulcwarren.ginkgo4j.builder;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class ExecutableChainBuilderITests {

	private static String str = null;

	{
		Describe("single context with multiple, unordered be's, jbe's and ae's", ()->{
			AtomicReference<String> capture = new AtomicReference<>("");
			AtomicReference<String> aeCapture = new AtomicReference<>("");
			AfterEach(() -> {
				assertThat(aeCapture.get(), is(""));
				aeCapture.set("1");
			});
			AfterEach(() -> {
				assertThat(aeCapture.get(), is("1"));
			});
			It("calls the be's, jbe's and ae's in the right order", () -> {
				assertThat(capture.get(), is("4th"));
			});
			JustBeforeEach(() -> {
				assertThat(capture.get(), is("2nd"));
				capture.set("3rd");
			});
			JustBeforeEach(() -> {
				assertThat(capture.get(), is("3rd"));
				capture.set("4th");
			});
			BeforeEach(() -> {
				assertThat(capture.get(), is(""));
				capture.set("1st");
			});
			BeforeEach(() -> {
				assertThat(capture.get(), is("1st"));
				capture.set("2nd");
			});
		});

		Describe("multiple context with multiple, unordered be's, jbe's and ae's", ()->{
			AtomicReference<String> capture = new AtomicReference<>("");
			AtomicReference<String> aeCapture = new AtomicReference<>("");
			AfterEach(() -> {
				assertThat(aeCapture.get(), is("2"));
				aeCapture.set("3");
			});
			AfterEach(() -> {
				assertThat(aeCapture.get(), is("3"));
			});
			Context("second context", ()->{
				AfterEach(() -> {
					assertThat(aeCapture.get(), is(""));
					aeCapture.set("1");
				});
				AfterEach(() -> {
					assertThat(aeCapture.get(), is("1"));
					aeCapture.set("2");
				});
				It("then sets 2nd capture", () -> {
					assertThat(capture.get(), is("8th"));
				});
				JustBeforeEach(() -> {
					assertThat(capture.get(), is("6th"));
					capture.set("7th");
				});
				JustBeforeEach(() -> {
					assertThat(capture.get(), is("7th"));
					capture.set("8th");
				});
				BeforeEach(() -> {
					assertThat(capture.get(), is("2nd"));
					capture.set("3rd");
				});
				BeforeEach(() -> {
					assertThat(capture.get(), is("3rd"));
					capture.set("4th");
				});
			});
			BeforeEach(() -> {
				assertThat(capture.get(), is(""));
				capture.set("1st");
			});
			BeforeEach(() -> {
				assertThat(capture.get(), is("1st"));
				capture.set("2nd");
			});
			JustBeforeEach(() -> {
				assertThat(capture.get(), is("4th"));
				capture.set("5th");
			});
			JustBeforeEach(() -> {
				assertThat(capture.get(), is("5th"));
				capture.set("6th");
			});
		});
	}
}
