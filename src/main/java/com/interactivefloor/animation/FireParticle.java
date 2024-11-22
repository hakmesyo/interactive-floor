/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.animation;

/**
 *
 * @author cezerilab
 */
public class FireParticle {

    public float x, y;
    public float vx, vy;
    public float size;
    public float alpha;
    public float brightness;
    private static final float GRAVITY = -0.1f;

    public FireParticle(float x, float y) {
        this.x = x;
        this.y = y;
        this.vx = (float) (Math.random() * 2 - 1);
        this.vy = -3 + (float) (Math.random() * -2);
        this.size = 5 + (float) (Math.random() * 15);
        this.alpha = 255;
        this.brightness = (float) Math.random();
    }

    public void update() {
        x += vx;
        y += vy;
        vy -= GRAVITY;
        size *= 0.97f;
        alpha *= 0.95f;
    }

    public boolean isDead() {
        return alpha < 10 || size < 1;
    }
}
