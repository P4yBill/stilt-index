package com.p4ybill.stilt.utils;

/**
 * Contains utility functions for keywords like {@link KeywordUtils#keywordToBits(String, int)}
 */
public class KeywordUtils {
    public static int keywordToBits(String s, int b) {
        return 0;
    }


    private int charToBits(char c){
        if(c >= 'a' && c <= 'z'){
            return c - 'a' + 1;
        }else if(c >= 'A' && c <= 'Z'){
            return c - 'A' + 1;
        }else{
            switch(c){
                case '\0':
                    return 0;
                case '?':
                    return 27;
//                case '"':
//                    return 28;
//                case '“':
//                    return 29;
//                case '°':
//                    return 30;
                default:
                    return 31;
            }
        }
    }
}
