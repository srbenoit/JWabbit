package jwabbit.log;

/*
 * This software was derived from the Wabbitemu software, as it existed in October 2015, by Steve Benoit. This software
 * is licensed under the GNU General Public License version 2 (GPLv2). See the disclaimers or warranty and liability
 * included in the terms of that license.
 */

import jwabbit.CoreConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The class responsible for writing the records generated by an {@code ObjLogger} to some configured output.
 *
 * <p>
 * The writer can log to the system console, to an internal {@code List} of log messages, or to an archived file. When
 * writing to an archived file, there is a maximum file size and a maximum number of files. Log messages are always
 * written to the [fileNameBase].log file. When a logged message results in that file exceeding the maximum file size,
 * the logs are rotated, with [fileNameBase].log moving to [fileNameBase]_001.log, and so forth, up to the maximum
 * number of log files.
 */
final class ObjLoggerWriter extends LogEntryList {

    /** Flag indicating log messages should be written to the console. */
    private final boolean logToConsole;

    /** Flag indicating log messages should be written to a set of rotating files. */
    private boolean logToFiles;

    /** The directory in which to write log files. */
    private final File logDir;

    /** The maximum size of a log file. */
    private final long maxFileSize;

    /** The maximum number of files in a rotating set. */
    private final int maxNumFiles;

    /** The filename base for log files (files will be base_###.log, where # is generation). */
    private final String fileNameBase;

    /** The current log file. */
    private final File curFile;

    /**
     * Constructs a new {@code ObjLoggerWriter}.
     */
    ObjLoggerWriter() {

        super();

        this.logToConsole = true;
        this.logToFiles = false;
        this.logDir = new File(System.getProperty("user.home"));
        this.maxFileSize = Long.MAX_VALUE;
        this.maxNumFiles = Integer.MAX_VALUE;
        this.fileNameBase = "blslog";
        this.curFile = null;
    }

    /**
     * Writes the message to the console (without adding a linefeed), if console logging is enabled.
     *
     * @param msg      the message to write
     * @param linefeed {@code true} to include a linefeed
     */
    private void writeConsole(final String msg, final boolean linefeed) {

        synchronized (getSynch()) {
            if (this.logToConsole) {
                if (linefeed) {
                    System.out.println(msg);
                } else {
                    System.out.print(msg);
                }
                System.out.flush();
            }
        }
    }

    /**
     * Writes the message to the log output.
     *
     * @param msg      the message to write
     * @param linefeed {@code true} to include a linefeed; {@code false} to omit
     */
    void writeMessage(final String msg, final boolean linefeed) {

        synchronized (getSynch()) {
            writeConsole(msg, linefeed);
            addToList(msg);

            if (this.logToFiles && this.curFile != null) {

                try {
                    try (final FileOutputStream out = new FileOutputStream(this.curFile, true)) {
                        out.write(msg.getBytes(StandardCharsets.UTF_8));
                        if (linefeed) {
                            out.write(CoreConstants.CRLF.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                } catch (final IOException e1) {
                    // Turn off file logging to prevent infinite loop when logging error
                    this.logToFiles = false;
                    writeMessage("Failed to log to " + this.curFile.getPath() + ":  "
                            + e1.getClass().getSimpleName(), true);
                    this.logToFiles = true;
                }

                if (this.maxFileSize > 0L && this.curFile.length() > this.maxFileSize) {
                    rotateLogs();
                }
            }
        }
    }

    /**
     * Rotates the log files.
     */
    private void rotateLogs() {

        synchronized (getSynch()) {
            if (this.curFile != null) {
                final String error = LogRotator.rotateLogs(this.logDir, this.fileNameBase, this.maxNumFiles,
                        this.curFile);

                if (error != null) {
                    // Turn off file logging to prevent infinite loop when logging error
                    this.logToFiles = false;
                    writeMessage(error, true);
                    this.logToFiles = true;
                }
            }
        }
    }
}
