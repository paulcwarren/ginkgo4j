package org.paulcwarren.ginkgo4j.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static org.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.paulcwarren.ginkgo4j.Ginkgo4jSpringRunner;
import org.paulcwarren.ginkgo4j.spring.Ginkgo4jSpringApplication.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

@RunWith(Ginkgo4jSpringRunner.class)
@SpringApplicationConfiguration(classes = Ginkgo4jSpringApplication.class)
public class Ginkgo4jSpringApplicationTests {

	@Autowired
	HelloService helloService;
	
	{
		Describe("Spring intergation", () -> {
			It("should be able to use spring beans", () -> {
				assertThat(helloService, is(not(nullValue())));
			});
			Context("hello service", () -> {
				It("should say hello", () -> {
					assertThat(helloService.sayHello("World"), is("Hello World!"));
				});
			});
		});
	}
	
	@Test
	public void contextLoads() {
		assertThat(helloService, is(not(nullValue())));
	}
}
