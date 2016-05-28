package com.baidu.disconf.web.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.web.common.comparator.StringComparator;

import difflib.Chunk;

/**
 *
 */
public class DiffUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(DiffUtils.class);

    /**
     * 简单的对比工作
     *
     * @param old
     * @param newData
     *
     * @return
     */
    public static String getDiffSimple(String old, String newData) {

        StringComparator stringComparator = new StringComparator(old, newData);
        try {
            return stringComparator.getChangesFromOriginal().toString();
        } catch (IOException e) {
            LOG.warn(e.toString());
            return "";
        }
    }

    /**
     * 专门为disconf打造的对照工具
     *
     * @param old
     * @param newData
     * @param identify
     *
     * @return
     */
    public static String getDiff(String old, String newData, String identify, String htmlClick) {

        StringComparator stringComparator = new StringComparator(old, newData);
        String contentString = StringEscapeUtils.escapeHtml4(identify) + "<br/><br/>" + htmlClick + "<br/> ";

        try {

            if (stringComparator.getChangesFromOriginal().size() == 0 &&
                    stringComparator.getDeletesFromOriginal().size() == 0 &&
                    stringComparator.getInsertsFromOriginal().size() == 0) {

                return "<span style='color:#FF0000'>OK, NO MODIFICATOIN!</span>";

            } else {

                String oldValue = "<br/><br/><br/><span style='color:#FF0000'>Old value:</span><br/>" +
                        StringEscapeUtils.escapeHtml4(old).replaceAll("\n", "<br/>");

                String newValue = "<br/><br/><br/><span style='color:#FF0000'>New value:</span><br/>" +
                        StringEscapeUtils.escapeHtml4(newData).replaceAll("\n", "<br/>");

                String diff = "";
                if (stringComparator.getChangesFromOriginal().size() != 0) {
                    diff = "<span style='color:#FF0000'>Change info: </span><br/>";
                    for (Chunk chunk : stringComparator.getChangesFromOriginal()) {
                        diff += StringEscapeUtils.escapeHtml4(chunk.toString()) + "<br/>";
                    }
                }

                if (stringComparator.getInsertsFromOriginal().size() != 0) {
                    diff += "<br/><span style='color:#FF0000'>Insert info: </span><br/>";
                    for (Chunk chunk : stringComparator.getInsertsFromOriginal()) {
                        diff += StringEscapeUtils.escapeHtml4(chunk.toString()) + "<br/>";
                    }
                }

                if (stringComparator.getDeletesFromOriginal().size() != 0) {
                    diff += "<br/><span style='color:#FF0000'>Delete info: </span><br/>";
                    for (Chunk chunk : stringComparator.getDeletesFromOriginal()) {
                        diff += StringEscapeUtils.escapeHtml4(chunk.toString()) + "<br/>";
                    }
                }

                return contentString + diff + oldValue + newValue;
            }

        } catch (IOException e) {
            LOG.error("compare error", e);

            return "comparator error" + e.toString();
        }
    }

}
