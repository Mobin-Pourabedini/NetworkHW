package master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterController {
    private List<Task> runningTasks = new ArrayList<>(), pendingTasks = new ArrayList<>();

    public Task getTaskByName(String name) {
        for (Task task: runningTasks) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        for (Task task: pendingTasks) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }

    public String addTask(Task task, String workerId) {
        Connection worker = Connection.getWorkerById(workerId);
        if (worker == null) {
            return "no such worker";
        }
        if (worker.getWorkerCapacity() > 0) {
            worker.setWorkerCapacity(worker.getWorkerCapacity() - 1);
            runningTasks.add(task);
            worker.send("add task " + task.getName());
            return "task added to worker " + workerId;
        } else {
            return "not enough capacity";
        }
    }

    public String addTask(Task task) {
        for (Connection worker: Connection.getWorkerConnections()) {
            if (worker.getWorkerCapacity() > 0) {
                worker.setWorkerCapacity(worker.getWorkerCapacity() - 1);
                runningTasks.add(task);
                worker.send("add task " + task.getName());
                return "task added to worker " + worker.getWorkerId();
            }
        }
        pendingTasks.add(task);
        return "task is pending ...";
    }

    public String createTask(Matcher matcher) {
        matcher.matches();
        String input = matcher.group("input");
        Matcher inputMatcher = Pattern.compile("--(?<var>[^=]+)=(?<val>[^\\s]+)\\s*").matcher(input);
        String var1, val1, var2, val2;
        if (!inputMatcher.find()) {
            return "please provide inputs";
        }
        var1 = inputMatcher.group("var");
        val1 = inputMatcher.group("val");
        if (!inputMatcher.find()) {
            if (!var1.equals("name")) {
                return "Invalid input";
            }
            if (getTaskByName(val1) != null) {
                return "Task already exists";
            }
            Task task = new Task(val1);
            return addTask(task);
        } else {
            var2 = inputMatcher.group("var");
            val2 = inputMatcher.group("val");
            String workerStr = var1.equals("node") ? val1 : val2;
            String taskStr = var1.equals("name") ? val1 : val2;
            if (getTaskByName(taskStr) != null) {
                return "Task already exists";
            }
            if ((!var1.equals("node") || !var2.equals("name")) && (!var1.equals("name") || !var2.equals("node"))) {
                return "Invalid input";
            }
            Task task = new Task(taskStr);
            return addTask(task, workerStr);
        }
    }

//    public String deleteTask(Matcher matcher) {
//        Task task = master.getTaskByName(matcher.group("name"));
//        if (task == null) {
//            return "no such task";
//        }
//        master.deleteTask(task);
//        return "Task deleted";
//    }

    public String showTasks() {
        StringBuilder sb = new StringBuilder();
        for (Task task: runningTasks) {
            if (task.isPending()) {
                sb.append(task.getName()).append(" is pending\n");
            } else {
                sb.append(task.getName()).append(" is running on ").append(task.getWorkerId()).append("\n");
            }
        }
        for (Task task: pendingTasks) {
            if (task.isPending()) {
                sb.append(task.getName()).append(" is pending\n");
            } else {
                sb.append(task.getName()).append(" is running on ").append(task.getWorkerId()).append("\n");
                // TODO set workerId for tasks
                // TODO set is pending for tasks
                // TODO handle multi line output
            }
        }
        return sb.toString();
    }

    public String showWorkers() {
        StringBuilder sb = new StringBuilder();
        for (Connection worker: Connection.getWorkerConnections()) {
            sb.append(worker.getWorkerId()).append( " and has ")
                    .append(worker.getWorkerCapacity()).append(" capacity left\n");
        }
        return sb.toString();
    }
}
