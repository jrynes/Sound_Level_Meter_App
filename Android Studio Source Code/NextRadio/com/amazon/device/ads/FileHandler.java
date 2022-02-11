package com.amazon.device.ads;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

abstract class FileHandler implements Closeable {
    private static final String LOGTAG;
    File file;
    private final MobileAdsLogger logger;

    public abstract void close();

    protected abstract Closeable getCloseableReaderWriter();

    protected abstract Closeable getCloseableStream();

    public abstract boolean isOpen();

    FileHandler() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    static {
        LOGTAG = FileHandler.class.getSimpleName();
    }

    public boolean setFile(File parent, String child) {
        return setFile(new File(parent, child));
    }

    public boolean setFile(String fileName) {
        return setFile(new File(fileName));
    }

    public boolean setFile(File file) {
        if (!isFileSet()) {
            this.file = file;
            return true;
        } else if (file.getAbsolutePath().equals(this.file.getAbsolutePath())) {
            return true;
        } else {
            this.logger.m639e("Another file is already set in this FileOutputHandler. Close the other file before opening a new one.");
            return false;
        }
    }

    public boolean isFileSet() {
        return this.file != null;
    }

    public long getFileLength() {
        if (isFileSet()) {
            return this.file.length();
        }
        throw new IllegalStateException("A file has not been set, yet.");
    }

    public boolean doesFileExist() {
        if (isFileSet()) {
            return this.file.exists();
        }
        throw new IllegalStateException("A file has not been set, yet.");
    }

    protected void closeCloseables() {
        Closeable readerWriter = getCloseableReaderWriter();
        if (readerWriter != null) {
            try {
                readerWriter.close();
                return;
            } catch (IOException e) {
                this.logger.m640e("Could not close the %s. %s", readerWriter.getClass().getSimpleName(), e.getMessage());
                closeStream();
                return;
            }
        }
        closeStream();
    }

    private void closeStream() {
        Closeable stream = getCloseableStream();
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                this.logger.m640e("Could not close the stream. %s", e.getMessage());
            }
        }
    }
}
