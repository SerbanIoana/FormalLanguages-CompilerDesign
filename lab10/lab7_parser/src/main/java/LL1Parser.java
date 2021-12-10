import java.lang.reflect.Array;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

class MyParserException extends Exception {
    public MyParserException(String message) {
        super(message);
    }
}

public class LL1Parser {
    private final String EPSILON = "eps";
    private Grammar grammar;
    private Map<String, List<String>> first;
    private Map<String, List<String>> follow;
    private List<ParseTableCell> parseTable;
    private Stack<String> inputStack;
    private Stack<String> workingStack;
    private List<String> output;
    private List<String> parseTree;


    public LL1Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new LinkedHashMap<>();
        this.follow = new LinkedHashMap<>();
        constructFirst();
        constructFollow();
        this.parseTable = new ArrayList<>();
        constructParseTable();
        this.inputStack = new Stack<>();
        this.workingStack = new Stack<>();
        this.output = new ArrayList<>();
        this.parseTree = new ArrayList<>();
    }

    private void constructParseTree() {
        parseTree.clear();
        if (!(output.size() == 0)) {
            for (String prodIndex : output) {
                Production production = grammar.getProductions().get(Integer.parseInt(prodIndex));
                if (parseTree.size() == 0) {
                    parseTree.add(production.getNonTerminal());
                    parseTree.add(production.getRuleToString());
                }
                else {
                    List<String> productionPrev = Arrays.asList(parseTree.get(parseTree.size() - 1).split(" "));
                    StringBuilder currentItem = new StringBuilder();

                    boolean replaced = false;
                    for (String symbol : productionPrev) {
                        if (symbol.equals(production.getNonTerminal()) && !replaced) {
                            String rule = production.getRuleToString();
                            if (!rule.equals(EPSILON))
                                currentItem.append(rule).append(" ");
                            replaced = true;
                        }
                        else
                            currentItem.append(symbol).append(" ");
                    }
                    currentItem.delete(currentItem.length() - 1, currentItem.length());
                    parseTree.add(currentItem.toString());
                }
            }
        }
//        System.out.println(parseTree);
    }

    private void pushStrings(Stack<String> stack, List<String> sequence) {
        for (int i = sequence.size() - 1; i >= 0; i--) {
            stack.push(sequence.get(i));
        }
    }

    private void initStacks(List<String> sequence) {
        inputStack.clear();
        inputStack.push("$");
        pushStrings(inputStack, sequence);

        workingStack.clear();
        workingStack.push("$");
        workingStack.push(grammar.getStartSymbol());

        output.clear();
        output.add(EPSILON);
    }

    private ParseTableCell findParseTableCell(String inputHead, String workingHead) {
        for (ParseTableCell cell : parseTable) {
            if (cell.getRow().equals(workingHead) && cell.getColumn().equals(inputHead))
                return cell;
        }
        return null;
    }

    public boolean parse(String s) throws InterruptedException {
        List<String> sequence = new ArrayList<>(Arrays.asList(s.split(" ")));
        initStacks(sequence);

        boolean go = true;
        boolean result = true;

        while (go) {
            System.out.println("inputStack:" + inputStack);
            System.out.println("workingStack:" + workingStack);
            System.out.println("output:" + output);
            TimeUnit.SECONDS.sleep(5);

            String inputHead = inputStack.peek();
            String workingHead = workingStack.peek();

            ParseTableCell parseTableCell = findParseTableCell(inputHead, workingHead);

            if (parseTableCell == null) {
                go = false;
                result = false;
            }
            else {
                //($,$)
                if (parseTableCell.getProduction().get(0).equals("acc") && parseTableCell.getProductionIndex() == -2) {
                    go = false;
                    constructParseTree();
                    return result;
                }
                //(terminal, terminal)
                else if (parseTableCell.getProduction().get(0).equals("pop") && parseTableCell.getProductionIndex() == -1) {
                    workingStack.pop();
                    inputStack.pop();
                }
                //(nonterminal, terminal)
                else {
                    workingStack.pop();
                    List<String> production = parseTableCell.getProduction();
                    if (!production.get(0).equals(EPSILON))
                        pushStrings(workingStack, production);

                    if (output.size() == 1 && output.get(0).equals(EPSILON))
                        output.remove(0);
                    output.add(Integer.toString(parseTableCell.getProductionIndex()));
                }
            }
        }
        return result;
    }

    private void constructParseTable() {
        //add entry ($,$)=acc
        List<String> tableProduction = new ArrayList<>();
        tableProduction.add("acc");
        this.parseTable.add(new ParseTableCell("$", "$", tableProduction, -2));

        List<String> terminals = grammar.getTerminals();
        List<String> nonterminals = grammar.getNonTerminals();

        //add entries (terminal,terminal)=pop
        for (String terminal : terminals) {
            tableProduction = new ArrayList<>();
            tableProduction.add("pop");
            ParseTableCell parseTableCell = new ParseTableCell(terminal, terminal, tableProduction, -1);
            this.parseTable.add(parseTableCell);
        }

        //add entries for (nonterminal,terminal)=(production,productionIndex)
        try {
            for (String nonterminal : nonterminals) {
                for (Production production : grammar.getProductionsForNonterminal(nonterminal)) {
                    List<String> firstOfProduction = new ArrayList<>();
                    tableProduction = production.getRule();

                    if (tableProduction.get(0).equals(EPSILON))
                        firstOfProduction.add(EPSILON);
                    else
                        firstOfProduction = getFirstOfProduction(tableProduction, this.first);

                    int productionIndex = grammar.getProductions().indexOf(production);

                    if (!firstOfProduction.contains(EPSILON)) {
                        for (String terminal : firstOfProduction) {
                            if (!this.first.get(nonterminal).contains(terminal))
                                break;
                            ParseTableCell parseTableCell = new ParseTableCell(nonterminal, terminal, tableProduction, productionIndex);

                            for (ParseTableCell cell : parseTable) {
                                if (cell.getRow().equals(parseTableCell.getRow()) && cell.getColumn().equals(parseTableCell.getColumn()))
                                    throw new MyParserException("Grammar is not LL(1)!\n" + "Error at (" + cell.getRow() + ", " + cell.getColumn() + "): old value (" + cell.getProduction() + "), new value (" + parseTableCell.getProduction() + ")");
                            }

                            this.parseTable.add(parseTableCell);
                        }
                    }
                    else {
                        for (String terminal : this.follow.get(nonterminal)) {
                            ParseTableCell parseTableCell;
                            if (terminal.equals(EPSILON))
                                parseTableCell = new ParseTableCell(nonterminal, "$", tableProduction, productionIndex);
                            else
                                parseTableCell = new ParseTableCell(nonterminal, terminal, tableProduction, productionIndex);

                            for (ParseTableCell cell : parseTable) {
                                if (cell.getRow().equals(parseTableCell.getRow()) && cell.getColumn().equals(parseTableCell.getColumn()))
                                    throw new MyParserException("Grammar is not LL(1)!\n" + "Error at (" + cell.getRow() + ", " + cell.getColumn() + "): old value (" + cell.getProduction() + "), new value (" + parseTableCell.getProduction() + ")");
                            }

                            this.parseTable.add(parseTableCell);
                        }
                    }
                }
            }
        } catch (MyParserException e) {
            e.printStackTrace();
        }

        //System.out.println(parseTable);
    }

    private List<String> getFirstOfProduction(List<String> rule, Map<String, List<String>> first_map) {
        List<String> productionFirsts = new ArrayList();
        boolean epsilonFound = true;
        int i = 0;

        while (i < rule.size()) {
            String symbol = rule.get(i);
            List<String> symbolFirsts = first_map.get(symbol);

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

        return productionFirsts;
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
                            List<String> productionFirsts = getFirstOfProduction(rule, first_aux);

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
        //System.out.println(first_aux);
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

    public List<ParseTableCell> getParseTable() {
        return parseTable;
    }

    public Stack<String> getInputStack() {
        return inputStack;
    }

    public Stack<String> getWorkingStack() {
        return workingStack;
    }

    public List<String> getOutput() {
        return output;
    }

    public List<String> getParseTree() {
        return parseTree;
    }
}
