import java.util.ArrayList;
import java.util.List;

public class Table {

    private List<String> columnNames, rowNames;
    private Object[][] table;

    public Table(List<String> columnNames, List<String> rowNames, Object[][] table) {
        this.columnNames = columnNames;
        this.rowNames = rowNames;
        this.table = table;
    }

    public String toString() {
        String returnString = "";
        List <Integer> columnSizes = new ArrayList<>();
        int width = 0;
        int length = 4 * rowNames.size();

        // compute width of columns
        int firstColumnMaxSize = 0;
        for (int j = 0; j < columnNames.size(); j++) {
            if (firstColumnMaxSize < table[0][j].toString().length()) {
                firstColumnMaxSize = table[0][j].toString().length();
            }
        }
        columnSizes.add(firstColumnMaxSize);
        width += firstColumnMaxSize + 3;

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            int maxSize = columnName.length();
            for (int j = 0; j < columnNames.size(); j++) {
                if (maxSize < table[i][j].toString().length()) {
                    maxSize = table[i][j].toString().length();
                }
            }
            columnSizes.add(maxSize);
            width += maxSize + 3;
        }

        // put characters in two dimensional array
        char[][] outputTable = new char[length][width];

        // vertical lines
        int currColumnInOutputTable = 0;
        for (int i = 0; i < columnNames.size() - 1; i++) {
            currColumnInOutputTable += columnSizes.get(i);
            for (int j = 0; j < length; j++) {
                outputTable[j][currColumnInOutputTable] = '|';
            }
        }

        // horizontal lines
        for (int i = 0; i < rowNames.size() - 1; i++) {
            for (int j = 0; j < width; j++) {
                int r = (i + 1) * 4;
                outputTable[r][j] = outputTable[r][j] == '|' ? '+' : '-';
            }
        }

        // convert array to string
        // todo

        return returnString;
    }


}
