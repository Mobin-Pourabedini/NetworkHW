package master;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    CREATE_TASK("k create task (?<input>(--(?<var>[^=]+)=(?<val>[^\\s]+)\\s*)+)"),
    SHOW_TASKS("k get tasks"),
    SHOW_WORKERS("k get nodes"),
    DELETE_TASK("k create delete task (?<input>--(?<var>[^=]+)=(?<val>[^\\s]+)\\s*)+"),
    ;
    private Pattern pattern;

    Commands(String s) {
        this.pattern = Pattern.compile(s);
    }

    public Matcher getMatcher(String input) {
        return this.pattern.matcher(input);
    }
}
