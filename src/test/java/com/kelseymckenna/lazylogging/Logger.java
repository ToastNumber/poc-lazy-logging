package com.kelseymckenna.lazylogging;

import java.util.function.Supplier;

/**
 * Created by Kelsey McKenna on 07/12/16.
 */
public class Logger {
    public static boolean debug;

    public void debug(final String message) {
    }

    public void debug(final Supplier<String> messageSupplier) {

    }

    public static boolean isDebugEnabled() {
        return debug;
    }
}
