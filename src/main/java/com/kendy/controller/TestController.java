package com.kendy.controller;


import java.io.IOException;
import java.time.LocalDate;
import org.apache.log4j.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.kendy.util.ShowUtil;
import application.Constants;
import application.Main;
import application.MyController;
import com.jfoenix.controls.JFXNodesList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class TestController extends Application {
  
  private static Logger log = Logger.getLogger(TestController.class);



    public static void main(String[] args) {
    }

    @Override
    public void start(Stage stage) throws IOException {
    	log.info("开始启动程序...");
//        JFXButton button1 = new JFXButton();
//        Label label = new Label("翻查");
//        button1.setGraphic(label);
//        label.setStyle(FX_TEXT_FILL_WHITE);
//        button1.setButtonType(ButtonType.RAISED);
//        button1.getStyleClass().add(ANIMATED_OPTION_BUTTON);
//
//        JFXNodesList nodesList = new JFXNodesList();
//        nodesList.addAnimatedNode(button1);
//        nodesList.setSpacing(10);
//        nodesList.setAlignment(Pos.CENTER_LEFT);
//        
//        LocalDate localDate = LocalDate.now();
//        for(int i=0; i<20; i++) {
//          Button sbutton = new Button(localDate.minusDays(i).toString().replace("2018-", ""));
//          sbutton.setPrefWidth(100);
////          sbutton.setStyle("-fx-font-size: 23px");
////          sbutton.setButtonType(ButtonType.RAISED);
////          sbutton.getStyleClass().addAll(ANIMATED_OPTION_BUTTON, ANIMATED_OPTION_SUB_BUTTON);
//          sbutton.setOnAction(e->{
//            ShowUtil.show(sbutton.getText(), 1);
//          });
//          nodesList.addAnimatedNode(sbutton);
//        }
//       // nodesList.setRotate(-90);
//
//        ScrollPane main = new ScrollPane();
//        main.setPrefWidth(200);
//        main.setPrefHeight(200d);
//        main.setPadding(new Insets(10));
//
//        main.setContent(nodesList);//.add(nodesList);
//
//        Scene scene = new Scene(main, 1200, 600);
//        scene.getStylesheets().add(TestController.class.getResource("/css/jfoenix-components.css").toExternalForm());
//        stage.setScene(scene);
//
//        stage.show();
        
        //**********************************************************
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/com/kendy/dialog/bank_flow_frame1.fxml").openStream());
//      Pane p = fxmlLoader.load(getClass().getResource("MainStage4.fxml").openStream());
//      Parent root = p.getParent()
        BankFlowController controller = (BankFlowController) fxmlLoader.getController();
        
        
        try {
          stage.getIcons().add(new Image("file:resources/icon.png"));
        } catch (Exception e) {
            log.error("找不到icon图标！");
        }
        stage.setTitle(Constants.TITLE + Constants.VERSION);
        stage.setScene(new Scene(root));
        //primaryStage.setResizable(false); 
        stage.show();
        
    }
}
