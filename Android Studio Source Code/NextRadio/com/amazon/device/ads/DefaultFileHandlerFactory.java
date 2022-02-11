package com.amazon.device.ads;

import java.io.File;

/* compiled from: FileHandlerFactory */
class DefaultFileHandlerFactory implements FileHandlerFactory {
    DefaultFileHandlerFactory() {
    }

    public FileInputHandler createFileInputHandler(File parent, String child) {
        FileInputHandler fileInputHandler = new FileInputHandler();
        fileInputHandler.setFile(parent, child);
        return fileInputHandler;
    }

    public FileInputHandler createFileInputHandler(String fileName) {
        FileInputHandler fileInputHandler = new FileInputHandler();
        fileInputHandler.setFile(fileName);
        return fileInputHandler;
    }

    public FileInputHandler createFileInputHandler(File file) {
        FileInputHandler fileInputHandler = new FileInputHandler();
        fileInputHandler.setFile(file);
        return fileInputHandler;
    }

    public FileOutputHandler createFileOutputHandler(File parent, String child) {
        FileOutputHandler fileOutputHandler = new FileOutputHandler();
        fileOutputHandler.setFile(parent, child);
        return fileOutputHandler;
    }

    public FileOutputHandler createFileOutputHandler(String fileName) {
        FileOutputHandler fileOutputHandler = new FileOutputHandler();
        fileOutputHandler.setFile(fileName);
        return fileOutputHandler;
    }

    public FileOutputHandler createFileOutputHandler(File file) {
        FileOutputHandler fileOutputHandler = new FileOutputHandler();
        fileOutputHandler.setFile(file);
        return fileOutputHandler;
    }
}
