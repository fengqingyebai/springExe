package com.kendy.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApplication extends Application {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final SpringFxmlLoaders loader = new SpringFxmlLoaders();

  @Override
  public void start(Stage primaryStage) {

    logger.info("start method is starting... ");

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
