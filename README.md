# Stilt Index
This repo contains an implementation of STILT, a spatio-temporal textual index.

[paper](https://www.cs.unb.ca/~sray/papers/stilt_ssdbm2020.pdf)

Currently the implementation is not thread-safe as decribed in the paper.



### Example

1. **Initialize index:**

	    MappingFunction coordMappingFunction = new SpatialMappingFunction();  
		DimensionMapper.Builder dimensionMapperBuilder = new DimensionMapper.Builder(4);  
		DimensionMapper dimensionMapper = dimensionMapperBuilder.addMapper(coordMappingFunction)  
	        .addMapper(coordMappingFunction)  
	        .addMapper(new KeywordMappingFunction())  
	        .addMapper(new DateMappingFunction())  
	        .build();  
		SimplePathScheduler pathScheduler = new SimplePathScheduler(4, 64, dimensionMapper);
		Stilt<Key> index = new Stilt<>(64, 4, null, pathScheduler);
    
    *Notes*:
    A `PathScheduler` is needed for the index as it contains all mapping functions(DimensionMapper) as well as the `getKey` function in which the interleaving process of bits reside . We provide an simple implementation of a `PathScheduler` called `SimplePathScheduler` with some very simple mapping functions(`MappingFunction` interface).
2. **Populate index:**
	The index can be populated with the `insert` function, e.g.:
	`index.insert(flatKey, 1);`. There are some classes(e.g. `DataTransformer`) and Functions(`populateIndex` in stilt:demo:Main.java) which help with transforming the test data(e.g. normalizing) and with the `insertion`.
3. **Query**

	    Query query = index.initQuery();  
	    query.setMinX(minX);  
	    query.setMaxX(maxX);  
	    ...
	      
	    Set<Integer> ids = index.rangeSearch(query);
	
	If for some dimension there is no range given then that dimension will be ignored.
	
	
### Run Demo
Basic command:
`$ gradlew stilt:demo:run`

 To pass the file path of hotels-v1 test data you have to include `test_data_path` system property:
 `$ gradlew stilt:demo:run -Dtest_data_path=path`
 
  
 ### Data normalization(hotels-v1 data)
 Spatial components were normalized in (0, 65535) range.
 
 Calling the `transformData()` function in main and passing the following System properties:
 
- `original_data_path` (path of hotels-v1 data)
- `transformed_data_path` (output file for normalized data)

The author of the paper says that the data they used were normalized. It is clear that
the spatial components were normalized between 0 and 100. We normalized the data between 0 and 65535
as we used 16bits for each dimension. Therefore, there is a better precision.

## Performance

**Index construction:** ~1768ms

Range search with spatial query only:

**Range Search:** ~12ms