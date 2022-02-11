package com.amazon.device.ads;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class FileInputHandler extends FileHandler {
    private static final String LOGTAG;
    private BufferedReader bufferedReader;
    private InputStream inputStream;
    private final MobileAdsLogger logger;

    FileInputHandler() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    static {
        LOGTAG = FileInputHandler.class.getSimpleName();
    }

    public boolean isOpen() {
        return this.inputStream != null;
    }

    public boolean open() {
        if (this.file == null) {
            this.logger.m639e("A file must be set before it can be opened.");
            return false;
        } else if (this.inputStream != null) {
            this.logger.m639e("The file is already open.");
            return false;
        } else {
            try {
                this.inputStream = new BufferedInputStream(new FileInputStream(this.file));
                this.bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public void close() {
        closeCloseables();
        this.bufferedReader = null;
        this.inputStream = null;
    }

    protected Closeable getCloseableStream() {
        return this.inputStream;
    }

    protected Closeable getCloseableReaderWriter() {
        return this.bufferedReader;
    }

    private void checkReadable() {
        if (this.bufferedReader == null) {
            throw new IllegalStateException("Could not read from the file because no file has been opened yet. Please set the file, then call open() before attempting to read.");
        }
    }

    public byte[] readBytes() {
        checkReadable();
        int totalBytesRead = 0;
        try {
            byte[] bArr = new byte[((int) this.file.length())];
            while (totalBytesRead < bArr.length) {
                int bytesRead = this.inputStream.read(bArr, totalBytesRead, bArr.length - totalBytesRead);
                if (bytesRead > 0) {
                    totalBytesRead += bytesRead;
                }
            }
            return bArr;
        } catch (IOException e) {
            this.logger.m640e("Error reading bytes from input file: %s", e.getMessage());
            return null;
        }
    }

    public byte[] readAllBytesFromFileAndClose() {
        if (isOpen() || open()) {
            byte[] data = readBytes();
            close();
            return data;
        }
        this.logger.m639e("Could not open the file for reading");
        return null;
    }

    public String readLine() {
        checkReadable();
        try {
            return this.bufferedReader.readLine();
        } catch (IOException e) {
            this.logger.m639e("Error reading line from file.");
            return null;
        }
    }
}
