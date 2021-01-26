package com.epam.bookshop.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Converts image to base64 format
 */
public class ImgToBase64Converter {
    private static final Logger logger = LoggerFactory.getLogger(ImgToBase64Converter.class);

    private static ImgToBase64Converter instance;
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * Thread safe singleton
     *
     * @return {@link ImgToBase64Converter} instance
     */
    public static ImgToBase64Converter getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new ImgToBase64Converter();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }


    /**
     * @param is {@link InputStream} instance to convert
     * @return converted string
     */
    public String convert(InputStream is) {
        byte[] imageBytes = convertImageToBytes(is);
        return Base64.getEncoder().encodeToString(imageBytes);
    }


    /**
     * @param is {@link InputStream} instance to convert
     * @return converted to byte array img
     */
    private byte[] convertImageToBytes(InputStream is) {

        byte[] imageBytes = null;
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while (true) {
                try {
                    if (!((bytesRead = is.read(buffer)) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                outputStream.write(buffer, 0, bytesRead);
            }

            imageBytes = outputStream.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return imageBytes;
    }
}
