package com.baidu.disconf.web.common.comparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.knightliao.apollo.utils.common.StringUtil;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

/**
 * @author knightliao
 */
public class StringComparator extends CommonComparator {

    private final String original;

    private final String revised;

    public StringComparator(String original, String revised) {
        this.original = original;
        this.revised = revised;
    }

    protected List<Delta> getDeltas() throws IOException {

        final List<String> originalFileLines = StringToLines(original);
        final List<String> revisedFileLines = StringToLines(revised);

        final Patch patch = DiffUtils.diff(originalFileLines, revisedFileLines);

        return patch.getDeltas();
    }

    private List<String> StringToLines(String data) throws IOException {

        if (data == null || data.isEmpty()) {
            return new ArrayList<String>();
        }

        List<String> lines = StringUtil.parseStringToStringList(data, "\\r?\\n");
        if (lines == null) {
            lines = new ArrayList<String>();
        }

        return lines;
    }
}
