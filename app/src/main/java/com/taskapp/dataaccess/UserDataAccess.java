package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.taskapp.model.User;

public class UserDataAccess {
    private final String filePath;

    public UserDataAccess() {
        filePath = "app/src/main/resources/users.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public UserDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * メールアドレスとパスワードを基にユーザーデータを探します。
     * @param email メールアドレス
     * @param password パスワード
     * @return 見つかったユーザー
     */
    public User findByEmailAndPassword(String email, String password) {
        User user = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                String[]users = line.split(",");
                if(!(email.equals(users[2]))){continue;}
                if(password.equals(users[3])){
                    user = new User(Integer.parseInt(users[0]),users[1],users[2],users[3]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
        return user;
     }

    /**
     * コードを基にユーザーデータを取得します。
     * @param code 取得するユーザーのコード
     * @return 見つかったユーザー
     */
    public User findByCode(int code) {
        User user = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                String[]users = line.split(",");
                if(code==Integer.parseInt(users[0])){
                user = new User (code,users[1],users[2],users[3]);    
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}
