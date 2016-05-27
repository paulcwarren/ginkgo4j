package com.github.paulcwarren.ginkgo4j.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class Ginkgo4jSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ginkgo4jSpringApplication.class, args);
	}
	
	public interface HelloService {
		String sayHello(String to);
	}
	
	@Service
	public static class HelloServiceImpl implements HelloService {
		@Override
		public String sayHello(String to) {
			return "Hello " + to + "!";
		}
	}
}
