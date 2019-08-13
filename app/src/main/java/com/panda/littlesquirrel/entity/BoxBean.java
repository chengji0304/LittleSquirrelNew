package com.panda.littlesquirrel.entity;

/**
 * Created by jinjing on 2019/6/27.
 */

public class BoxBean {
    private int canNum;//桶号
    private int category;//垃圾桶分类
    private double quantity;//容量
    private double weight;//垃圾重量
    private double temperature;//温度

    public BoxBean() {
    }

    public int getCanNum() {
        return canNum;
    }

    public void setCanNum(int canNum) {
        this.canNum = canNum;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "BoxBean{" +
                "canNum=" + canNum +
                ", category=" + category +
                ", quantity=" + quantity +
                ", weight=" + weight +
                ", temperature=" + temperature +
                '}';
    }
}
