package com.p4ybill.stilt;

import com.p4ybill.stilt.index.FlatKey;
import com.p4ybill.stilt.index.Key;
import com.p4ybill.stilt.index.Query;
import com.p4ybill.stilt.index.Stilt;
import com.p4ybill.stilt.parser.*;
import com.p4ybill.stilt.utils.KeywordUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MappingFunction coordMappingFunction = new SpatialMappingFunction();
        DimensionMapper.Builder dimensionMapperBuilder = new DimensionMapper.Builder(4);
        DimensionMapper dimensionMapper = dimensionMapperBuilder.addMapper(coordMappingFunction)
                .addMapper(coordMappingFunction)
                .addMapper(new KeywordMappingFunction())
                .addMapper(new DateMappingFunction())
                .build();
        SimplePathScheduler pathScheduler = new SimplePathScheduler(4, 64, dimensionMapper);
//
        Stilt<Key> index = new Stilt<>(64, 4, null, pathScheduler);
//
        Key flatKey = new FlatKey(1, 40, 50, "cheqer", 1624194064, 1, 1);

        index.insert(flatKey, 1);

        Key flatKey2 = new FlatKey(1, 40, 60, "cheqer", 1624194064, 1, 1);

        index.insert(flatKey2, 1);
        Key flatKey3 = new FlatKey(1, 40, 50, "cheqeroputa", 1624194064, 1, 1);
        index.insert(flatKey3, 1);



//        Key flatKey = new FlatKey(1, 50, 40, "cheqer", 1624194064, 1, 1);
//
//        index.insert(flatKey, 1);
//
//        Key flatKey2 = new FlatKey(1, 60, 40, "cheqer", 1624194064, 1, 1);
//
//        index.insert(flatKey2, 1);




        Query query = index.initQuery();
//        query.setMinX(39d);
//        query.setMinY(20d);
//        query.setMaxX(61d);
//        query.setMaxY(41d);
        List<String> words = new ArrayList<>();
//        words.add("chiquit");
        words.add("cheqer");
//        words.add("cheqeroputa");
//        query.setWords(words);

        query.setMinTimestamp(1624191000L);
        query.setMaxTimestamp(1624197000L);

        List<Key> res = index.rangeSearch(query);
        for(Key key : res){
            System.out.println(key.getKeyword());
        }
//
        System.out.println(res.size());

    }
}