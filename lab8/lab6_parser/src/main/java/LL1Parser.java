import java.util.*;

public class LL1Parser {
    private Grammar grammar;
    private Map<String, List<String>> first;
    private Map<String, List<String>> follow;
    private List<ParseTableCell> parseTable;
    private Stack<String> inputStack;
    private Stack<String> workingStack;
    private List<String> output;


    public LL1Parser(Grammar grammar) {
        this.grammar = grammar;
        constructFirst();
        constructFollow();
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
        this.parseTable = new ArrayList<>();
        this.inputStack = new Stack<>();
        this.workingStack = new Stack<>();
        this.output = new ArrayList<>();
    }

    public void constructFirst() {
        Map<String, List<String>> first_aux = new HashMap<>();
        for (String terminal : grammar.getTerminals()) {
            first_aux.put(terminal, new ArrayList<>());
            first_aux.get(terminal).add(terminal);
        }

        for (String nonTerminal : grammar.getNonTerminals()) {
            first_aux.put(nonTerminal, new ArrayList<>());
            List<Production> productionsNonterminal = grammar.getProductionsForNonterminal(nonTerminal);
            for (Production production : productionsNonterminal) {
                String firstSymbol = production.getRule().get(0);
                if (grammar.getTerminals().contains(firstSymbol))
                    first_aux.get(nonTerminal).add(firstSymbol);
            }
        }

        boolean doneFirst = false;
        Map<String, List<String>> first_current = new HashMap<>();

        while (!doneFirst) {
            for (String nonTerminal : grammar.getNonTerminals()) {
                for (Production production : grammar.getProductionsForNonterminal(nonTerminal)) {
                    List<String> rule = production.getRule();
                    if(!grammar.getTerminals().contains(rule.get(0))) {
                        for (String symbol : rule) {

                        }
                    }

                }
            }
        }

    }

    public void constructFollow() {

    }

}
