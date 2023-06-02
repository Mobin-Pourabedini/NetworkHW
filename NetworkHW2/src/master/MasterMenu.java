package master;

import java.io.IOException;

public class MasterMenu extends Thread{
    private Connection clientConnection;
    private MasterController controller;

    public MasterMenu(Connection clientConnection) {
        this.clientConnection = clientConnection;
        this.controller = new MasterController();
    }

    @Override
    public void run() {
        try {
            String command = clientConnection.receive();
            String output;
            while (!command.equals("exit")) {
                if (Commands.CREATE_TASK.getMatcher(command).matches()) {
                    output = controller.createTask(Commands.CREATE_TASK.getMatcher(command));
                    clientConnection.send(output);
                }
                if (Commands.DELETE_TASK.getMatcher(command).matches()) {
//                    controller.deleteTask(Commands.DELETE_TASK.getMatcher(command));
//                    clientConnection.send(output);
                }
                if (Commands.SHOW_TASKS.getMatcher(command).matches()) {
                    output = controller.showTasks();
                    clientConnection.send(output);
                }
                if (Commands.SHOW_WORKERS.getMatcher(command).matches()) {
                    output = controller.showWorkers();
                    clientConnection.send(output);
                }
                command = clientConnection.receive();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
