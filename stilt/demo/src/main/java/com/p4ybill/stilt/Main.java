package com.p4ybill.stilt;

import com.p4ybill.stilt.index.Node;
import com.p4ybill.stilt.index.Node32;
import com.p4ybill.stilt.index.Node8;

public class Main {
    public static void main(String[] args){
        byte leftLengthByte = (byte) 7;
        byte leftPathByte = (byte) 124;
        byte rightLengthByte = (byte) 56;
        byte rightPathByte = (byte) 89;

        int leftPathInt = 7;

        int rightPathInt = 56;


        Node nodeTest = new Node8(leftLengthByte,
                leftPathByte,
                rightLengthByte,
                rightPathByte);

        Node nodeTest2 = new Node32(leftLengthByte,
                leftPathInt,
                rightLengthByte,
                rightPathInt);

        System.out.println();

//        byte five = 0b101;
//        assertEquals((byte) 5, five);
//
//        short three = 0b11;
//        assertEquals((short) 3, three);
//
//        int nine = 0B1001;
//        assertEquals(9, nine);
//
//        long twentyNine = 0B11101;
//        assertEquals(29, twentyNine);
//
//        int minusThirtySeven = -0B100101;
//        assertEquals(-37, minusThirtySeven);

        int byteNum = 0b11111111;
        System.out.println("////- BYTESSSS -////");
//        System.out.println(byteNum << 1);
        char c = 'f';
        System.out.println(c - 'a' + 1);
    }
}
