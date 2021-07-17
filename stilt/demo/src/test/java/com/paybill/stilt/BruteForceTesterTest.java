package com.paybill.stilt;

import com.p4ybill.stilt.BruteForceTester;
import com.p4ybill.stilt.Parser;
import com.p4ybill.stilt.index.FourDimensionalKey;
import com.p4ybill.stilt.index.Query;
import com.p4ybill.stilt.index.Stilt;
import com.p4ybill.stilt.parser.*;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class BruteForceTesterTest {
    Stilt<FourDimensionalKey> index;
    BruteForceTester<FourDimensionalKey> bruteForceTester;

    public BruteForceTesterTest(){
        MappingFunction coordMappingFunction = new SpatialMappingFunction();
        DimensionMapper.Builder dimensionMapperBuilder = new DimensionMapper.Builder(4);
        DimensionMapper dimensionMapper = dimensionMapperBuilder.addMapper(coordMappingFunction)
                .addMapper(coordMappingFunction)
                .addMapper(new KeywordMappingFunction())
                .addMapper(new DateMappingFunction())
                .build();
        SimplePathScheduler pathScheduler = new SimplePathScheduler(4, 64, dimensionMapper);

        this.index = new Stilt<>(64, 4, null, pathScheduler);
        File file = new File(System.getProperty("bruteForce_test_path"));
        Parser.populateIndex(index, file);

        this.bruteForceTester = new BruteForceTester<>();
    }

    @Test
    public void querySpatial0(){
        Query query = index.initQuery();

        query.setMinX(848d);
        query.setMinY(47176d);
        query.setMaxX(851d);
        query.setMaxY(47178d);

        assertTrue(bruteForceTester.test(index, query));
    }
}
