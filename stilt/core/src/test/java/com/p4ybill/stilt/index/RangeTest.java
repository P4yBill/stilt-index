package com.p4ybill.stilt.index;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class RangeTest {
    @Test
    public void intersectingUpperBound(){
        Range r1 = new Range(30, 50);
        Range r2 = new Range(29,31);

        assertTrue(r1.intersects(r2));
        assertTrue(r2.intersects(r1));
    }

    @Test
    public void intersectingBothBounds(){
        Range r1 = new Range(30, 50);
        Range r2 = new Range(29,31);

        assertTrue(r1.intersects(r2));
        assertTrue(r2.intersects(r1));
    }
}
