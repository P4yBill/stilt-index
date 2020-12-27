package com.p4ybill.stilt.utils;

public class BinaryUtils {
    public static int clz(long binaryNum) {
        return Long.numberOfLeadingZeros(binaryNum);
    }

    public static int msb(int binaryNum) {
        return (binaryNum & 0x80) >>> 7;
    }

    public static int bitSetWithoutMsb(int binaryNum) {
        return unsetBit(binaryNum, 1);
    }

    private static int unsetBit(int b, int p) {
        int mask = 1 << 8 - p;
        return b & ~mask;
    }

    private static int unsetBit2(int b, int p) {
        int mask = 1 << 8 - p;
        return b & ~mask;
    }

    public static long getBitsAfterPosition(long b, int length){
        return b >>> length;
    }

    public static boolean isOneAtPosition(long b, int length){
        return (b >>> length) == 1;
    }

    /**
     * Trims the leading bits based on the given number
     *
     * @param b the binary string
     * @param l length of the binary string
     * @param trimLength number of leading bits to trim
     * @return trimmed binary string
     */
    public static long trim(long b, int l, int trimLength){
        long mask = (~0x0) >>> (64 - (l - trimLength));

        return b & mask;
    }

    /**
     * Returns the position of the highest one bit in the binary string
     *
     * @param b the binary string
     * @return {int} highest one bit position
     */
    public static int positionOfHighestOneBit(int b) {
        return (int) ((Math.log10(Integer.highestOneBit(b)) / Math.log10(2)) + 1);
    }

    /**
     * Bounded clz - {@link BinaryUtils#clz(long)} that takes into consideration a threshold(bound) {@code bound}.
     * @param b the binary string number
     * @param bound desired upper bound. Should be in range 1..64
     * @return number of leading zeros from the upper bound {@code bound}. Zero is returned if given bits is less than the bits of the given binary number.
     */
    public static int clzBounded(long b, int bound) {
        int clzBounded = clz(b) - (64 - bound);
        return Math.max(clzBounded, 0);
    }

    public static long interleave2(int x, int y) {
        int[] B = {0x55555555, 0x33333333, 0x0F0F0F0F, 0x00FF00FF};
        int[] S = {1, 2, 4, 8};

        x = (x | (x << S[3])) & B[3];
        System.out.println(Integer.toBinaryString(x));
        x = (x | (x << S[2])) & B[2];
        System.out.println(Integer.toBinaryString(x));
        x = (x | (x << S[1])) & B[1];
        System.out.println(Integer.toBinaryString(x));
        x = (x | (x << S[0])) & B[0];
        System.out.println(Integer.toBinaryString(x));

        y = (y | (y << S[3])) & B[3];
        System.out.println(Integer.toBinaryString(y));
        y = (y | (y << S[2])) & B[2];
        System.out.println(Integer.toBinaryString(y));
        y = (y | (y << S[1])) & B[1];
        System.out.println(Integer.toBinaryString(y));
        y = (y | (y << S[0])) & B[0];
        System.out.println(Integer.toBinaryString(y));

        return x | (y << 1);
    }
}
