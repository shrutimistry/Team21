package com.nineplusten.app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {
  private Stage primaryStage;
  private BorderPane rootLayout;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Immigration Service Analytics");

    initRootLayout();
    
    showLoginView();

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

  public void showLoginView() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("view/LoginView.fxml"));
      AnchorPane loginView = (AnchorPane) loader.load();

      rootLayout.setCenter(loginView);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
