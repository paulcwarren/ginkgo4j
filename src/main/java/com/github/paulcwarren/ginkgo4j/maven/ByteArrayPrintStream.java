package com.github.paulcwarren.ginkgo4j.maven;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ByteArrayPrintStream extends PrintStream {

    private ByteArrayOutputStream out;

    public ByteArrayPrintStream(ByteArrayOutputStream out) {
        super(out);
        this.out = out;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return this.out;
    }
}
