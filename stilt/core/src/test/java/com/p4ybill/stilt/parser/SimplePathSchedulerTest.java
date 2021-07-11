package com.p4ybill.stilt.parser;

import com.p4ybill.stilt.index.FlatKey;
import com.p4ybill.stilt.index.FourDimensionalKey;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimplePathSchedulerTest {
   @Test
   public void interleaveBitsCorrectlyFourDimensions(){
       MappingFunction coordMappingFunction = new SpatialMappingFunction();
       DimensionMapper.Builder dimensionMapperBuilder = new DimensionMapper.Builder(4);
        DimensionMapper dimensionMapper = dimensionMapperBuilder.addMapper(coordMappingFunction)
                        .addMapper(coordMappingFunction)
                        .addMapper(new KeywordMappingFunction())
                        .addMapper(new DateMappingFunction())
                        .build();

        FourDimensionalKey flatKey = new FlatKey(1, 50, 40, "cheqer", 1624194064, 100, 14);
//       0000000000101000 : 40
//       0000000000110010 : 50
//       0001101000001011 : keyword prefix cheqer
//       0000100000000111 : date
//       interleaved : 0000 0000 0000 0010 0011 0000 0010 0000 0000 0000 1100 0100 1010 0001 0111 0011

//       0000000000101000 : 40
//       0000000000111100 : 60
//       0001101000001011 : keyword prefix cheqer
//       0000100000000111 : date
//       interleaved : 0000 0000 0000 0010 0011 0000 0010 0000 0000 0000 1100 0100 1110 0001 0111 0011

       SimplePathScheduler pathScheduler = new SimplePathScheduler(4, 64, dimensionMapper);

       long mapped = pathScheduler.getKey(flatKey);
        assertEquals(0b0000000000000010001100000010000000000000110001001010000101110011L, mapped);
   }
}
