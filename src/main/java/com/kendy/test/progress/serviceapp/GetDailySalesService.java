/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.test.progress.serviceapp;


import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 进度条的正确使用方式2 service与Application thread之间通过creatTask()进行联络，而Tast肯定要返回一个东西，哪怕这个东西是Void
 *
 * @author linzt
 * @time 2018年7月4日 下午4:14:33
 */
public class GetDailySalesService extends Service<ObservableList<DailySales>> {

  /**
   * Create and return the task for fetching the data. Note that this method is called on the
   * background thread (all other code in this application is on the JavaFX Application Thread!).
   *
   * @return A task
   */
  @Override
  protected Task createTask() {
    return new GetDailySalesTask();
  }
}
