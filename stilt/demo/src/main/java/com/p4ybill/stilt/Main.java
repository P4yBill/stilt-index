package com.p4ybill.stilt;

import com.p4ybill.stilt.index.Key;
import com.p4ybill.stilt.index.Stilt;
import com.p4ybill.stilt.utils.KeywordUtils;

public class Main {
    public static void main(String[] args) {

        Stilt<Key> index = new Stilt<>(64, 4);
    }

    private static void testingStilt() {

    }


    private static void testingKeywordToBits() {
        System.out.println(Integer.toBinaryString(KeywordUtils.keywordToBits("cheqer", 16)));
    }
    
    public static int interleave(int a, int b) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if ((a & (1 << i)) != 0)     // the bit at position i in a is 1
            {
                ans |= 1L << i * 2 + 1;  // set the bit at position (i*2 + 1) in ans to 1
            }
            if ((b & (1 << i)) != 0)     // the bit at position i in b is 1
            {
                ans |= 1L << i * 2;      // set the bit at position (i*2) in ans to 1
            }
        }
        return ans;
    }

    public static int zOrdering(int x, int y) {
        System.out.println("Leading zero: " + ((y & 0xff) >> 7));

        int z = 0;

        for (int i = 0; i < Integer.SIZE; i++) {
            int x_masked_i = (x & (1 << i));
            int y_masked_i = (y & (1 << i));

            z |= (x_masked_i << i);
            z |= (y_masked_i << (i + 1));
        }
        return z;
    }
}