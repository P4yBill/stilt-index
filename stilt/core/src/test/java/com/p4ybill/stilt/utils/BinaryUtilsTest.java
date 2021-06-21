package com.p4ybill.stilt.utils;

import com.p4ybill.stilt.parser.SimplePathScheduler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinaryUtilsTest {

    @Test
    public void clzBounded() {
        long b1 = 0b00011110;
        long b2 = 0b01111010;
        int pathLen = 8;

        assertEquals(BinaryUtils.clzBounded(b1, pathLen), 3);
        assertEquals(BinaryUtils.clzBounded(b2, pathLen), 1);
    }

    @Test
    public void getBinaryTrimmed(){
        long b = 0b11000101;
        int len = 9;
        long withLeadingZ = 0b00000101;

        assertEquals(BinaryUtils.trim(b, len, 3), 0b101);
        assertEquals(BinaryUtils.trim(withLeadingZ, len, 3), 0b101);
    }
}
