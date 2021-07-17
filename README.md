# Stilt Index
This repo contains an implementation of **STILT**, a spatio-temporal textual in-memory index.

**STILT** index can be used as spatial, temporal or textual index, or a combination of them.

[paper](https://www.cs.unb.ca/~sray/papers/stilt_ssdbm2020.pdf)

### Requirements
- Gradle 6.7.1(intellij downloads it automatically)
- Java

#### Differences:
There are some differences worth mentioning between out implementation and the one presented in the paper.

- There is a difference with how we manage the edge path. In STILT paper, we can implicitly deduce from the algorithms that the paths are left shifted,
 that is why the author can do a simple *subtraction* or use the `msb` of the binary path in some places. Also we can explicitly see this left shift in `keywordToBits` function.
In our implementation the paths are right shifted(or no shifted at all).

   ***Example***(`8bit` per dimension), let path=`10101`:
    
    Author's implementation:`10101000`
    
    Our's: `00010101`
    
    **This may result in a poor performance(e.g. in range search) because we use more operations**

- Currently the implementation is not thread-safe as described in the paper.
- LSM is not used.
- Currently `keywordToBits` differs a bit.
- Top-K search is missing.

### Project Structure
There are two packages:
- `core`: Core library that contains the index.
- `demo`: Contains a demo using the index.

## Parser And DataTransformer
We provide simple utility classes, `Parser` and `DataTransformer`, to help with normalizing test data and parsing test data when running the index. 
You can also use your own.
Located under `stilt:demo` package. Uses the "Env" variables in `stilt:demo:IndexUtils`.
The following variables are used in `Parser` and `DataTransformer`.

    public static final String DATA_TEXTUAL_SEPARATOR = ",";
    public static final String DATA_COMPONENTS_SEPARATOR = "|";
    public static final int DATA_ID_INDEX = 0;
    public static final int DATA_X_INDEX = 1;
    public static final int DATA_Y_INDEX = 2;
    public static final int DATA_DATE_INDEX = 3;
    public static final int DATA_TEXTUAL_INDEX = 4;
    public static final int DATA_LINE_COMPONENTS_LENGTH = 5;

*In order to parse or transform data, a `.txt` file need to be passed with lines(see below) concatenated with `newline`(`\n`),
components with `DATA_COMPONENTS_SEPARATOR` and keywords with `DATA_TEXTUAL_SEPARATOR`.`DATA_LINE_COMPONENTS_LENGTH` should also be given which is the number of components 
concatenated with `DATA_COMPONENTS_SEPARATOR`, Indexes of components need to match the `INDEX` variables.*

*`DATA_{d}_INDEX` with `{d} belongs to {ID, X, Y, DATE, TEXTUAL}`*

`DATA_ID_INDEX` is not mandatory. Can also be `-1` if no `ID` index is present. If that happens, an auto-increment id will be given, starting from `1`.

##### Example of a file using the variables above.

| X        | Y           | Date  | Keywords |
| :------- |:------:| :-------- |:---------------------:|
| 1054.4233470292888| 45238.97821558467 | 36019.409703537196 |    laundry_service,cable_tv |

In test file:

`x|y|date|word1,word2`

`1054.4233470292888|45238.97821558467|36019.409703537196|laundry_service,cable_tv`


---

 ### Data normalization(`DataTransformer`)
 Located under `stilt:demo` package.
 
 Spatial components were normalized in (0, 65535) range.
 
 Calling the `transformData()` function in main and passing the following System properties(`-Dproperty=value`):
 
- `original_data_path` (path of hotels-v1 data)
- `transformed_data_path` (output file for normalized data)

The author of the paper states that the spatio-temporal components were normalized in `[0, 100]`(`8bits` per dimension). 
We normalized the data in `[0, 65535]` range as we used `16bits` for each dimension in our example. With this way, we can achieve a better precision.
 
 
### Using test data(`Parser`)
  Located under `stilt:demo` package. 
It can be used for populating the data. `Env Variables` are needed as described in ***Parser And DataTransformer*** section

  - `test_data_path` System property is needed, which tells the `Parser` which file to use.

### Using Brute Force test class(`BruteForceTester`)
  Located under `stilt:demo` package. 
  Populates the index with the parser as described, and does a range query given a `Query` `query` and then checks if the results obtained from range query are correct.
  
  - `bruteForce_test_path` System property is needed, which tells the `BruteForceTester` and `Parser` which file to use.
    

### Getting started(example)
The following example can be found in `stilt:demo:Main.java` file
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
    
    *Note*:
    A `PathScheduler` is needed for the index as it contains all mapping functions(DimensionMapper) as well as the `getKey` function in which the interleaving process of bits reside . We provide an simple implementation of a `PathScheduler` called `SimplePathScheduler` with some very simple mapping functions(`MappingFunction` interface).
2. **Populate index:**
	The index can be populated with the `insert` function, e.g.:
	
	`index.insert(key, 1);`
	 
	 Also we provide a simple `Parser` class as described in the `Using test data(Parser)` section, that helps with populating the index.
3. **Range Search**

	    Query query = index.initQuery();  
	    query.setMinX(minX);  
	    query.setMaxX(maxX);  
	    ...
	      
	    Set<Integer> ids = index.rangeSearch(query);
	
	If for a dimension there is no range given, then that dimension will be ignored.
	

### Run Demo

    $ gradlew build
    $ gradlew stilt:demo:run
 
 Passing a System property: `... -Dproperty=value`
 
 To pass the file path of test data and use `Parser` `test_data_path` System property has to be included in the run command:
 
 `$ gradlew stilt:demo:run -Dtest_data_path=path`

## Performance

**Index construction:** ~1768ms

**Range Search(spatial query):** ~12ms