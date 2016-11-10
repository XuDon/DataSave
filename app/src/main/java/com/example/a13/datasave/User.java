package com.example.a13.datasave;

/**
 * Created by 13 on 2016/11/9.
 */

public class User {
    String userName;
    String userPassword;

    public void User(String userName,String userPassword){
        this.userName=userName;
        this.userPassword=userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public String toString() {
        return "UserName:"+userName+" UserPassword:"+userPassword;
    }
}
