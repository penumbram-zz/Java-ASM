package cfg_live_variable_analysis.markup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by tolgacaner on 07/11/16.
 */
public class LineBinder {
    private static String[] lines;

    public LineBinder() {}

    public void bindLinesOf(String fileName) {
        int counter = 0;
        String line = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((line = bufferedReader.readLine()) != null) {
                counter++;
            }
            lines = new String[counter + 1];
            bufferedReader.close();
            bufferedReader = new BufferedReader(new FileReader(fileName));
            counter = 1;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                line = line.replace(';', ' ');
                line = line.replace('"', ' ');
                lines[counter] = line;
                counter++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String[] getLines() {
        return lines;
    }
}
