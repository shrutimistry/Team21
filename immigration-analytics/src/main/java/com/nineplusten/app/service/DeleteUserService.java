package com.nineplusten.app.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.util.RestDbIO;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DeleteUserService extends Service<Void> {

    String userID;

    public DeleteUserService(String userID){
        this.userID = userID;
    }
    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                deleteUser();
                return null;
            }
        };
    }

    private void deleteUser() {
        try {
            RestDbIO.delete("/users", "user_id", userID);
        } catch (UnirestException e){
            e.printStackTrace();
        }
    }

}