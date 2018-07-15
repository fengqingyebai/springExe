package com.kendy.test;

import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.kendy.application.Main;
import com.kendy.util.ErrorUtil;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

public class SpringFxmlLoaders {
  static {
    try {
      // 生产环境可用
      String logName = "log4j/log4j.properties";
      PropertyConfigurator.configure(Main.class.getClassLoader().getResourceAsStream(logName));
    } catch (Exception e) {
      ErrorUtil.err("日志组件初始化失败");
    }
  }
  
  
  private static final ApplicationContext applicationContext =
      new ClassPathXmlApplicationContext("spring/spring-service2.xml");

  public Object load(String url) {
    try (InputStream fxmlStream = SpringFxmlLoaders.class.getResourceAsStream(url)) {
      System.err.println(SpringFxmlLoaders.class.getResourceAsStream(url));
      FXMLLoader loader = new FXMLLoader();
      loader.setControllerFactory(new Callback<Class<?>, Object>() {
        @Override
        public Object call(Class<?> clazz) {
          return applicationContext.getBean(clazz);
        }
      });
      return loader.load(fxmlStream);
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }
  }
  
  
  public static ApplicationContext getContext() {
    return applicationContext;
  }
}
