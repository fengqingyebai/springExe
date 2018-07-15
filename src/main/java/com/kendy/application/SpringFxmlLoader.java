package com.kendy.application;

import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.kendy.util.ErrorUtil;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

public class SpringFxmlLoader {
  

  
  private static  ApplicationContext applicationContext =
      new ClassPathXmlApplicationContext("com/kendy/application/spring-service.xml");
//  new ClassPathXmlApplicationContext("/spring/spring-service.xml");
  
  static {
    try {
      // 生产环境可用
      String logName = "log4j/log4j.properties";
      PropertyConfigurator.configure(Main.class.getClassLoader().getResourceAsStream(logName));
    } catch (Exception e) {
      ErrorUtil.err("日志组件初始化失败");
    }
  }

  public Object load(String url) {
    try (InputStream fxmlStream = SpringFxmlLoader.class.getResourceAsStream(url)) {
      FXMLLoader loader = new FXMLLoader();
      loader.setControllerFactory(new Callback<Class<?>, Object>() {
        @Override
        public Object call(Class<?> clazz) {
          return applicationContext.getBean(clazz);
        }
      });
      return loader.load(fxmlStream);
    } catch (IOException ioException) {
      ErrorUtil.err("加载视图文件失败");
      throw new RuntimeException(ioException);
    }
  }
  public static ApplicationContext getContext() {
    return applicationContext;
  }
}
