import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class MyScannerException extends Exception {
    public MyScannerException(String message) {
        super(message);
    }
}

class Pair<T1, T2> {
    private final T1 key;
    private final T2 value;

    public Pair(T1 key, T2 value) {
        this.key = key;
        this.value = value;
    }

    public T1 getKey() {
        return key;
    }

    public T2 getValue() {
        return value;
    }
}

public class MyScanner {
    private SymbolTable symbolTable;
    private Classifier classifier;
    private List<Pair<String, Integer>> PIF;   //(token, ST_pos)
    private List<String> operators;
    private List<String> separators;
    private List<String> reservedWords;

    public MyScanner() {
        this.symbolTable = new SymbolTable();
        this.PIF = new ArrayList<>();
        this.operators = new ArrayList<>();
        this.separators = new ArrayList<>();
        this.reservedWords = new ArrayList<>();
        getLanguageTokens();
        this.classifier = new Classifier(operators, separators, reservedWords);
    }

    public void getLanguageTokens() {
        Path path = Paths.get("tokens.in");
        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(" "));
                String keyword = items.get(0);
                String tokens = items.get(1);

                if (keyword.equals("operators"))
                    this.operators = new ArrayList<String>(Arrays.asList(tokens.split(",")));
                if (keyword.equals("separators")) {
                    this.separators = new ArrayList<String>(Arrays.asList(tokens.split(",")));
                    this.separators.add(" ");
                    this.separators.add(",");
                }
                if (keyword.equals("reserved"))
                    this.reservedWords = new ArrayList<String>(Arrays.asList(tokens.split(",")));
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void scan(String filepath) {
        String separatorsString = String.join("", separators);
        separatorsString += "\"'";
        String operatorsString = String.join("", operators);
        String delimiters = separatorsString + operatorsString;

        Path path = Paths.get(filepath);
        try {
            Files.lines(path).forEach(line -> scanLine(line, delimiters));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            exportToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportToFile() throws IOException {
        FileWriter PIFwriter = new FileWriter("PIF.out");
        FileWriter SymbolTableWriter = new FileWriter("ST.out");

        StringBuilder PIFstr = new StringBuilder();
        PIFstr.append("TOKEN | ST_POS\n\n");
        for (Pair<String, Integer> pair : PIF) {
            String newStr = pair.getKey() + " -> " + pair.getValue() + "\n";
            PIFstr.append(newStr);
        }

        PIFwriter.write(PIFstr.toString());
        PIFwriter.close();
        SymbolTableWriter.write(symbolTable.toString());
        SymbolTableWriter.close();
    }

    private void scanLine(String line, String delimiters) {
        List<String> items = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(line, delimiters, true);

        while (tokenizer.hasMoreElements()) {
            String item = tokenizer.nextToken();
            if (!item.equals(" ")) {
                items.add(item);
            }
        }

        int i = 0;
        while (i < items.size()) {
            String token = items.get(i);

            //look-ahead for strings
            if (token.equals("\"")) {
                if (i < items.size()) {
                    String nextToken = items.get(++i);
                    while (!nextToken.equals("\"") && i < items.size()) {
                        token += nextToken;
                        nextToken = items.get(++i);
                    }
                    token += nextToken;
                }
            }

            //look ahead for chars
            if (token.equals("'")) {
                if (i < items.size()) {
                    String nextToken = items.get(++i);
                    while (!nextToken.equals("'") && i < items.size()) {
                        token += nextToken;
                        nextToken = items.get(++i);
                    }
                    token += nextToken;
                }
            }

            //look-ahead for operators ==,!=,<=,>=,++,--,+=,-=,&&,||
            if (token.equals("=") || token.equals("!") || token.equals("<") || token.equals(">") || token.equals("+") || token.equals("-") || token.equals("&") || token.equals("|")) {
                String nextToken = items.get(++i);
                if (nextToken.equals("=") || nextToken.equals("+") || nextToken.equals("-") || nextToken.equals("&") || nextToken.equals("|"))
                    token += nextToken;
                else
                    i--;
            }

            //look-ahead for negative integers
            if (token.equals("-")) {
                int j = i - 1;
                String nextToken = items.get(++i);
                try {
                    if (classifier.classify(nextToken) == 2) {
                        String previousToken = items.get(j);
                        if (classifier.classify(previousToken) == 0) {
                            token += nextToken;
                        }
                        else
                            i--;
                    }
                    else
                        i--;
                } catch (MyScannerException e) {
                    System.out.println("Error! " + e.getMessage());
                }
            }

            try {
                int type = classifier.classify(token);

                if (type == 0)
                    //operator, separator,reservedWord type
                    PIF.add(new Pair<>(token, -1));
                else if (type == 1) {
                    //identifier type
                    int index = symbolTable.put(token);
                    PIF.add(new Pair<>("id", index));
                } else if (type == 2) {
                    //const type
                    int index = symbolTable.put(token);
                    PIF.add(new Pair<>("const", index));
                }
            } catch (MyScannerException e) {
                System.out.println("Error! " + e.getMessage());
            }
            i++;
        }
    }
}
