public class SymbolTableTests {

    public static void test(){
        SymbolTable symbolTable = new SymbolTable();
        String val1 = "ab";
        String val2 = "cab";
        String val3 = "ba";
        String val4 = "asd";

        //collision between val1 and val3 - resolved
        int pos1 = symbolTable.put("ab");
        System.out.println(pos1 + ": " + val1);
        int pos2 = symbolTable.put("cab");
        System.out.println(pos2 + ": " + val2);
        int pos3 = symbolTable.put("ba");
        System.out.println(pos3 + ": " + val3);
        int pos4 = symbolTable.put("asd");
        System.out.println(pos4 + ": " + val4);
    }

    public static void main(String[] args) {
        test();
    }
}

