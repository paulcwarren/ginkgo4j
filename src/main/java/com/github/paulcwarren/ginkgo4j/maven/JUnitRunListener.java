package com.github.paulcwarren.ginkgo4j.maven;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import static java.lang.String.format;

public class JUnitRunListener extends RunListener {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private PrintStream console;

    private ThreadLocal<Boolean> testErrored = new ThreadLocal<>();

    /**
     * Called before any tests have been run.
     * */
    @Override
    public void testRunStarted(Description description) {
        console = System.out;
        ThreadPrintStream.replaceSystemOut();
    }

    /**
     *  Called when all tests have finished
     * */
    @Override
    public void testRunFinished(Result result) {
    }

    /**
     *  Called when an atomic test is about to be started.
     * */
    @Override
    public void testStarted(Description description) {
        testErrored.set(false);
        ByteArrayPrintStream stream = new ByteArrayPrintStream(new ByteArrayOutputStream());
        ((ThreadPrintStream)System.out).setThreadOut(stream);
    }

    /**
     *  Called when an atomic test has finished, whether the test succeeds or fails.
     * */
    @Override
    public void testFinished(Description description) {
        if (isTest(description) && testErrored.get() == false) {

            PrintStream printWriter = null;
            try {
                printWriter = new PrintStream(console, true, Charset.forName("UTF-8").name());
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            char bullet = '\u2022';
            printWriter.print(ANSI_GREEN);
            printWriter.print(bullet);
            printWriter.print(ANSI_RESET);
            printWriter.flush();
        }
    }

    /**
     *  Called when an atomic test fails.
     * */
    @Override
    public void testFailure(Failure failure) {
        testErrored.set(true);

        console.println();
        console.print(ANSI_RED);
        console.println(getTestName(failure.getDescription()));
        failure.getException().printStackTrace(console);
        ByteArrayPrintStream outputStream = (ByteArrayPrintStream) ((ThreadPrintStream)System.out).getThreadOut();
        console.println(new String(outputStream.getByteArrayOutputStream().toByteArray()));
        console.print(ANSI_RESET);

        console.flush();
    }

    @Override
    public void testAssumptionFailure(Failure failure) {

        testErrored.set(true);

        console.println();
        console.print(ANSI_RED);
        console.println(getTestName(failure.getDescription()));
        failure.getException().printStackTrace(console);
        ByteArrayPrintStream outputStream = (ByteArrayPrintStream) ((ThreadPrintStream)System.out).getThreadOut();
        console.println(new String(outputStream.getByteArrayOutputStream().toByteArray()));
        console.print(ANSI_RESET);

        console.flush();
    }

    /**
     *  Called when a test will not be run, generally because a test method is annotated with Ignore.
     * */
    @Override
    public void testIgnored(Description description) {
        console.print(ANSI_BLUE);
        console.print("S");
        console.print(ANSI_RESET);
        console.flush();
    }

    private boolean isTest(Description description) {
        return description.getMethodName() != null;
    }

    private String getTestName(Description description) {
        Field privateStringField = null;
        try {
            privateStringField = Description.class.getDeclaredField("fUniqueId");
            privateStringField.setAccessible(true);
            String fieldValue = (String) privateStringField.get(description);
            fieldValue = fieldValue.replaceAll("\\.", " ");
            return fieldValue;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown test";
    }
}
