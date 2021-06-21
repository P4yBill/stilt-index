package com.p4ybill.stilt.parser;

import com.p4ybill.stilt.utils.KeywordUtils;

public class KeywordMappingFunction implements MappingFunction {

    @Override
    public int map(Object value, int bits) {
        String keyword = (String) value;

        return KeywordUtils.keywordToBits(keyword, bits);
    }
}
