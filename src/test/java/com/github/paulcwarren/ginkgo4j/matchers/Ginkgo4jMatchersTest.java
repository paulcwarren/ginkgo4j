package com.github.paulcwarren.ginkgo4j.matchers;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.matchers.Ginkgo4jMatchers.eventually;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.Duration;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import org.junit.runner.RunWith;

@RunWith(Ginkgo4jRunner.class)
public class Ginkgo4jMatchersTest {
    
    {
        Describe("#eventually", () -> {

            Context("when the subject returns a passing result", () -> {

                It("should be successful", () -> {
                    eventually(
                        () -> {
                            return true;
                        },
                        (result) -> {
                            assertThat(result, is(true));
                        }
                    );
                });
            });
            Context("when the duration expires", () -> {

                It("should throw an AssertionError", () ->{

                    try {
                        eventually(
                            () -> {
                                return null;
                            },
                            (result) -> {
                                fail("shouldn't call this");
                            },
                            Duration.ofMillis(100), Duration.ofSeconds(1));
                            fail("shouldn't call this");
                    } catch (Throwable e) {
                        assertThat(e, is(instanceOf(AssertionError.class)));
                    }
                });
            });
        });
    }
}
