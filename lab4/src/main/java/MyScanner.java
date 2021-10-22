import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyScanner {
    private SymbolTable symbolTable;
    private HashMap<String, Integer> PIF;   //(token, ST_pos)
    private List<String> operators;
    private List<String> separators;
    private List<String> reservedWords;

    public MyScanner() {
        this.symbolTable = new SymbolTable();
        this.PIF = new HashMap<>();
        this.operators = new ArrayList<>();
        this.separators = new ArrayList<>();
        this.reservedWords = new ArrayList<>();
        getLanguageTokens();
    }

    public void getLanguageTokens() {
        Path path = Paths.get("tokens.in");
        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(" "));
                String keyword = items.get(0);
                String tokens = items.get(1);

                if(keyword.equals("operators"))
                    this.operators = Arrays.asList(tokens.split(","));
                if(keyword.equals("separators")){
                    this.separators = Arrays.asList(tokens.split(","));
                }
                if(keyword.equals("reserved"))
                    this.reservedWords = Arrays.asList(tokens.split(","));

                System.out.println(operators);
                System.out.println(separators);
                System.out.println(reservedWords);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        this.separators.add(",");
    }
    
    public void scan(String filepath) {
        Path path = Paths.get(filepath);
        try {
            Files.lines(path).forEach(line -> {
                //todo parse line character by character

                //use string tokenizer with boolean flag to keep the separator/operator by which you do the splitting

            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
