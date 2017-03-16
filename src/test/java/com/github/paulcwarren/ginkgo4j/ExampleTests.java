package com.github.paulcwarren.ginkgo4j;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import junit.framework.Assert;

@Ignore
@SuppressWarnings("deprecation")
@RunWith(Ginkgo4jRunner.class)
//@Ginkgo4jConfiguration(threads=1)
public class ExampleTests {
	
	private  String something;

	private Example example;
	{
		Describe("describe", () -> {
			Context("context", () -> {
				something.length();
				It("a test", () -> {
					assertThat(true, is(true));
				});
			});
		});

        Describe("", () -> {
            Context("", () -> {
                It("", () -> {
                    assertThat(true, is(true));
                });
            });
        });

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

			BeforeEach(() -> {
				example = new Example();
			});

			It("should pass", () -> {
				Thread.sleep(1000);
				Assert.assertTrue(true);
			});

			It("should fail", () -> {
				Thread.sleep(1000);
				throw new Exception("this test threw an unexpected exception");
			});
		});
		
		Context("parent context", () -> {

			BeforeEach(() -> {
				example = new Example();
			});

			Describe("child context", () -> {
				It("it", () -> {
					Assert.assertTrue(true);
				});
			});
		});
	}
}
