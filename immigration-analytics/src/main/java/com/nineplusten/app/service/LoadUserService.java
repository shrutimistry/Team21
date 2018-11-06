package com.nineplusten.app.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class LoadUserService extends Service<Void> {

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // loads a user from the database
              loadUserInstance();
              return null;
            }
        };
    }

   public void loadUserInstance(){

    }

}