import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final List<String> columnNames;
    private final List<String> rowNames;
    private final Object[][] table;

    public Table(List<String> rowNames, List<String> columnNames, Object[][] table) {
        this.columnNames = columnNames;
        this.rowNames = rowNames;
        this.table = table;
    }

    public String toString() {
        List <Integer> columnSizes = new ArrayList<>();
        int width = 0;
        int length = 2 * rowNames.size() + 1;

        // compute width of columns
        int firstColumnMaxSize = 0;
        for (String s : rowNames) {
            if (firstColumnMaxSize < s.length()) {
                firstColumnMaxSize = s.length();
            }
        }
        columnSizes.add(firstColumnMaxSize + 2);
        width += firstColumnMaxSize + 2;

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            int maxSize = columnName.length();
            for (int j = 0; j < rowNames.size(); j++) {
                if (maxSize < table[i][j].toString().length()) {
                    maxSize = table[i][j].toString().length();
                }
            }
            columnSizes.add(maxSize + 3);
            width += maxSize + 3;
        }

        // put characters in two dimensional array
        char[][] outputTable = new char[length][width];

        // put spaces on whole array
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                outputTable[i][j] = ' ';
            }
        }

        // vertical lines
        int currColumnInOutputTable = 0;
        for (int i = 0; i < columnSizes.size() - 1; i++) {
            currColumnInOutputTable += columnSizes.get(i);
            for (int j = 0; j < length; j++) {
                outputTable[j][currColumnInOutputTable] = '|';
            }

            // insert column name
            String name = columnNames.get(i);
            for (int j = 0; j < name.length(); j++) {
                outputTable[0][currColumnInOutputTable + 2 + j] = name.charAt(j);
            }

            // insert table content
            for (int j = 0; j < rowNames.size(); j++) {
                String cell = table[i][j].toString();
                for (int k = 0; k < cell.length(); k++) {
                    outputTable[(j + 1) * 2][currColumnInOutputTable + 2 + k] = cell.charAt(k);
                }
            }
        }

        // horizontal lines
        for (int i = 0; i < rowNames.size(); i++) {
            for (int j = 0; j < width; j++) {
                int r = (i) * 2 + 1;
                outputTable[r][j] = outputTable[r][j] == '|' ? '+' : '-';
            }
        }

        // insert row names
        for (int i = 0; i < rowNames.size(); i++) {
            String rowName = rowNames.get(i);
            for (int j = 0; j < rowName.length(); j++) {
                outputTable[(i + 1) * 2][j + 1] = rowName.charAt(j);
            }
        }

        // convert 2d array to string
        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                returnString.append(outputTable[i][j]);
            }
            returnString.append("\n");
        }

        return returnString.toString();
    }
}
