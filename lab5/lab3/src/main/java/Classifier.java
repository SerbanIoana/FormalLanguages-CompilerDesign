import java.util.List;
import java.util.regex.Pattern;

public class Classifier {
    private final List<String> operators;
    private final List<String> separators;
    private final List<String> reservedWords;

    public Classifier(List<String> operators, List<String> separators, List<String> reservedWords) {
        this.operators = operators;
        this.separators = separators;
        this.reservedWords = reservedWords;
    }

    public int classify(String token) throws MyScannerException {
        if(operators.contains(token) || separators.contains(token) || reservedWords.contains(token))
            return 0;
        if(isIdentifier(token))
            return 1;
        if(isConstant(token))
            return 2;
        throw new MyScannerException("Token type doesn't exist! Token: " + token);
    }

    private boolean isIdentifier(String token) {
        String regex = "(^[a-zA-Z][a-zA-Z0-9_]*)";
        return Pattern.matches(regex, token);
    }

    private boolean isConstant(String token) {
        return isInteger(token) || isChar(token) || isString(token);
    }

    private boolean isInteger(String token) {
        String regex = "^(-?[1-9][0-9]*)|0$";
        return Pattern.matches(regex, token);
    }

    private boolean isChar(String token) {
        String regex = "'[a-zA-Z1-9]'";
        return Pattern.matches(regex, token);
    }

    private boolean isString(String token) {
        String regex = "\"([a-zA-Z0-9]|[^a-zA-Z0-9]|\s)+\"";
        return Pattern.matches(regex, token);
    }
}
