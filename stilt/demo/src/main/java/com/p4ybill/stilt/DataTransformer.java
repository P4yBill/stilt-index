package com.p4ybill.stilt;

import java.io.*;

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

        int i = 0;
        double maxX = 0d;
        double minX = Double.MAX_VALUE;
        double maxY = 0d;
        double minY = Double.MAX_VALUE;

        // finds the min and max of the spatial components.
        while ((sLine = bufferedReader.readLine()) != null) {
            String[] components = sLine.split("[|]");
            double y = Double.parseDouble(components[4]);
            double x = Double.parseDouble(components[5]);
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
        }

        fileInputStream.getChannel().position(0);
        bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileNew, true));

        // normalized x and y and append to another file
        while ((sLine = bufferedReader.readLine()) != null) {
            String[] components = sLine.split("[|]");

            double y = Double.parseDouble(components[4]);
            double x = Double.parseDouble(components[5]);
            double normalizedX = getNormalized(minX, maxX, x);
            double normalizedY = getNormalized(minY, maxY, y);

            components[4] = String.valueOf(normalizedX);
            components[5] = String.valueOf(normalizedY);

            String lineNew = String.join("|", components);
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
