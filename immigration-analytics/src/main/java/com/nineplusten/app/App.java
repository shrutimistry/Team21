package com.nineplusten.app;

import java.io.IOException;
import com.nineplusten.app.model.SessionModel;
import com.nineplusten.app.view.LandingController;
import com.nineplusten.app.view.LoadingController;
import com.nineplusten.app.view.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
  private Stage primaryStage;
  private BorderPane rootLayout;
  private SessionModel session;
  
  public App() {
    session = new SessionModel();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Immigration Service Analytics");

    initRootLayout();
    
    showLoadingSplash();

  }

  public void initRootLayout() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("view/RootLayout.fxml"));
      rootLayout = (BorderPane) loader.load();

      Scene scene = new Scene(rootLayout);
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void showLoadingSplash() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("view/LoadingSplash.fxml"));
      AnchorPane loadingView = (AnchorPane) loader.load();

      rootLayout.setCenter(loadingView);
      LoadingController controller = loader.getController();
      controller.setMainApp(this);
      controller.startLoading();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showLoginView() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("view/LoginView.fxml"));
      AnchorPane loginView = (AnchorPane) loader.load();

      rootLayout.setCenter(loginView);
      LoginController controller = loader.getController();
      controller.setMainApp(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void showLandingView() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("view/Landing.fxml"));
      AnchorPane landingView = (AnchorPane) loader.load();

      rootLayout.setCenter(landingView);
      
      LandingController controller = loader.getController();
      controller.setMainApp(this);
      controller.configureUserView();
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public SessionModel getSession() {
    return session;
  }
  
  public Stage getPrimaryStage() {
    return primaryStage;
  }
}
