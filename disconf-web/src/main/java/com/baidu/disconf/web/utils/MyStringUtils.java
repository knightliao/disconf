package com.baidu.disconf.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by knightliao on 15/1/26.
 */
public class MyStringUtils {

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private final static int BUFFER_SIZE = 4096;
    private final static String DEFAULT_ENCODING = "UTF-8";

    /**
     * 将InputStream转换成指定编码的String
     */
    private static String inputStreamToString(InputStream in, String encoding) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }
        return new String(outStream.toByteArray(), encoding);
    }

    public static String multipartFileToString(MultipartFile file) throws IOException {
        BOMInputStream bomInputStream = new BOMInputStream(file.getInputStream());
        ByteOrderMark bom = bomInputStream.getBOM();
        String charsetName = bom == null ? DEFAULT_ENCODING : bom.getCharsetName();
        return inputStreamToString(bomInputStream, charsetName);
    }
}