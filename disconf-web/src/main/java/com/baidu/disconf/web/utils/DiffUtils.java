package com.baidu.disconf.web.utils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
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
        String contentString = StringEscapeUtils.escapeHtml(identify) + "<br/><br/>" + htmlClick + "<br/> ";

        try {

            final List<Chunk> changesFromOriginal = stringComparator.getChangesFromOriginal();

            if (changesFromOriginal.size() == 0) {

                return "<span style='color:#FF0000'>OK, NO MODIFICATOIN!</span>";

            } else {

                String oldValue = "<br/><br/><br/><span style='color:#FF0000'>Old value:</span><br/>" +
                                      StringEscapeUtils.escapeHtml(old);

                String newValue = "<br/><br/><br/><span style='color:#FF0000'>New value:</span><br/>" +
                                      StringEscapeUtils.escapeHtml(newData);

                String diff = "<span style='color:#FF0000'>Modification info: </span><br/>";
                for (Chunk chunk : changesFromOriginal) {
                    diff += StringEscapeUtils.escapeHtml(chunk.toString()) + "<br/>";
                }

                return contentString + diff + oldValue + newValue;
            }

        } catch (IOException e) {
            LOG.error("compare error", e);

            return "comparator error" + e.toString();
        }
    }
}
