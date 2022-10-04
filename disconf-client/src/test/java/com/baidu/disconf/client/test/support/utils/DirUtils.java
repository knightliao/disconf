package com.baidu.disconf.client.test.support.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class DirUtils {

    public static File createTempDirectory() throws IOException {

        final File temp;

        temp = Files.createTempDirectory("temp" + Long.toString(System.nanoTime())).toFile();

        return (temp);
    }
}
