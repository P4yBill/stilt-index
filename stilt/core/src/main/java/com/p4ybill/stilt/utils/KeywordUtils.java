package com.p4ybill.stilt.utils;

import java.math.BigInteger;

/**
 * Contains utility functions for keywords like {@link KeywordUtils#keywordToBits(String, int)}
 */
public class KeywordUtils {
    private static final BigInteger bigInteger = new BigInteger(String.valueOf(Integer.MAX_VALUE));

    public static int keywordToBits(String s, int b) {
        if ((s.length() * 5) < b) {
            s = padRightZero(s, b/5 + 1);
        }

        int bits = 0b0;
        char[] arrChar = s.toCharArray();

        for (int i = 0; i < b / 5; i++) {
            System.out.println("Char: " + arrChar[i] + " -- mapped to: " + Integer.toBinaryString(charToBits(arrChar[i])));
            bits = (bits << 5) + charToBits(arrChar[i]);
        }

        int rem = b % 5;
        if(rem != 0){
            bits = bits << rem;
            bits = bits + (charToBits(arrChar[b/5]) >> (5 - rem));
        }

        return bits;
    }

    private static int charToBits(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a' + 1;
        } else if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 1;
        } else {
            switch (c) {
                case '\0':
                    return 0;
                case '?':
                    return 27;
                default:
                    return 31;
            }
        }
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padWithNulls(String s, int n) {
        return padRight(s, n);
    }

    public static String padRightZero(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder(inputString);
        while (sb.length() < length) {
            sb.append('\0');
        }

        return sb.toString();
    }
}
