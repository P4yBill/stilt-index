package com.p4ybill.stilt.parser;

import java.util.Calendar;

public class DateMappingFunction implements MappingFunction {

    @Override
    public int map(Object value, int bits) {
        long timestamp = (long) value;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp * 1000);
        return cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH) + cal.get(Calendar.MINUTE) + cal.get(Calendar.HOUR) + cal.get(Calendar.SECOND);
    }
}
