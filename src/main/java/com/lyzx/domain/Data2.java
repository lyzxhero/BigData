package com.lyzx.domain;

import java.util.List;

public class Data2 {
    private String name;
    private int age;
    private List<Data> datas;

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

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "Data2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", datas=" + datas +
                '}';
    }
}
