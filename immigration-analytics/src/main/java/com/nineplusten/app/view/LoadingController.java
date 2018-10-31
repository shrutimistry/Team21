package com.nineplusten.app.view;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.nineplusten.app.App;
import com.nineplusten.app.service.LoadingService;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;

public class LoadingController {
  @FXML
  private Label loadingText;
  @FXML
  private ProgressIndicator loadingIndicator;

  private App mainApp;
  private LoadingService loadingService;

  @FXML
  private void initialize() {
    loadingService = new LoadingService(new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create());
    loadingText.visibleProperty().bind(loadingService.runningProperty());
    loadingIndicator.visibleProperty().bind(loadingService.runningProperty());
  }

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }
  
  public void startLoading() {
    loadingService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        mainApp.showLoginView();
      }
    });
    loadingService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = loadingService.getException();
        throwable.printStackTrace();
      }
    });
    loadingService.start();
  }

}
