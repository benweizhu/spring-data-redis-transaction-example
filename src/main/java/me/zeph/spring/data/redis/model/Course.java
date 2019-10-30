package me.zeph.spring.data.redis.model;

import java.io.Serializable;

public class Course implements Serializable {

  private String name;
  private int count;

  public Course() {
  }

  public Course(String name, int count) {
    this.name = name;
    this.count = count;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
