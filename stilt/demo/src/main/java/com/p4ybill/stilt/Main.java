package com.p4ybill.stilt;

import com.p4ybill.stilt.index.FourDimensionalKey;
import com.p4ybill.stilt.index.Query;
import com.p4ybill.stilt.index.Stilt;
import com.p4ybill.stilt.parser.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Stilt<FourDimensionalKey> index = new Stilt<>(64, 4, null, pathScheduler);
//
//        Key flatKey = new FlatKey(1, 40, 50, "cheqer", 1624194064, 1, 1);
//
//        index.insert(flatKey, 1);
//
//        Key flatKey2 = new FlatKey(1, 40, 60, "cheqer", 1624194064, 1, 1);
//
//        index.insert(flatKey2, 1);
//        Key flatKey3 = new FlatKey(1, 40, 50, "cheqeroputa", 1624194064, 1, 1);
//        index.insert(flatKey3, 1);


//        Key flatKey = new FlatKey(1, 50, 40, "cheqer", 1624194064, 1, 1);
//
//        index.insert(flatKey, 1);
//
//        Key flatKey2 = new FlatKey(1, 60, 40, "cheqer", 1624194064, 1, 1);
        System.out.println("Populating...");
        Instant start = Instant.now();
        populateIndex(index);
        Instant end = Instant.now();
        System.out.println("DONE!");
        System.out.println("Index construction time taken: " + Duration.between(start, end).toMillis() + "ms");
        Query query = index.initQuery();
        query.setMinX(65530d);
        query.setMinY(730d);
        query.setMaxX(65535d);
        query.setMaxY(740d);
        List<String> words = new ArrayList<>();
//        words.add("air_conditioning");
//        words.add("wedding_services");
//        words.add("internet_free");
//        words.add("internet_wireless");
        query.setWords(words);
//
//        query.setMinTimestamp(1624191000L);
//        query.setMaxTimestamp(1624197000L);

//        List<Key> res = index.rangeSearch(query);
//        for(Key key : res){
//            System.out.println(key.toString());
//        }
////
//        System.out.println(res.size());

        start = Instant.now();
        Set<Integer> ids = index.rangeSearch(query);
        end = Instant.now();
        System.out.println("Range Search took: " + Duration.between(start, end).toMillis() + "ms");

//        ids.forEach(System.out::println);
        System.out.println(ids.size());

    }

    private static void populateIndex(Stilt<FourDimensionalKey> stilt){
        String testDataPath = System.getProperty("test_data_path");

        File file = new File(testDataPath);
        Parser.populateIndex(stilt, file);
    }

    /**
     * Uses {@link DataTransformer} to transform the data(e.g. normalize spatial components).
     * Properties original_data_path and transformed_data_path system properties should be passed in command also
     * <code>gradlew stilt:demo:run -Doriginal_data_path=oPath -Dtransformed_data_path=tPath</code>
     */
    public static void transformData() {
        String originalDataPath = System.getProperty("original_data_path");
        String transformedDataPath = System.getProperty("transformed_data_path");

        File file = new File(originalDataPath);
        File fileNew = new File(transformedDataPath);
        try {
            fileNew.delete();
            if(fileNew.createNewFile()){
                System.out.println("FILE CREATED");

            }else{
                System.out.println("ERROR WHILE CREATING FILE");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataTransformer dataTransformer = new DataTransformer(file, fileNew);

        try {
            dataTransformer.transform();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}