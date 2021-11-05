import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.*;

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

        getFromFile();
    }

    private void getFromFile() {
        Path path = Paths.get("FA.in");
        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split("="));
                String keyword = items.get(0);
                String tokens = items.get(1);

                if (keyword.equals("Q")){
                    this.allStates = new ArrayList<String>(Arrays.asList(tokens.split(",")));
                }
                if (keyword.equals("E")){
                    this.alphabet = new ArrayList<String>(Arrays.asList(tokens.split(",")));
                }
                if (keyword.equals("q0")){
                    this.initialState = tokens;
                }
                if (keyword.equals("F")){
                    this.finalStates = new ArrayList<String>(Arrays.asList(tokens.split(",")));
                }
                if (keyword.equals("S")){
                    List<String> transitionList = new ArrayList<String>(Arrays.asList(tokens.split("(, )")));
                    for (String tuple : transitionList) {
                        List<String> transitionComp = new ArrayList<>(Arrays.asList(tuple.substring(1,6).split(",")));
                        this.transitions.add(new Transition(transitionComp.get(0), transitionComp.get(2), transitionComp.get(1)));
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(allStates);
        System.out.println(alphabet);
        System.out.println(initialState);
        System.out.println(finalStates);
        System.out.println(transitions);
    }
}
