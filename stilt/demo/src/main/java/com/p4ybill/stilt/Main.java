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
        Stilt<FourDimensionalKey> index = new Stilt<>(64, 4, null, pathScheduler);

        System.out.println("Populating...");

        Instant start = Instant.now();
        populateIndex(index);
        Instant end = Instant.now();

        System.out.println("DONE!");
        System.out.println("Index construction time taken: " + Duration.between(start, end).toMillis() + "ms");

        spatialQuery(index);
    }

    private static void spatialTextualQuery(Stilt<FourDimensionalKey> index){
        Query query = index.initQuery();
        query.setMinX(65530d);
        query.setMinY(730d);
        query.setMaxX(65535d);
        query.setMaxY(740d);

//        query.setMinTimestamp(3823L);
//        query.setMaxTimestamp(3825L);

        List<String> words = new ArrayList<>();
        words.add("air_conditioning");
        words.add("wedding_services");
        words.add("internet_free");
        words.add("internet_wireless");
        query.setWords(words);

        Instant start = Instant.now();
//        Set<Integer> ids = index.rangeSearch(query);
        List<FourDimensionalKey> keys = index.rangeSearch(query);
        Instant end = Instant.now();
        System.out.println("Range Search took: " + Duration.between(start, end).toMillis() + "ms");
        System.out.println("Results: " + keys.size());

        keys.forEach(System.out::println);
        keys.forEach(key -> System.out.println(key.getId()));
    }

    private static void spatialQuery(Stilt<FourDimensionalKey> index){
        Query query = index.initQuery();
//        query.setMinX(848d);
//        query.setMinY(47176d);
//        query.setMaxX(851d);
//        query.setMaxY(47178d);

        query.setMinTimestamp(3823L);
        query.setMaxTimestamp(3825L);

        Instant start = Instant.now();
        List<FourDimensionalKey> keys = index.rangeSearch(query);
        Instant end = Instant.now();
        System.out.println("Range Search took: " + Duration.between(start, end).toMillis() + "ms");
        System.out.println("Results: " + keys.size());

//        keys.forEach(System.out::println);
//        keys.forEach(key -> System.out.println(key.getId()));
        keys.forEach(key -> System.out.println(key.getX()));
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