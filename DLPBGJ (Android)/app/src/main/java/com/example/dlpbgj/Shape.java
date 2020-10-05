package com.example.dlpbgj;

public abstract class Shape {
    private int x,y, String color;

    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
        this.color=color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
