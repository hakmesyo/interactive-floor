/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.animation;

/**
 *
 * @author cezerilab
 */
public class WaterRipple {

    public float x, y;
    public float size;
    private float maxSize;
    private float growthSpeed;
    private int life;
    private static final int MAX_LIFE = 100;

    public WaterRipple(float x, float y) {
        this.x = x;
        this.y = y;
        this.size = 10;
        this.maxSize = 100 + (float) (Math.random() * 50);
        this.growthSpeed = 2 + (float) (Math.random() * 2);
        this.life = MAX_LIFE;
    }

    public void update() {
        if (size < maxSize) {
            size += growthSpeed;
        }
        life -= 1;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public int getLife() {
        return life;
    }

    public float getAlpha() {
        return (float) life / MAX_LIFE * 255;
    }
}
