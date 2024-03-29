/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.test.progress.serviceapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 进度条的正确使用方式
 *
 * @author linzt
 * @time 2018年7月4日 下午4:14:15
 */
public class ServiceApp extends Application {

  final GetDailySalesService service = new GetDailySalesService();

  public Parent createContent() {
    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(12));
    TableView tableView = new TableView();
    Button button = new Button("Refresh");
    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        service.restart();
      }
    });
    vbox.setPrefHeight(160);
    vbox.getChildren().addAll(tableView, button);

    Region veil = new Region();
    veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
    veil.setPrefSize(240, 160);
    ProgressIndicator p = new ProgressIndicator();
    p.setMaxSize(140, 140);

    //Define table columns
    TableColumn idCol = new TableColumn();
    idCol.setText("ID");
    idCol.setCellValueFactory(new PropertyValueFactory("dailySalesId"));
    idCol.setPrefWidth(32);
    tableView.getColumns().add(idCol);
    TableColumn qtyCol = new TableColumn();
    qtyCol.setText("Qty");
    qtyCol.setCellValueFactory(new PropertyValueFactory("quantity"));
    qtyCol.setPrefWidth(60);
    tableView.getColumns().add(qtyCol);
    TableColumn dateCol = new TableColumn();
    dateCol.setText("Date");
    dateCol.setCellValueFactory(new PropertyValueFactory("date"));
    dateCol.setMinWidth(240);
    tableView.getColumns().add(dateCol);

    p.progressProperty().bind(service.progressProperty());
    veil.visibleProperty().bind(service.runningProperty());
    p.visibleProperty().bind(service.runningProperty());
    tableView.itemsProperty().bind(service.valueProperty());
    tableView.setMinSize(240, 140);
    StackPane stack = new StackPane();
    stack.getChildren().addAll(vbox, veil, p);

    service.start();
    return stack;
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setScene(new Scene(createContent()));
    primaryStage.show();
  }

  /**
   * Java main for when running without JavaFX launcher
   */
  public static void main(String[] args) {
    launch(args);
  }
}
