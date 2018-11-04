package com.kendy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义列注解
 *
 * @author linzt
 * @time 2018年7月6日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mycolumn {

  /**
   * 不需要红色列
   */
  boolean noNeedRedColumn() default false;


}
