package com.yemyat.zopa.challenge;

import com.yemyat.zopa.challenge.error.InvalidDataFormatException;
import com.yemyat.zopa.challenge.model.LenderInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVDataSource implements DataSource {
    private String filename;

    public CSVDataSource(String filename) {
        this.filename = filename;
    }

    @Override
    public LenderInfo[] getData() {
        BufferedReader bufferedReader;
        String line;
        ArrayList<LenderInfo> lenders = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new FileReader(filename));
            bufferedReader.readLine();

            try {
                while ((line = bufferedReader.readLine()) != null) {
                    String[] columns = line.split(",");
                    lenders.add(new LenderInfo(Double.parseDouble(columns[1]), Integer.parseInt(columns[2])));
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new InvalidDataFormatException(
                        String.format("Data in %s does not conform to the expected format", filename));
            }
        } catch (IOException e) {
            System.err.println(String.format("Error accessing CSV file - %s", e.getMessage()));
        }
        return lenders.toArray(new LenderInfo[lenders.size()]);
    }
}
