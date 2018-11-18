package com.nineplusten.app.service;

import java.util.List;
import java.util.ArrayList;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.util.RestDbIO;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class LoadUserDataService extends Service<List<String[]>> {

    @Override
    protected Task<List<String[]>> createTask() {
        return new Task<List<String[]>>() {
            @Override
            protected List<String[]> call() throws Exception {
              // loads a user from the database
              return loadUser();
            }
        };
    }

   public List<String[]> loadUser(){
    JSONArray userArray = null;
        try {
            userArray = RestDbIO.get("/users");
        } catch (UnirestException e){
            e.printStackTrace();
        }

        return parseUserArray(userArray);
    }

    private List<String[]> parseUserArray(JSONArray jsonArray){

        List<String[]> users = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String[] temp = new String[3];
            temp[0] = jsonObject.optString("user_id");
            temp[1] = jsonObject.optString("email");
            temp[2] = extractRole(jsonObject.optString("user_role"));

            users.add(temp);
        }

        return users;
    }

    private String extractRole(String s){
        //TODO: replace with proper way to extract the role name
        s = s.replaceAll("\\[", "");
        s = s.replaceAll("\\]", "");
        s = s.replaceAll("\\{", "");
        s = s.replaceAll("\\}", "");
        s = s.replaceAll("\"", "");
        s = s.split(",")[0];
        s = s.split(":")[1];
        return s;
    }
}