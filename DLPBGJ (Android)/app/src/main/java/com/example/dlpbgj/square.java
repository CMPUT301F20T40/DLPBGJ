package com.example.dlpbgj;

public class square extends Shape {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public square(int x, int y, String name) {
        super(x, y);
        this.name = name;
    }
}
