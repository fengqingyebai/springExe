package com.kendy.test;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JFXDialogTest extends Application {

  @Override
  public void start(Stage primaryStage) {
    JFXDialogLayout content = new JFXDialogLayout();
    content.setHeading(new Text("Error, No selection"));
    content.setBody(new Text("No student selected"));
    StackPane stackpane = new StackPane();
    JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton button = new JFXButton("Okay");
    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        dialog.close();
      }
    });
    content.setActions(button);

    Scene scene = new Scene(stackpane, 600, 650);
    primaryStage.setScene(scene);
    dialog.show();
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}