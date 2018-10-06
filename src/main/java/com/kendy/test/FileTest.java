package com.kendy.test;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

public class FileTest {

  public static void main(String[] args) throws IOException {

    System.out.println(StringUtils.rightPad("总流水：", 10, "W"));
    System.out.println(StringUtils.rightPad("总流水：6454", 10, "W"));
    System.out.println(StringUtils.rightPad("总流水：7857454", 10, "W"));
    System.out.println(StringUtils.rightPad("总流水：78574524", 10, "W"));

    System.out.println("finishes...");
  }

}
