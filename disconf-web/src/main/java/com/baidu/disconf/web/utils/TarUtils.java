package com.baidu.disconf.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.io.IOUtils;

import com.baidu.dsp.common.constant.DataFormatConstants;
import com.github.knightliao.apollo.utils.io.OsUtil;
import com.github.knightliao.apollo.utils.time.DateUtils;

/**
 * @author liaoqiqi
 * @version 2014-9-15
 */
public class TarUtils {

    /**
     * @param fileList
     *
     * @return
     *
     * @throws IOException
     * @throws CompressorException
     */
    public static String tarFiles(String dir, String fileNamePrefix, List<File> fileList)
        throws IOException, CompressorException {

        //
        OsUtil.makeDirs(dir);

        // 时间
        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);

        // 文件名
        String outputFilePath = fileNamePrefix + "_" + curTime + ".tar.gz";
        File outputFile = new File(dir, outputFilePath);

        FileOutputStream out = null;
        out = new FileOutputStream(outputFile);

        //
        // 进行打包
        //
        TarArchiveOutputStream os = new TarArchiveOutputStream(out);
        for (File file : fileList) {
            os.putArchiveEntry(new TarArchiveEntry(file, file.getName()));
            IOUtils.copy(new FileInputStream(file), os);
            os.closeArchiveEntry();
        }

        if (os != null) {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputFile.getAbsolutePath();
    }
}
