package com.p4ybill.stilt.parser;

import com.p4ybill.stilt.utils.BinaryUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimplePathSchedulerTest {
//    @Test
//    public void interleaveBitsCorrectlyFourDimsZeroMsb(){
//        int[] dims = new int[]{ 0b0001, 0b1101, 0b0101, 0b0000 };
//
//
//        SimplePathScheduler pathScheduler = new SimplePathScheduler(4, 16);
//
//        // 15 bits in length plus 1 lz = 16bits
//        assertEquals(pathScheduler.getKey(dims), 0b100011000001110);
//    }
//
//    @Test
//    public void interleaveBitsCorrectlyTwoDimsZeroMsb(){
//        int[] dims = new int[]{ 0b00011101, 0b11000001 };
//
//        SimplePathScheduler pathScheduler = new SimplePathScheduler(2, 16);
//
//        assertEquals(pathScheduler.getKey(dims), 0b0101001010100011);
//    }
//
   @Test
   public void interleaveBitsCorrectlyThreeDimsZeroMsb(){
       int[] dims = new int[]{ 0b00011101, 0b11000001, 0b10000001 };
       MappingFunction coordMappingFunction = new SpatialMappingFunction();

        DimensionMapper.Builder dimensionMapperBuilder = new DimensionMapper.Builder(4);
        DimensionMapper dimensionMapper = dimensionMapperBuilder.addMapper(coordMappingFunction)
                        .addMapper(coordMappingFunction)
                        .addMapper(new KeywordMappingFunction())
                        .addMapper(new DateMappingFunction())
                        .build();


       SimplePathScheduler pathScheduler = new SimplePathScheduler(4, 64, dimensionMapper);

    //    assertEquals(pathScheduler.getKey(dims), 0b0101001010100011);
   }
}
