package worker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    ADD_TASK("add task (?<id>\\S+)"), REMOVE_TASK("remove task (?<id>\\S+)")
    , GET_CAP("get capacity");

    private Pattern pattern;

    Commands(String s) {
        this.pattern = Pattern.compile(s);
    }

    public Matcher getMatcher(String input) {
        return pattern.matcher(input);
    }
}
