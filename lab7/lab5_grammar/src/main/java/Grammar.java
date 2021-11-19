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

        getFromFile("g1.in");
    }

    private void getFromFile(String filename) {
        Path path = Paths.get(filename);
        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split("="));
                String keyword = items.get(0);
                String tokens = items.get(1);

                if (keyword.equals("N")) {
                    this.nonTerminals = new ArrayList<String>(Arrays.asList(tokens.split(",")))
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                }
                if (keyword.equals("S")) {
                    this.startSymbol = Arrays.asList(tokens.split(",")).get(0);
                }
                if (keyword.equals("T")) {
                    this.terminals = new ArrayList<String>(Arrays.asList(tokens.split(",")))
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                }
                if (keyword.equals("P")) {
                    List<String> productionsList = new ArrayList<String>(Arrays.asList(tokens.split("(, )")));
                    for (String tuple : productionsList) {
                        tuple = tuple.substring(1, tuple.length() - 1);
                        List<String> productionComp = new ArrayList<>(Arrays.asList(tuple.split(",")));

                        boolean foundProduction = false;
                        for (Production transition : productions) {
                            if (transition.getNonTerminal().equals(productionComp.get(0)) && transition.getRule().equals(productionComp.get(1))) {
                                foundProduction = true;
                                break;
                            }
                        }
                        if (!foundProduction)
                            this.productions.add(new Production(productionComp.get(0), productionComp.get(1)));
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
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
