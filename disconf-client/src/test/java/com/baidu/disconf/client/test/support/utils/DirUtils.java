package com.baidu.disconf.client.test.support.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class DirUtils {

    public static File createTempDirectory() throws IOException {

        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }
}
