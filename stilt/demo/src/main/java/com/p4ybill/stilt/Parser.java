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
            while ((sLine = bufferedReader.readLine()) != null) {
                String[] components = sLine.split("[|]");
                int id = Integer.parseInt(components[0]);

                double y = Double.parseDouble(components[4]);
                double x = Double.parseDouble(components[5]);
                long timestamp = 1624194064;
                if(components.length < 7){
                    FlatKey flatKey = new FlatKey(id, y, x, "", timestamp, 1, 1);
                    index.insert(flatKey, id);
                }else{
                    String keywords = components[6];
                    List<String> listKeywords = Arrays.asList(keywords.split(","));
                    SlimCommon slimCommon = new SlimCommon(id, y, x, timestamp, listKeywords.size());
                    for (String word : listKeywords) {
                        SlimKey slimKey = new SlimKey(slimCommon, word, 1);
                        index.insert(slimKey, id);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
