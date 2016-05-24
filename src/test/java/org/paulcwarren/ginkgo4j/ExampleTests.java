package org.paulcwarren.ginkgo4j;

import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore
@RunWith(Ginkgo4jRunner.class)
public class ExampleTests {
	
	private Example example;

	{
		Describe("A describe", () -> {
			
			BeforeEach(() -> {
				example = new Example();
				
				example.setStatus("describe");
			});

			AfterEach(() -> {
				Assert.assertNull(example);
			});
			
			It("should pass", () -> {
				Thread.sleep(1000);
				Assert.assertSame("describe", example.getStatus());
				example = null;
			});
			
			Context("a context", () -> {

				BeforeEach(() -> {
					example.setStatus("context");
				});
				
				AfterEach(() -> {
					example = null;
				});
				
				It("should also pass", () -> {
					Thread.sleep(1000);
					Assert.assertSame("context", example.getStatus());
				});

			});
			
			Context("a second context", () -> {

				BeforeEach(() -> {
					example.setStatus("2nd context");
				});
				
				AfterEach(() -> {
					example = null;
				});
				
				It("should fail", () -> {
					Thread.sleep(1000);
					Assert.assertSame("not this", example.getStatus());
				});

			});
		});
		
		Describe("A 2nd describe", () -> {
			
			It("should pass", () -> {
				Thread.sleep(1000);
				Assert.assertTrue(true);
			});

			It("should fail", () -> {
				Thread.sleep(1000);
				throw new Exception("this test threw an unexpected exception");
			});
		});
	}

}
