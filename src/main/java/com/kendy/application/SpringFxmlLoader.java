package com.kendy.application;

import com.kendy.util.ErrorUtil;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 由SpringFxmlLoader代理FxmlLoader去加载FXML文件
 *
 * @author linzt
 * @time 2018年7月18日
 */
public class SpringFxmlLoader {


  public static Logger logger = null;
  /*
   * 加载日志
   * 注意：必须在Spring容器初始化之前手动加载日志
   */
  static {
    try {
      logger = LoggerFactory.getLogger(SpringFxmlLoader.class);
    } catch (Exception e) {
      ErrorUtil.err("日志组件初始化失败");
    }
  }
  
  /*
   * Spring IOC 容器
   */
  private static ApplicationContext applicationContext = null;



  /*
   * 加载Spring容器
   */
  static {
    try {
      applicationContext =
          new ClassPathXmlApplicationContext("spring/spring-service.xml");
    } catch (Exception e) {
      ErrorUtil.err("Spring组件初始化失败:" + e.getMessage());
    }
  }


  /**
   * 加载FXML文件
   *
   * @param url FXML 文件路径
   */
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

  /**
   * 自定义返回Spring容器
   */
  public static ApplicationContext getContext() {
    return applicationContext;
  }
}
