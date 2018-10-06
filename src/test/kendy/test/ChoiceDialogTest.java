package kendy.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class ChoiceDialogTest extends Application {

  // launch the application
  public void start(Stage stage) {
    // set title for the stage
    stage.setTitle("creating choice dialog");


    // create a tile pane
    TilePane tilePane = new TilePane();
    
    tilePane.setHgap(8);
    tilePane.setPrefColumns(4);
    for (int i = 0; i < 20; i++) {
      Button button = new Button(i+"");
      final int index = i;
      button.setOnAction(e->{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("多谢大家" + index);
        alert.setGraphic(new ImageView("images/ok.png"));
        alert.show();
      });
      
      
      tilePane.getChildren().add(button);
      
    }
    
    

    Button b = new Button("click");
    String days[] = {"联盟1", "联盟2", "联盟3"};
    ChoiceDialog<String> dialog = new ChoiceDialog<>(days[0], days);
    b.setOnAction(e->{
      dialog.show();
    });
    
    dialog.setHeaderText(null);
    dialog.setGraphic(null);

    // add button
    tilePane.getChildren().add(b);

    // create a scene
    Scene sc = new Scene(tilePane, 200, 200);

    // set the scene
    stage.setScene(sc);

    stage.show();
  }

  public static void main(String args[]) {
    // launch the application
    launch(args);
  }
}
