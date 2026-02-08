package ToDoList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {
    private File myNotes;

    public Manager(File myNotes){
        this.myNotes = myNotes;
    }

    public List<String> returnAllTasks(){
        List<String> data = new ArrayList<>();
        
        try (Scanner myReader = new Scanner(myNotes)){
            while (myReader.hasNextLine()){
                String line = myReader.nextLine();
                String[] split = line.split(",", 2);

                if (split.length > 0){
                    data.add(split[0] + ", " + split[1]);
                }
            }
        } catch (FileNotFoundException e){
            data.add("Error!");
        }

        return data;
    }

    public String returnTaskWithId(String id){
        String data = "Not present.";
        
        try (Scanner myReader = new Scanner(myNotes)){
            while (myReader.hasNextLine()){
                String line = myReader.nextLine();
                String[] split = line.split(",", 2);

                if (split[0].equals(id)){
                    data = (split[0] + ", " + split[1]);
                    break;
                }   
            }
        } catch (FileNotFoundException e){
            data = "err";
        }

        return data;
    }

    private int returnLastId(){
        List<String> data = this.returnAllTasks();
        if (data.isEmpty()){
            return -1;
        }else{
            String lastData = data.getLast();
            String[] split = lastData.split(",", 2);
            String lastIndex = split[0];
            return Integer.parseInt(lastIndex);
        }
    }

    public String recordTask(String task, Boolean appendStatus){
        try (FileWriter myWriter = new FileWriter(myNotes.getPath(), appendStatus);){
            int newIndex = returnLastId() + 1;
            
            if (myNotes.length() > 0) {
                myWriter.write(System.lineSeparator());
            }
            myWriter.write(newIndex + "," + task);

            return "Written success";
        } catch (IOException e){
            return "Failure writing";
        }
    }

    private String clearTasksFresh(List<String> data){
        try (FileWriter myWriter = new FileWriter(myNotes.getPath());){
            for (String task: data){
                myWriter.write(task);
            }
            return "Task gone";
        } catch (IOException e){
            return "Failure to remove";
        }
    }

    public String deleteTaskWithId(String id){
        List<String> data = new ArrayList<>();
        data = this.returnAllTasks();
        
        for (int i = 0; i < data.size(); i++){
            String line = data.get(i);
            String[] split = line.split(",", 2);

            if (split[0].equals(id)){
                data.remove(i);
                return clearTasksFresh(data);
            }
        }
        return "No such task exists";
    }
}

