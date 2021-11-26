import java.util.List;

public class Production {
    private String nonTerminal;
    private List<String> rule;

    public Production(String nonTerminal, List<String> rule) {
        this.nonTerminal = nonTerminal;
        this.rule = rule;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public List<String> getRule() {
        return rule;
    }

    public String getRuleToString() {
        StringBuilder ruleStr = new StringBuilder();
        for (String symbol : rule) {
            ruleStr.append(symbol).append(" ");
        }
        return ruleStr.toString();
    }

    public void setRule(List<String> rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return nonTerminal + " := " + getRuleToString();
    }
}
