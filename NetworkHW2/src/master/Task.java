package master;

public class Task {
    private boolean isPending;
    private String workerId;
    private String name;

    public Task(String name) {
        this.name = name;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getName() {
        return name;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isPending() {
        return isPending;
    }
}
