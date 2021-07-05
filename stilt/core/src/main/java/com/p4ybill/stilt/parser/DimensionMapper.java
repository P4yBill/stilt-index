package com.p4ybill.stilt.parser;

import com.p4ybill.stilt.utils.Validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Keeps all mapping functions for each dimension.
 * One mapping function should be given for each dimension.
 *
 * The order that the functions were added, dictate what function will be used for a dimension k.
 * E.g. the mapping function that was added first, will be used for the first dimension etc.
 */
public class DimensionMapper {
    private final MappingFunction[] mappingFunctions;

    /**
     * Private constructor it is used by {@link Builder#build()} method.
     * @param mappingFunctions array of mapping functions
     */
    private DimensionMapper(MappingFunction[] mappingFunctions) {
        this.mappingFunctions = mappingFunctions;
    }

    /**
     * Returns a mapping function for a dimension index.
     * @param dim dimension index of dimension k.
     * @return DimensionMapFunction the mapping function of the corresponding dimension.
     */
    public MappingFunction getMappingFunction(int dim) {
        return this.mappingFunctions[dim];
    }

    /**
     * @return DimensionMapFunction[] all mapping functions.
     */
    public MappingFunction[] getAllMappingFunctions() {
        return Arrays.copyOf(this.mappingFunctions, this.mappingFunctions.length);
    }

    /**
     * Builder for constructing the {@link DimensionMapper}
     */
    public static class Builder {
        private List<MappingFunction> mappingFunctionsList;
        private int dimensions;

        public Builder(int dimensions) {
            this.dimensions = dimensions;
            this.mappingFunctionsList = new ArrayList<>();
        }

        /**
         * Adds a mapping function to the array.
         * @param mapFunction
         * @return
         */
        public Builder addMapper(MappingFunction mapFunction) {
            this.mappingFunctionsList.add(mapFunction);

            return this;
        }

        /**
         * Builds the dimension mapper and returns it with the added mapping functions.
         * @throws IllegalArgumentException If number of mapping function given is less than the number of dimensions.
         * @return DimensionMapper
         */
        public DimensionMapper build() {
            Validators.checkArgument(this.mappingFunctionsList.size() == this.dimensions, "The number of mapping functions should be equal to the number of dimensions.");

            MappingFunction[] array = new MappingFunction[this.mappingFunctionsList.size()];
            DimensionMapper dm = new DimensionMapper(this.mappingFunctionsList.toArray(array));
            this.mappingFunctionsList = Collections.emptyList();

            return dm;
        }

    }
}
