import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class FiniteAutomaton {
    private List<String> allStates;
    private List<String> alphabet;
    private String initialState;
    private List<String> finalStates;
    private List<Transition> transitions;

    public FiniteAutomaton() {
        this.allStates = new ArrayList<String>();
        this.alphabet = new ArrayList<String>();
        this.initialState = "";
        this.finalStates = new ArrayList<String>();
        this.transitions = new ArrayList<Transition>();

        getFromFile("FA1.in");
    }

    private void getFromFile(String filename) {
        Path path = Paths.get(filename);
        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split("="));
                String keyword = items.get(0);
                String tokens = items.get(1);

                if (keyword.equals("Q")) {
                    this.allStates = new ArrayList<String>(Arrays.asList(tokens.split(",")))
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                }
                if (keyword.equals("E")) {
                    this.alphabet = new ArrayList<String>(Arrays.asList(tokens.split(",")))
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());;
                }
                if (keyword.equals("q0")) {
                    this.initialState = Arrays.asList(tokens.split(",")).get(0);
                }
                if (keyword.equals("F")) {
                    this.finalStates = new ArrayList<String>(Arrays.asList(tokens.split(",")))
                            .stream()
                            .distinct()
                            .collect(Collectors.toList());
                }
                if (keyword.equals("S")) {
                    List<String> transitionList = new ArrayList<String>(Arrays.asList(tokens.split("(, )")));
                    for (String tuple : transitionList) {
                        tuple = tuple.substring(1, tuple.length() - 1);
                        List<String> transitionComp = new ArrayList<>(Arrays.asList(tuple.split(",")));
                        boolean foundTransition = false;
                        for (Transition transition : transitions) {
                            if (transition.getState1().equals(transitionComp.get(0)) && transition.getRoute().equals(transitionComp.get(1))) {
                                foundTransition = true;
                                break;
                            }
                        }
                        if (!foundTransition)
                            this.transitions.add(new Transition(transitionComp.get(0), transitionComp.get(2), transitionComp.get(1)));
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean verifySequence(String sequence) {
        String currentState = this.initialState;
        boolean foundNext;
        for (char symbol : sequence.toCharArray()) {
            foundNext = false;
            for (Transition transition : this.transitions) {
                if (transition.getState1().equals(currentState) && transition.getRoute().equals(String.valueOf(symbol))) {
                    currentState = transition.getState2();
                    foundNext = true;
                    break;
                }
            }
            if (!foundNext)
                return false;
        }
        return this.finalStates.contains(currentState);
    }

    public List<String> getAllStates() {
        return allStates;
    }

    public void setAllStates(List<String> allStates) {
        this.allStates = allStates;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(List<String> finalStates) {
        this.finalStates = finalStates;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }
}
