package kendy.test;

import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JFXCheckBoxTest extends Application {

  public int i = 0;
  public int step = 1;

  @Override
  public void start(Stage stage) {

    FlowPane main = new FlowPane();
    main.setVgap(20);
    main.setHgap(20);

    CheckBox cb = new CheckBox("CheckBox");
    JFXCheckBox jfxCheckBox = new JFXCheckBox("JFX CheckBox");
    JFXCheckBox customJFXCheckBox = new JFXCheckBox("JFX CheckBox");
    customJFXCheckBox.getStyleClass().add("custom-jfx-check-box");

    main.getChildren().add(cb);
    main.getChildren().add(jfxCheckBox);
    main.getChildren().add(customJFXCheckBox);

    StackPane pane = new StackPane();
    pane.getChildren().add(main);
    StackPane.setMargin(main, new Insets(100));
    pane.setStyle("-fx-background-color:WHITE");

    final Scene scene = new Scene(pane, 600, 200);
//    scene.getStylesheets().add(
//        JFXCheckBoxTest.class.getResource("/resources/css/jfoenix-components.css").toExternalForm());
    stage.setTitle("JFX CheckBox Demo ");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();

  }

  public static void main(String[] args) {
    launch(args);
  }

}