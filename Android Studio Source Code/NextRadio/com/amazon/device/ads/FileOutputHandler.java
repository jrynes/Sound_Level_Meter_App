package com.amazon.device.ads;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

class FileOutputHandler extends FileHandler {
    private static final String LOGTAG;
    private BufferedWriter bufferedWriter;
    private final MobileAdsLogger logger;
    private OutputStream outputStream;
    private WriteMethod writeMethod;

    public enum WriteMethod {
        APPEND,
        OVERWRITE
    }

    FileOutputHandler() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    static {
        LOGTAG = FileOutputHandler.class.getSimpleName();
    }

    public boolean isOpen() {
        return this.outputStream != null;
    }

    public boolean open(WriteMethod writeMethod) {
        if (this.file == null) {
            this.logger.m639e("A file must be set before it can be opened.");
            return false;
        } else if (this.outputStream != null) {
            this.logger.m639e("The file is already open.");
            return false;
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(this.file, WriteMethod.APPEND.equals(writeMethod));
                this.writeMethod = writeMethod;
                this.outputStream = new BufferedOutputStream(fos);
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.outputStream));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public void close() {
        flush();
        closeCloseables();
        this.bufferedWriter = null;
        this.outputStream = null;
    }

    public void flush() {
        if (this.outputStream != null) {
            try {
                this.outputStream.flush();
            } catch (IOException e) {
                this.logger.m640e("Could not flush the OutputStream. %s", e.getMessage());
            }
        }
        if (this.bufferedWriter != null) {
            try {
                this.bufferedWriter.flush();
            } catch (IOException e2) {
                this.logger.m640e("Could not flush the BufferedWriter. %s", e2.getMessage());
            }
        }
    }

    protected Closeable getCloseableStream() {
        return this.outputStream;
    }

    protected Closeable getCloseableReaderWriter() {
        return this.bufferedWriter;
    }

    private void checkWritable() {
        if (this.bufferedWriter == null) {
            throw new IllegalStateException("Could not write to the file because no file has been opened yet. Please set the file, then call open() before attempting to write.");
        }
    }

    public void write(String str) throws IOException {
        checkWritable();
        this.bufferedWriter.write(str);
    }

    public void write(byte[] buffer) throws IOException {
        checkWritable();
        this.outputStream.write(buffer);
    }

    public boolean overwriteFile(String data) {
        return writeToFile(data, WriteMethod.OVERWRITE);
    }

    public boolean appendToFile(String data) {
        return writeToFile(data, WriteMethod.APPEND);
    }

    private boolean writeToFile(String data, WriteMethod writeMethod) {
        if (isOpen()) {
            if (!writeMethod.equals(this.writeMethod)) {
                close();
                if (!open(writeMethod)) {
                    this.logger.m640e("Could not reopen the file for %s.", writeMethod.toString());
                    return false;
                }
            }
        } else if (!open(writeMethod)) {
            this.logger.m639e("Could not open the file for writing.");
            return false;
        }
        try {
            write(data);
            close();
            return true;
        } catch (IOException e) {
            this.logger.m639e("Failed to write data to the file.");
            return false;
        }
    }
}
