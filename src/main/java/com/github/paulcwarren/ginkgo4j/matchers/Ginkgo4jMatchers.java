package com.github.paulcwarren.ginkgo4j.matchers;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Ginkgo4jMatchers {

    private Ginkgo4jMatchers() {}

    public static <T> void eventually(Supplier<T> subjectSupplier, Consumer<T> expectations)
        throws InterruptedException {

        eventually(subjectSupplier, expectations, Duration.ofMillis(1000), Duration.ofSeconds(10));
    }

    public static <T> void eventually(Supplier<T> subjectSupplier, Consumer<T> expectations, Duration interval, Duration timeout)
            throws InterruptedException {

        Instant start = Instant.now();
        T result = null;
        do {

            result = subjectSupplier.get();
            Thread.sleep(interval.toMillis());

            if (result != null) {
                try {
                    expectations.accept(result);
                    return;
                } catch (Throwable t) {}
            }
        } while (Duration.between(start, Instant.now()).compareTo(timeout) < 0);

        throw new AssertionError();
    }
}
