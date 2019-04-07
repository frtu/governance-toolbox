package com.github.frtu.schema.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Util to generate data into file.
 *
 * @author frtu
 * @since 0.3.6
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * File name and data
     *
     * @since 1.0.1
     */
    public static class FileData {
        private String filename;
        private byte[] fileContent;

        public FileData(String filename, String fileContent) {
            this(filename, fileContent.getBytes(Charset.forName("UTF-8")));
        }

        public FileData(String filename, byte[] fileContent) {
            this.filename = filename;
            this.fileContent = fileContent;
        }
    }

    /**
     * Write a FileData into folder
     *
     * @param fileData        object representing the name and data
     * @param outputDirectory target folder
     * @since 1.0.1
     */
    public static void writeIntoFile(FileData fileData, File outputDirectory) throws IOException {
        outputDirectory.mkdirs();
        final File outputFile = new File(outputDirectory, fileData.filename);
        IOUtils.write(fileData.fileContent, new FileOutputStream(outputFile));
        LOGGER.info("Dot file generated={}", outputFile.getAbsolutePath());
    }

    public static void writeIntoFile(String fileContent, String filename, File outputDirectory) throws IOException {
        outputDirectory.mkdirs();
        final File outputFile = new File(outputDirectory, filename);
        IOUtils.write(fileContent, new FileOutputStream(outputFile), Charset.forName("UTF-8"));
        LOGGER.info("Dot file generated={}", outputFile.getAbsolutePath());
    }
}
