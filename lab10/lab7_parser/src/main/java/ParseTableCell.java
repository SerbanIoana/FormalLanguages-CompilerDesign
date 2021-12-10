import java.util.List;

public class ParseTableCell {
    private String row;
    private String column;
    private List<String> production;
    private int productionIndex;

    public ParseTableCell(String row, String column, List<String> production, int productionIndex) {
        this.row = row;
        this.column = column;
        this.production = production;
        this.productionIndex = productionIndex;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public List<String> getProduction() {
        return production;
    }

    public void setProduction(List<String> production) {
        this.production = production;
    }

    public int getProductionIndex() {
        return productionIndex;
    }

    public void setProductionIndex(int productionIndex) {
        this.productionIndex = productionIndex;
    }

    @Override
    public String toString() {
//        return "(" + row + ", " + column + ") = " + "(" + production + ", " + productionIndex + ")";
        StringBuilder productionStr = new StringBuilder();
        for (String symbol : production) {
            productionStr.append(symbol);
        }

        return row + " | " + column + " | " + productionStr + " | " + productionIndex;
    }
}
