import java.util.*;

public class LL1Parser {
    private final String EPSILON = "eps";
    private Grammar grammar;
    private Map<String, List<String>> first;
    private Map<String, List<String>> follow;
//    private List<ParseTableCell> parseTable;
//    private Stack<String> inputStack;
//    private Stack<String> workingStack;
//    private List<String> output;


    public LL1Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new LinkedHashMap<>();
        this.follow = new LinkedHashMap<>();
        constructFirst();
        constructFollow();
//        this.parseTable = new ArrayList<>();
//        this.inputStack = new Stack<>();
//        this.workingStack = new Stack<>();
//        this.output = new ArrayList<>();
    }

    public void constructFirst() {
        //init terminals f0
        Map<String, List<String>> first_aux = new LinkedHashMap<>();
        for (String terminal : grammar.getTerminals()) {
            first_aux.put(terminal, new ArrayList<>());
            first_aux.get(terminal).add(terminal);
        }

        //init nonterminals f0
        for (String nonTerminal : grammar.getNonTerminals()) {
            first_aux.put(nonTerminal, new ArrayList<>());
            List<Production> productionsNonterminal = grammar.getProductionsForNonterminal(nonTerminal);
            for (Production production : productionsNonterminal) {
                String firstSymbol = production.getRule().get(0);
                if (firstSymbol.equals(EPSILON) || grammar.getTerminals().contains(firstSymbol) && !first_aux.get(nonTerminal).contains(firstSymbol))
                    first_aux.get(nonTerminal).add(firstSymbol);
            }
        }

        boolean doneFirst = false;
        Map<String, List<String>> first_current = new LinkedHashMap<>();

        //starting with f1
        while (!doneFirst) {
            for (String terminal : grammar.getTerminals()) {
                first_current.put(terminal, new ArrayList<>());
                first_current.get(terminal).addAll(first_aux.get(terminal));
            }

            for (String nonTerminal : grammar.getNonTerminals()) {
                //copy all previous
                first_current.put(nonTerminal, new ArrayList<>());
                first_current.get(nonTerminal).addAll(first_aux.get(nonTerminal));

                for (Production production : grammar.getProductionsForNonterminal(nonTerminal)) {
                    List<String> rule = production.getRule();

                    //continue only if rule doesn't start with terminal
                    if(!grammar.getTerminals().contains(rule.get(0)) && !rule.get(0).equals(EPSILON)) {
                        //check if first is previously computed for all symbols of rule
                        boolean canCompute = true;
                        for (String symbol : rule) {
                            List<String> symbolFirsts = first_aux.get(symbol);
                            if (symbolFirsts.isEmpty()) {
                                canCompute = false;
                                break;
                            }
                        }

                        if (canCompute) {
                            List<String> productionFirsts = new ArrayList();
                            boolean epsilonFound = true;
                            int i = 0;

                            while (i < rule.size()) {
                                String symbol = rule.get(i);
                                List<String> symbolFirsts = first_aux.get(symbol);

                                productionFirsts.addAll(symbolFirsts);
                                if (!symbolFirsts.contains(EPSILON)) {
                                    epsilonFound = false;
                                    break;
                                }
                                productionFirsts.remove(EPSILON);
                                i++;
                            }

                            //if epsilon is in all firsts, add it
                            if (epsilonFound)
                                productionFirsts.add(EPSILON);

                            for (String terminal : productionFirsts) {
                                if (!first_current.get(nonTerminal).contains(terminal))
                                    first_current.get(nonTerminal).add(terminal);
                            }
                        }
                    }
                }
            }

            doneFirst = first_current.entrySet()
                    .stream()
                    .allMatch(e -> Arrays.equals(new List[]{e.getValue()}, new List[]{first_aux.get(e.getKey())}));

            for (String key : first_aux.keySet()) {
                first_aux.get(key).clear();
                first_aux.get(key).addAll(first_current.get(key));
            }
            first_current.clear();
        }
        this.first = first_aux;
    }

    public void constructFollow() {
        Map<String, List<String>> follow_aux = new LinkedHashMap<>();
        //init nonterminals l0
        for (String nonTerminal : grammar.getNonTerminals()) {
            follow_aux.put(nonTerminal, new ArrayList<>());
        }
        follow_aux.get(grammar.getStartSymbol()).add(EPSILON);

        boolean doneFollow = false;
        Map<String, List<String>> follow_current = new LinkedHashMap<>();

        //starting with l1
        while (!doneFollow) {
            for (String nonTerminal : grammar.getNonTerminals()) {
                follow_current.put(nonTerminal, new ArrayList<>());
                follow_current.get(nonTerminal).addAll(follow_aux.get(nonTerminal));

                for (Production production : grammar.getProductionsContainingNonterminal(nonTerminal)) {
                    List<String> rule = production.getRule();
                    String nonterminalStart = production.getNonTerminal();
                    List<String> productionFollow = new ArrayList();
                    boolean epsilonFound = true;
                    int i = rule.indexOf(nonTerminal) + 1;

                    while (i < rule.size()) {
                        String symbol = rule.get(i);
                        List<String> symbolFirsts = this.first.get(symbol);

                        productionFollow.addAll(symbolFirsts);
                        if (!symbolFirsts.contains(EPSILON)) {
                            epsilonFound = false;
                            break;
                        }
                        productionFollow.remove(EPSILON);
                        i++;
                    }

                    //if epsilon is in all firsts, add follow of the start nonterminal for production (from previous step)
                    if (epsilonFound)
                        productionFollow.addAll(follow_aux.get(nonterminalStart));

                    for (String terminal : productionFollow) {
                        if (!follow_current.get(nonTerminal).contains(terminal))
                            follow_current.get(nonTerminal).add(terminal);
                    }
                }
            }

            doneFollow = follow_current.entrySet()
                    .stream()
                    .allMatch(e -> Arrays.equals(new List[]{e.getValue()}, new List[]{follow_aux.get(e.getKey())}));

            for (String key : follow_aux.keySet()) {
                follow_aux.get(key).clear();
                follow_aux.get(key).addAll(follow_current.get(key));
            }
            follow_current.clear();
        }
        this.follow = follow_aux;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public Map<String, List<String>> getFirst() {
        return first;
    }

    public Map<String, List<String>> getFollow() {
        return follow;
    }
}
