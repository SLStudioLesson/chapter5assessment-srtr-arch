package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskDataAccess {

    private final String filePath;

    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * 
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader((filePath)))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] task = line.split(",");
                int userCode = Integer.parseInt(task[3]);
                User user = userDataAccess.findByCode(userCode);
                tasks.add(new Task(Integer.parseInt(task[0]), task[1], Integer.parseInt(task[2]), user));
            }
            // Code,Name,Status,Rep_User_Code
            // 1,taskA,0,1

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * タスクをCSVに保存します。
     * 
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(filePath, true)))) {
            writer.newLine();
            String line = createLine(task);
            writer.write(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを1件取得します。
     * 
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    public Task findByCode(int code) {
        Task task = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tasks = line.split(",");
                if (Integer.parseInt(tasks[0]) == code) {
                    int userCode = Integer.parseInt(tasks[3]);
                    User user = userDataAccess.findByCode(userCode);
                    task = new Task(Integer.parseInt(tasks[0]), tasks[1], Integer.parseInt(tasks[2]), user);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * タスクデータを更新します。
     * 
     * @param updateTask 更新するタスク
     */
    public void update(Task updateTask) {
        List<Task> tasks = findAll();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Code,Name,Status,Rep_User_Code");
            for (Task task : tasks) {
                String line;
                line = createLine(task);
                if (task.getCode() == updateTask.getCode()) {
                    line = createLine(updateTask);
                }
                writer.newLine();
                writer.write(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを削除します。
     * 
     * @param code 削除するタスクのコード
     */
    // public void delete(int code) {
    // try () {

    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * 
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    private String createLine(Task task) {
        return task.getCode() + "," + task.getName() + "," + task.getStatus() + "," + task.getRepUser().getCode();
    }
}