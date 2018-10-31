package com.nineplusten.app.service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Agency;
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
        Type agencyType = new TypeToken<Collection<Agency>>(){}.getType();
        Cache.userRoles = gson.fromJson(RestDbIO.get("/user-roles").toString(), userRoleType);
        Cache.agencies = gson.fromJson(RestDbIO.get("/agencies").toString(), agencyType);
        
        // Sort for quick access of list elements via binary search
        Collections.sort(Cache.userRoles);
        Collections.sort(Cache.agencies);
        
        return null;
      }
    };
  }

}
