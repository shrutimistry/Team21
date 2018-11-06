package com.nineplusten.app.service;

import java.lang.reflect.Type;
import java.util.Collection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.UserRole;
import com.nineplusten.app.util.RestDbIO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadingService extends Service<Void> {
  
  private Gson gson;
  
  public LoadingService(Gson gson) {
    this.gson = gson;
  }

  @Override
  protected Task<Void> createTask() {
    return new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        Type userRoleType = new TypeToken<Collection<UserRole>>(){}.getType();
        Type templateType = new TypeToken<Collection<Template>>(){}.getType();
        Cache.userRoles = gson.fromJson(RestDbIO.get("/user-roles").toString(), userRoleType);
        Cache.templates = gson.fromJson(RestDbIO.get("/templates").toString(), templateType);
        
        return null;
      }
    };
  }

}
