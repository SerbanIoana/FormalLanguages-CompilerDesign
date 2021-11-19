public class Production {
    private String nonTerminal;
    private String rule;

    public Production(String nonTerminal, String rule) {
        this.nonTerminal = nonTerminal;
        this.rule = rule;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return nonTerminal + " -> " + rule;
    }
}
