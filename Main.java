import ToDoList.Manager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        String filePath = returnFilePathFromArg(args);
        Boolean fileSuccess = createFile(filePath);

        if (fileSuccess){
            Scanner sc = new Scanner(System.in);
            File myNotes = new File(filePath);
            Manager mngr = new Manager(myNotes);

            Boolean running = true;
            while (running) {
                System.out.print("> ");
                String cmd = sc.nextLine();
                if (cmd.trim().equalsIgnoreCase("quit")) running = false;

                String[] validCmd = validateCMD(cmd);
                String response = acceptInputAndCommand(validCmd, mngr, sc);
                System.out.println(response);
            }

            sc.close();
        }else{
            System.out.println("File writting error.");
        }
    }

    private static String returnFilePathFromArg(String[] args){
        if (args.length <= 0) return "myNotes.csv";

        String filePath = args[0];
        
        if (filePath == null){
            filePath = "myNotes";
        }

        if (!filePath.endsWith(".csv")){
            filePath += ".csv";
        }

        return filePath;
    }

    private static boolean createFile(String filePath){
        try {
            File targetFile = new File(filePath);
            targetFile.createNewFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static String[] validateCMD(String cmd){
        if (!cmd.isBlank() || !cmd.isEmpty()){
            String lowerCmd = cmd.toLowerCase();
            String[] includeArgs = lowerCmd.split("\\s+");
            return includeArgs;
        }
        return null;
    }

    private static String inputTask(Scanner sc){
        System.out.print("> ");
        String task = sc.nextLine();

        if (!task.isEmpty()){
            return task;
        }
        return null;
    }

    private static List<String> returnValidParameter(String[] cmds){
        if (cmds.length > 1){
            List<String> parameters = new ArrayList<>();

            for (int i = 1; i < cmds.length; i++){
                parameters.add(cmds[i]);
            }
            return parameters;
        }else{
            return null;
        }
    }

    private static String acceptInputAndCommand(String[] cmds, Manager mngr, Scanner sc){
        String cmd = cmds[0];
        List<String> para = returnValidParameter(cmds);

        if (cmd.contains("help")){
            return "quit = Quit app,\ntasks = Output all tasks,\n record = Start task record";
        }else if (cmd.contains("tasks")){
            List<String> data = new ArrayList<>();
            data = mngr.returnAllTasks();

            String tasks = "";
            for (String line: data){
                tasks += line + '\n';
            }

            return tasks;
        }else if (cmd.contains("printtask")){
            String id = para.get(0);
            String desiredTask = mngr.returnTaskWithId(id);
            return desiredTask;
        }else if (cmd.contains("record") && para != null){
            String task = String.join(" ", para.subList(0, para.size()));
            String response = mngr.recordTask(task, true);
            return response;
        }else if(cmd.contains("complete")){
            String id = para.get(0);
            String response = mngr.deleteTaskWithId(id);
            return response;
        }else{
            return "...";
        }
    }
}