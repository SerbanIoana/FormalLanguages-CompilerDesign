import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Grammar grammar = new Grammar("g5.txt");
    private static final LL1Parser parser = new LL1Parser(grammar);

    public static void main(String[] args) {
        //runGrammarMenu();
        runParserMenu();
    }

    private static void printGrammarMenu() {
        System.out.println("\n_____Menu_____");
        System.out.println("1. Display the set of nonterminals");
        System.out.println("2. Display the start symbol");
        System.out.println("3. Display the set of terminals");
        System.out.println("4. Display the set of productions");
        System.out.println("5. Display productions for a given nonterminal");
        System.out.println("6. Check if grammar is context-free");
        System.out.println("0. EXIT");
        System.out.print(">>>");
    }

    private static void printParserMenu() {
        System.out.println("\n_____Menu_____");
        System.out.println("1. Display FIRST");
        System.out.println("2. Display FOLLOW");
        System.out.println("0. EXIT");
        System.out.print(">>>");
    }

    private static void runGrammarMenu() {
        Scanner input = new Scanner(System.in);

        while (true) {
            printGrammarMenu();
            try {
                int command = input.nextInt();
                switch (command) {
                    case 0:
                        System.exit(0);
                    case 1:
                        printNonterminals();
                        break;
                    case 2:
                        printStartSymbol();
                        break;
                    case 3:
                        printTerminals();
                        break;
                    case 4:
                        printAllProductions();
                        break;
                    case 5:
                        printProductionsForNonterminal();
                        break;
                    case 6:
                        checkCFG();
                        break;
                    default:
                        System.out.println("Invalid Command");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private static void runParserMenu() {
        Scanner input = new Scanner(System.in);

        while (true) {
            printParserMenu();
            try {
                int command = input.nextInt();
                switch (command) {
                    case 0:
                        System.exit(0);
                    case 1:
                        printFirst();
                        break;
                    case 2:
                        printFollow();
                        break;
                    default:
                        System.out.println("Invalid Command");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private static void printFollow() {
        StringBuilder followStr = new StringBuilder("FOLLOW set\n");
        for (Map.Entry<String, List<String>> followEntry : parser.getFollow().entrySet()) {
            String symbol = followEntry.getKey();
            List<String> symbolFollow = followEntry.getValue();
            followStr.append("FOLLOW(").append(symbol).append(") = {");

            for (String terminal : symbolFollow) {
                followStr.append(terminal).append(", ");
            }
            followStr.delete(followStr.length() - 2, followStr.length());
            followStr.append("}\n");
        }
        System.out.println(followStr);
    }

    private static void printFirst() {
        StringBuilder firstStr = new StringBuilder("FIRST set\n");
        for (Map.Entry<String, List<String>> firstEntry: parser.getFirst().entrySet()) {
            String symbol = firstEntry.getKey();
            List<String> symbolFirsts = firstEntry.getValue();
            firstStr.append("FIRST(").append(symbol).append(") = {");

            for (String terminal : symbolFirsts) {
                firstStr.append(terminal).append(", ");
            }
            firstStr.delete(firstStr.length() - 2, firstStr.length());
            firstStr.append("}\n");
        }
        System.out.println(firstStr);
    }

    private static void printNonterminals() {
        StringBuilder nonterminalsStr = new StringBuilder("N = {");
        for (String nonterminal : grammar.getNonTerminals()) {
            nonterminalsStr.append(nonterminal).append(", ");
        }
        nonterminalsStr.delete(nonterminalsStr.length() - 2, nonterminalsStr.length());
        nonterminalsStr.append("}");
        System.out.println(nonterminalsStr);
    }

    private static void printStartSymbol() {
        System.out.println("S = " + grammar.getStartSymbol());
    }

    private static void printTerminals() {
        StringBuilder terminalsStr = new StringBuilder("T = {");
        for (String terminal : grammar.getTerminals()) {
            terminalsStr.append(terminal).append(", ");
        }
        terminalsStr.delete(terminalsStr.length() - 2, terminalsStr.length());
        terminalsStr.append("}");
        System.out.println(terminalsStr);
    }

    private static void printAllProductions() {
        StringBuilder productionsStr = new StringBuilder("P = {\n");
        for (Production production : grammar.getProductions()) {
            productionsStr.append(production.toString()).append("\n");
        }
        productionsStr.delete(productionsStr.length() - 2, productionsStr.length());
        productionsStr.append("\n}");
        System.out.println(productionsStr);
    }

    private static void printProductionsForNonterminal() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">>nonterminal: ");
        try {
            String nonterminal = bufferedReader.readLine();
            List<Production> productions = grammar.getProductionsForNonterminal(nonterminal);

            if (productions == null)
                System.out.println("Inexistent nonterminal!");
            else {
                StringBuilder productionsStr = new StringBuilder("Productions for nonterminal ");
                productionsStr.append(nonterminal).append(" = {\n");
                for (Production production : productions) {
                    productionsStr.append(production.toString()).append(",\n");
                }
                productionsStr.delete(productionsStr.length() - 2, productionsStr.length());
                productionsStr.append("\n}");
                System.out.println(productionsStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkCFG() {
        boolean accepted = grammar.checkContextFree();
        if (accepted)
            System.out.println("The given grammar is context-free");
        else
            System.out.println("The given grammar is NOT context-free");
    }
}
