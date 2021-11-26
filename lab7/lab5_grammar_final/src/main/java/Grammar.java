import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Grammar {
    private List<String> nonTerminals;
    private List<String> terminals;
    private String startSymbol;
    private List<Production> productions;


    public Grammar() {
        this.nonTerminals = new ArrayList<String>();
        this.terminals = new ArrayList<String>();
        this.startSymbol = "";
        this.productions = new ArrayList<Production>();

        getFromFile("g2.txt");
    }

    private void getFromFile(String filename) {
        try
        {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                List<String> items = Arrays.asList(line.split("( = )|( =)"));
                String keyword = items.get(0);

                if (keyword.equals("N")) {
                    String tokens = items.get(1);
                    this.nonTerminals = new ArrayList<String>(Arrays.asList(tokens.split(",")))
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                }
                if (keyword.equals("S")) {
                    String tokens = items.get(1);
                    this.startSymbol = Arrays.asList(tokens.split(",")).get(0);
                }
                if (keyword.equals("T")) {
                    String tokens = items.get(1);
                    this.terminals = new ArrayList<String>(Arrays.asList(tokens.split(",")))
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                }
                if (keyword.equals("P")) {
                    String nextLine;
                    while ((nextLine = br.readLine()) != null) {
                        List<String> production = new ArrayList<String>(Arrays.asList(nextLine.split("( := )")));
                        String nonterminal = production.get(0);
                        String rules = production.get(1);

                        List<String> rulesList = new ArrayList<String>(Arrays.asList(rules.split("( \\| )")));
                        for (String rule : rulesList) {
                            boolean foundProduction = false;
                            for (Production production1 : productions) {
                                if (production1.getNonTerminal().equals(nonterminal) && production1.getRuleToString().equals(rule)) {
                                    foundProduction = true;
                                    break;
                                }
                            }
                            if (!foundProduction) {
                                List<String> symbolsList = new ArrayList<String>(Arrays.asList(rule.split(" ")));
                                this.productions.add(new Production(nonterminal, symbolsList));
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(nonTerminals);
        System.out.println(terminals);
        System.out.println(startSymbol);
        System.out.println(productions);
    }

    public List<Production> getProductionsForNonterminal(String nonterminal) {
        if (this.nonTerminals.contains(nonterminal)){
            List<Production> productionsNonterminal = new ArrayList<>();
            for (Production production : this.productions) {
                if (production.getNonTerminal().equals(nonterminal))
                    productionsNonterminal.add(production);
            }
            return productionsNonterminal;
        }
        else
            return null;
    }

    public boolean checkContextFree() {
        for (Production production : this.productions) {
            String symbol = production.getNonTerminal();
            if (symbol.length() > 1 || !this.nonTerminals.contains(symbol))
                return false;
        }
        return true;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(List<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<String> terminals) {
        this.terminals = terminals;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }
}
