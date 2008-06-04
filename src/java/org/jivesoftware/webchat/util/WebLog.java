package org.jivesoftware.webchat.util;

import org.jivesoftware.webchat.FastpathServlet;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class WebLog {
    static File errorFile;
    private static Logger LOGGER;

    static {
        // Create an appending file handler
        File webFile = new File(FastpathServlet.BASE_LOCATION, "WEB-INF");

        File logDir = null;
        try {
            logDir = new File(webFile.getParentFile().getParentFile().getParentFile(), "logs");
        }
        catch (Exception e) {

        }
        if (logDir != null && logDir.exists()) {
            errorFile = new File(logDir, "webchat-error.log");
        }
        else {
            errorFile = new File(webFile, "webchat-error.log");
        }


        try {
            // Create an appending file handler
            boolean append = true;
            FileHandler handler = new FileHandler(errorFile.getCanonicalPath(), append);
            handler.setFormatter(new SimpleFormatter());

            // Add to the desired logger
            LOGGER = Logger.getAnonymousLogger();
            LOGGER.addHandler(handler);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public final static void changeLogFile(File file) {
        errorFile = file;
    }

    /**
     * Logs all error messages to default error logger.
     *
     * @param message a message to append to log file.
     * @param ex      the exception being thrown.
     */
    public final static void logError(String message, Exception ex) {
        LOGGER.log(Level.WARNING, message, ex);
    }

    /**
     * Logs all error messages to default error logger.
     *
     * @param message a message to append to log file.
     * @param ex      the exception being thrown.
     */
    public final static void logError(String message, Throwable ex) {
        LOGGER.log(Level.WARNING, message, ex);
    }

    /**
     * Logs all error messages to default error logger.
     *
     * @param message a message to append to log file.
     */
    public final static void logError(String message) {
        LOGGER.log(Level.WARNING, message);
    }

    public final static void log(String message) {
        LOGGER.log(Level.INFO, message);
    }
}
