package kendy.test;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXSpinner;
//import demos.MainDemo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;

public class SpinnerTest extends Application {

  @Override
  public void start(final Stage stage) throws Exception {

    MaskerPane maskerPane = new MaskerPane();

    StackPane pane = new StackPane();

    JFXSpinner root = new JFXSpinner(0.9);

    pane.getChildren().add(root);

    final Scene scene = new Scene(pane, 300, 300);
//    scene.getStylesheets()
//        .add(MainDemo.class.getResource("/css/jfoenix-components.css").toExternalForm());
    stage.setScene(scene);
    stage.setTitle("JFX Spinner Demo");
    stage.show();
    for (int i = 0; i < 10; i++) {
      root.setRadius(i*0.1);
      Thread.sleep(1000);
    }
  }

  public static void main(final String[] arguments) {
    Application.launch(arguments);
  }
}