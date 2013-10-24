package com.fenwell.jwell.mvc.spi.logger;

import java.io.PrintStream;

import com.fenwell.jwell.mvc.api.Logger;

public class SimpleLogger implements Logger {

    private static final String returnLine = "\n";

    private String name;

    public SimpleLogger(String name) {
        this.name = name;
    }

    private void print(String msg, Throwable t, Object... objs) {
        print(msg, t, System.out, objs);
    }

    private void printError(String msg, Throwable t, Object... objs) {
        print(msg, t, System.err, objs);
    }

    private void print(String msg, Throwable t, PrintStream out, Object... objs) {
        if (objs != null) {
            if (msg != null) {
                msg = name + msg;
            }
            msg += returnLine;
            printf(msg, out, objs);
        } else {
            if (msg != null) {
                msg = name + msg;
                out.println(msg);
            }
        }
        if (t != null) {
            t.printStackTrace(out);
            return;
        }
    }

    private void printf(String msg, PrintStream out, Object... objs) {
        out.printf(msg, objs);
    }

    public void info(String msg) {
        print(msg, null);
    }

    public void info(Throwable t) {
        print(null, t);
    }

    public void info(String msg, Throwable t) {
        print(msg, t);
    }

    public void infof(String msg, Object... obj) {
        print(msg, null, obj);
    }

    public void warn(String msg) {
        print(msg, null);
    }

    public void warn(Throwable t) {
        print(null, t);
    }

    public void warn(String msg, Throwable t) {
        print(msg, t);
    }

    public void warnf(String msg, Object... obj) {
        print(msg, null, obj);
    }

    public void error(String msg) {
        printError(msg, null);
    }

    public void error(Throwable t) {
        printError(null, t);
    }

    public void error(String msg, Throwable t) {
        printError(msg, t);
    }

    public void errorf(String msg, Object... obj) {
        printError(msg, null, obj);
    }

    public void debug(String msg) {
        print(msg, null);
    }

    public void debug(Throwable t) {
        print(null, t);
    }

    public void debug(String msg, Throwable t) {
        print(msg, t);
    }

    public void debugf(String msg, Object... obj) {
        print(msg, null, obj);
    }

}
