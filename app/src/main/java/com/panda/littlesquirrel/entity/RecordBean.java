package com.panda.littlesquirrel.entity;

/**
 * 用户投递记录参数/回收员回收记录
 */
public class RecordBean {

    private int category;//垃圾桶分类
    private int count;//垃圾个数
    private double weight;//垃圾重量

    public RecordBean() {
    }

    public RecordBean(int category, int count, double weight) {
        this.category = category;
        this.count = count;
        this.weight = weight;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
                "category=" + category +
                ", count=" + count +
                ", weight=" + weight +
                '}';
    }
}
