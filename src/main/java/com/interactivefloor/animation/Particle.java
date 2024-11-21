/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.animation;

import processing.core.PVector;

/**
 * Represents a single particle in the particle animation system. Contains
 * position, velocity, and lifecycle information.
 */
public class Particle {

    PVector position;
    PVector velocity;
    PVector acceleration;
    float size;
    int lifespan;
    private static final float GRAVITY = 0.1f;

    /**
     * Creates a new particle with specified properties.
     *
     * @param x Initial X position
     * @param y Initial Y position
     * @param vx Initial X velocity
     * @param vy Initial Y velocity
     * @param size Particle size
     * @param lifespan Initial lifespan
     */
    public Particle(float x, float y, float vx, float vy, float size, int lifespan) {
        this.position = new PVector(x, y);
        this.velocity = new PVector(vx, vy);
        this.acceleration = new PVector(0, GRAVITY);
        this.size = size;
        this.lifespan = lifespan;
    }

    /**
     * Updates the particle's position and lifespan.
     */
    public void update() {
        // Apply physics
        velocity.add(acceleration);
        position.add(velocity);

        // Add some drag
        velocity.mult(0.98f);

        // Decrease lifespan
        lifespan -= 2;
    }

    /**
     * Checks if the particle should be removed.
     *
     * @return true if the particle's lifespan has expired
     */
    public boolean isDead() {
        return lifespan <= 0;
    }

    /**
     * Gets the current alpha value based on lifespan.
     *
     * @param maxLifespan The initial lifespan value
     * @return Alpha value between 0 and 255
     */
    public float getAlpha(int maxLifespan) {
        return (float) lifespan / maxLifespan * 255;
    }
}
