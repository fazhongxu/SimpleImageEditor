package com.yjing.imageeditandroid;

/**
 * Created by xxl on 19/4/29.
 * <p>
 * Description
 **/
public class Shapes {
    public float startX, startY, endX, endY ;
    public int width,color,circle;

    public Shapes(float startX, float startY, float endX, float endY, int width, int color , int circle) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;
        this.color = color;
        this.circle = circle;
    }
}
