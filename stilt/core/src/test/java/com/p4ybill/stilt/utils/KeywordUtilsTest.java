package com.p4ybill.stilt.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeywordUtilsTest {
    @Test
    public void keyWordToBitsSamePrefix() {
        int map = KeywordUtils.keywordToBits("cheqer", 16);
        int map2 = KeywordUtils.keywordToBits("cheqero", 16);
        int map3 = KeywordUtils.keywordToBits("chepo", 16);
        assertTrue(map == map2 && map == map3);
    }
}
