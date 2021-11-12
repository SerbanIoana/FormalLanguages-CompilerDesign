import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    private static final FiniteAutomaton finiteAutomaton = new FiniteAutomaton();

    public static void main(String[] args) {
        runFAMenu();
    }

    private static void printMenu() {
        System.out.println("\n_____Menu_____");
        System.out.println("1. Display the set of states");
        System.out.println("2. Display the alphabet");
        System.out.println("3. Display the initial state");
        System.out.println("4. Display the set of final states");
        System.out.println("5. Display the transitions");
        System.out.println("6. Verify if FA accepts sequence");
        System.out.println("0. EXIT");
        System.out.print(">>>");
    }

    private static void runFAMenu() {
        Scanner input = new Scanner(System.in);

        while (true) {
            printMenu();
            try {
                int command = input.nextInt();
                switch (command) {
                    case 0:
                        System.exit(0);
                    case 1:
                        printAllStates();
                        break;
                    case 2:
                        printAlphabet();
                        break;
                    case 3:
                        printInitialState();
                        break;
                    case 4:
                        printFinalStates();
                        break;
                    case 5:
                        printTransitions();
                        break;
                    case 6:
                        verifyFASequence();
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

    private static void verifyFASequence() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">>sequence: ");
        try {
            String sequence = bufferedReader.readLine();
            boolean accepted = finiteAutomaton.verifySequence(sequence);
            if (accepted)
                System.out.println("Sequence accepted by FA");
            else
                System.out.println("Sequence NOT accepted by FA");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printAllStates() {
        StringBuilder allStatesStr = new StringBuilder("Q = {");
        for (String state : finiteAutomaton.getAllStates()) {
            allStatesStr.append(state).append(", ");
        }
        allStatesStr.delete(allStatesStr.length() - 2, allStatesStr.length());
        allStatesStr.append("}");
        System.out.println(allStatesStr);
    }

    private static void printAlphabet() {
        StringBuilder alphabetStr = new StringBuilder("E = {");
        for (String symbol : finiteAutomaton.getAlphabet()) {
            alphabetStr.append(symbol).append(", ");
        }
        alphabetStr.delete(alphabetStr.length() - 2, alphabetStr.length());
        alphabetStr.append("}");
        System.out.println(alphabetStr);
    }

    private static void printInitialState() {
        System.out.println("q0 = " + finiteAutomaton.getInitialState());
    }

    private static void printFinalStates() {
        StringBuilder finalStatesStr = new StringBuilder("F = {");
        for (String state : finiteAutomaton.getFinalStates()) {
            finalStatesStr.append(state).append(", ");
        }
        finalStatesStr.delete(finalStatesStr.length() - 2, finalStatesStr.length());
        finalStatesStr.append("}");
        System.out.println(finalStatesStr);
    }

    private static void printTransitions() {
        StringBuilder transitionsStr = new StringBuilder("S = {\n");
        for (Transition transition : finiteAutomaton.getTransitions()) {
            transitionsStr.append(transition.toString()).append(",\n");
        }
        transitionsStr.delete(transitionsStr.length() - 2, transitionsStr.length());
        transitionsStr.append("\n}");
        System.out.println(transitionsStr);
    }
}
