package com.kendy.db.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "man")
public class Man extends GenericEntity {

  @Id
  @Column(name = "name")
  private String name;

  @Column(name = "age")
  private String age;

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   */
  public void setName(String name) {
    this.name = name == null ? null : name.trim();
  }

  /**
   * @return age
   */
  public String getAge() {
    return age;
  }

  /**
   * @param age
   */
  public void setAge(String age) {
    this.age = age == null ? null : age.trim();
  }
}