package com.kendy.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.alibaba.fastjson.JSON;

public class LampdaTest {

  public static void main(String[] args) {
    A a = new A("a", 20);
    A b = new A("b", 25);
    A c = new A("c", 30);
    List<A> list = Arrays.asList(a, b, c);
    list.forEach(e -> System.out.println(JSON.toJSONString(e)));

    list.stream().forEach(e -> e.setAge(55));
    list.forEach(e -> System.out.println(JSON.toJSONString(e)));

  }

  private static class A {

    private String name;
    private int age;

    public A(String name, int age) {
      super();
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }
}
