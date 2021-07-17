package com.p4ybill.stilt;

import com.p4ybill.stilt.index.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private Parser() {
    }

    public static void populateIndex(Stilt<FourDimensionalKey> index, File dataFile) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(dataFile));
            String sLine = "";
            int id = 1;
            while ((sLine = bufferedReader.readLine()) != null) {
                String[] components = sLine.split("[" + IndexUtils.DATA_COMPONENTS_SEPARATOR + "]");

                if(IndexUtils.DATA_ID_INDEX != -1){
                    id = Integer.parseInt(components[IndexUtils.DATA_ID_INDEX]);
                }
                double y = Double.parseDouble(components[IndexUtils.DATA_Y_INDEX]);
                double x = Double.parseDouble(components[IndexUtils.DATA_X_INDEX]);
                double timestamp = Double.parseDouble(components[IndexUtils.DATA_DATE_INDEX]);

                boolean hasKeywords = components.length == IndexUtils.DATA_LINE_COMPONENTS_LENGTH;
                if (hasKeywords) {
                    String keywords = components[IndexUtils.DATA_TEXTUAL_INDEX];
                    List<String> listKeywords = Arrays.asList(keywords.split(IndexUtils.DATA_TEXTUAL_SEPARATOR));
                    if (listKeywords.size() == 1) {
                        // there is one word so FlatKey is enough
                        FlatKey flatKey = new FlatKey(id, y, x, listKeywords.get(0), (long)timestamp, 1, 1);
                        index.insert(flatKey, id);
                    } else {
                        // there are more words so we should use SlimCommon
                        SlimCommon slimCommon = new SlimCommon(id, y, x, (long)timestamp, listKeywords.size());
                        for (String word : listKeywords) {
                            SlimKey slimKey = new SlimKey(slimCommon, word, 1);
                            index.insert(slimKey, id);
                        }
                    }
                    if(IndexUtils.DATA_ID_INDEX == -1){
                        id++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
