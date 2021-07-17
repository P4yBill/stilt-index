package com.p4ybill.stilt;

import com.p4ybill.stilt.index.Key;
import com.p4ybill.stilt.index.Query;
import com.p4ybill.stilt.index.Stilt;

import java.io.*;
import java.util.*;

/**
 * Brute Force method that tests if a range query does by a index returns the correct results.
 * `bruteForce_test_path` should be given
 * @param <K>
 */
public class BruteForceTester<K extends Key> {
    File file;

    public BruteForceTester() {
        String bruteForceTestPath = System.getProperty("bruteForce_test_path");
        this.file = new File(bruteForceTestPath);
    }


    /**
     * Executes a range query given an index and a query. Then its reading sequentially the test file and checks what documents satisfy the query.
     * Those should be the real results. Then it checks whether the results obtained from the range search match the real results.
     *
     * @param index STILT index
     * @param query given query to check results
     * @return
     */
    public boolean test(Stilt<K> index, Query query) {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String sLine = "";
            Set<Integer> resultsIds = index.rangeSearch(query);

            // finds the min and max of the spatial-temporal components.
            List<Integer> realResultIds = new ArrayList<>();
            int id = 1;
            while ((sLine = bufferedReader.readLine()) != null) {
                String[] components = sLine.split("[|]");
                if(IndexUtils.DATA_ID_INDEX != -1){
                    id = Integer.parseInt(components[IndexUtils.DATA_ID_INDEX]);
                }
                double y = Double.parseDouble(components[IndexUtils.DATA_Y_INDEX]);
                double x = Double.parseDouble(components[IndexUtils.DATA_X_INDEX]);
                double date = Double.parseDouble(components[IndexUtils.DATA_DATE_INDEX]);
                long dateLong = (long) date;

                boolean xSatisfied = true;
                boolean ySatisfied = true;
                boolean dateSatisfied = true;
                boolean wordsSatisfied = true;

                if (query.getMinX().isPresent()) {
                    xSatisfied = x >= query.getMinX().get();
                }
                if (query.getMaxX().isPresent()) {
                    xSatisfied = xSatisfied && x <= query.getMaxX().get();
                }

                if (query.getMinY().isPresent()) {
                    ySatisfied = y >= query.getMinY().get();
                }
                if (query.getMaxY().isPresent()) {
                    ySatisfied = ySatisfied && y <= query.getMaxY().get();
                }

                if (query.getMinTimestamp().isPresent()) {
                    dateSatisfied = dateLong >= query.getMinTimestamp().get();
                }
                if (query.getMaxTimestamp().isPresent()) {
                    dateSatisfied = dateSatisfied && dateLong <= query.getMaxTimestamp().get();
                }

                boolean hasKeywords = components.length == IndexUtils.DATA_LINE_COMPONENTS_LENGTH;
                if (hasKeywords) {
                    if (query.getWords().isPresent() && query.getWords().get().size() > 0) {
                        String keywords = components[IndexUtils.DATA_TEXTUAL_INDEX];
                        List<String> listKeywords = Arrays.asList(keywords.split(IndexUtils.DATA_TEXTUAL_SEPARATOR));
                        wordsSatisfied = listKeywords.containsAll(query.getWords().get());
                    }

                    boolean isResult = wordsSatisfied && xSatisfied && ySatisfied && dateSatisfied;
                    if (isResult) {
                        realResultIds.add(id);
                    }

                    if(IndexUtils.DATA_ID_INDEX == -1){
                        id++;
                    }
                }

            }

            return realResultIds.size() == resultsIds.size() && resultsIds.containsAll(realResultIds);
        } catch (IOException e) {

        }

        return false;
    }
}

