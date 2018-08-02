package com.kendy.rxjava;

import org.junit.Test;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * rxjava测试
 * 
 * @author linzt
 * @time 2018年8月1日
 */
public class RxjavaTest {
  
  
  @Test
  public void test1() {
    Flowable.range(1, 10)
//    .parallel()
//    .runOn(Schedulers.computation())
    .map(v -> v * v)
//    .sequential()
//    .blockingSubscribe(System.out::println);
    .subscribe(System.out::println);

  }

}
