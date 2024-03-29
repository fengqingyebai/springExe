/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.test.progress.serviceapp;

import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class GetDailySalesTask extends Task<ObservableList<DailySales>> {

  @Override
  protected ObservableList<DailySales> call() throws Exception {
    for (int i = 0; i < 500; i++) {
      updateProgress(i, 500);
      Thread.sleep(20);
    }
    ObservableList<DailySales> sales = FXCollections.observableArrayList();
    sales.add(new DailySales(1, 5000, new Date()));
    sales.add(new DailySales(2, 2473, new Date(0)));
    return sales;
  }
}
