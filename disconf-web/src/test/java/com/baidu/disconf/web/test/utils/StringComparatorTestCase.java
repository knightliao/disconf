package com.baidu.disconf.web.test.utils;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.baidu.disconf.web.common.comparator.StringComparator;

import difflib.Chunk;

/**
 * 
 * @author knightliao
 * 
 */
public class StringComparatorTestCase {

    private final String original = "coe.baiFaCoe=1.3\r\ncoe.yuErBaoCoe=1.2";

    private final String revised = "coe.baiFaCoe=1.2\r\ncoe.yuErBaoCoe=1.2";

    @Test
    public void shouldGetChangesBetweenFiles() {

        final StringComparator comparator = new StringComparator(original, revised);

        try {
            final List<Chunk> changesFromOriginal = comparator.getChangesFromOriginal();
            for (Chunk<String> chunk : changesFromOriginal) {
                System.out.println(chunk.toString());
            }

        } catch (IOException ioe) {
            fail("Error running test shouldGetChangesBetweenFiles " + ioe.toString());
        }
    }
}
