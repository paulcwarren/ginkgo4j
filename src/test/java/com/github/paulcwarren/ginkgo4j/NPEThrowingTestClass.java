package com.github.paulcwarren.ginkgo4j;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;

public class NPEThrowingTestClass {
	public static String describe = "";
	public static String context = "";
	public static String jbe = "";
	public static String be = "";
	public static String ae = "";
	{
		Describe("a describe", () -> {
			describe.length();
			BeforeEach(() -> {
				be.length();
			});
			JustBeforeEach(() -> {
				jbe.length();
			});
			Context("a context", () -> {
				context.length();
			    It("stuff", () -> {});
			});
			AfterEach(() -> {
				ae.length();
			});
		}); 
	}
}