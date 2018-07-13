package com.kendy.test;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApplication extends Application {

  private static final SpringFxmlLoader loader = new SpringFxmlLoader();

  @Override
  public void start(Stage primaryStage) {
    Parent root = (Parent) loader.load("search.fxml");
    Scene scene = new Scene(root, 768, 480);
    primaryStage.setScene(scene);
    primaryStage.setTitle("JavaFX demo");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
