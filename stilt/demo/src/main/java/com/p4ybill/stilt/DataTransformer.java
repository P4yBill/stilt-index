package com.p4ybill.stilt;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class DataTransformer {
    private File file;
    private File fileNew;

    public DataTransformer(File file, File fileNew) {
        if (file == null) {
            throw new IllegalArgumentException("Given file should not be null");
        }

        this.file = file;
        this.fileNew = fileNew;
    }

    public void transform() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String sLine = "";

        double maxX = 0d;
        double minX = Double.MAX_VALUE;
        double maxY = 0d;
        double minY = Double.MAX_VALUE;

        int maxDate = 0;
        int minDate = Integer.MAX_VALUE;
        // finds the min and max of the spatial-temporal components.
        while ((sLine = bufferedReader.readLine()) != null) {
            String[] components = sLine.split("[|]");
            double y = Double.parseDouble(components[IndexUtils.DATA_Y_INDEX]);
            double x = Double.parseDouble(components[IndexUtils.DATA_X_INDEX]);
            int date = Integer.parseInt(components[IndexUtils.DATA_DATE_INDEX]);
            if (x > maxX) {
                maxX = x;
            }
            if (x < minX) {
                minX = x;
            }

            if (y > maxY) {
                maxY = y;
            }
            if (y < minY) {
                minY = y;
            }

            if (date > maxDate) {
                maxDate = date;
            }
            if (date < minDate) {
                minDate = date;
            }
        }

        fileInputStream.getChannel().position(0);
        bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileNew, true));

        // normalized x and y and append to another file
        while ((sLine = bufferedReader.readLine()) != null) {
            String[] components = sLine.split("[" + IndexUtils.DATA_COMPONENTS_SEPARATOR + "]");

            double y = Double.parseDouble(components[IndexUtils.DATA_Y_INDEX]);
            double x = Double.parseDouble(components[IndexUtils.DATA_X_INDEX]);
            int date = Integer.parseInt(components[IndexUtils.DATA_DATE_INDEX]);

            double normalizedX = getNormalized(minX, maxX, x);
            double normalizedY = getNormalized(minY, maxY, y);
            double normalizedDate = getNormalized(minDate, maxDate, date);

            components[IndexUtils.DATA_Y_INDEX] = String.valueOf(normalizedY);
            components[IndexUtils.DATA_X_INDEX] = String.valueOf(normalizedX);
            components[IndexUtils.DATA_DATE_INDEX] = String.valueOf(normalizedDate);

            String lineNew = String.join(IndexUtils.DATA_COMPONENTS_SEPARATOR, components);
            writer.append(lineNew).append("\n");
        }

        bufferedReader.close();
        fileInputStream.close();
        writer.close();

    }

    /**
     * Normalizes a value based on a boundary with min-max normalization.
     *
     * @param min
     * @param max
     * @param value
     * @return
     */
    private double getNormalized(double min, double max, double value) {
        return ((value - min) / (max - min)) * IndexUtils.NORMALIZATION_BOUNDARY;
    }

}
