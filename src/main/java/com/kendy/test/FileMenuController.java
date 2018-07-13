package com.kendy.test;


import javafx.application.Platform;
import javafx.event.ActionEvent;

public class FileMenuController {

 public void exit(ActionEvent actionEvent) {
  Platform.exit();
 }
}