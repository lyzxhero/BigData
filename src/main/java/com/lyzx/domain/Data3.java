package com.lyzx.domain;

import com.fasterxml.jackson.annotation.JsonView;

public class Data3 {

    @JsonView(Views.Normal.class)
    private String name;
    @JsonView(Views.Manager.class)
    private String extra;
    @JsonView(Views.Normal.class)
    private int age;
    @JsonView(Views.Normal.class)
    private double weight;
    @JsonView(Views.Normal.class)
    private double height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Data3{" +
                "name='" + name + '\'' +
                ", extra='" + extra + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }
}
