/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.animation;

import processing.core.PApplet;
import com.interactivefloor.player.Player;
import com.interactivefloor.player.PlayerListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A particle-based animation that creates visual effects following player
 * movement. Implements both Animation and PlayerListener interfaces to react to
 * player events.
 */
public class ParticleAnimation implements Animation, PlayerListener {

    private final List<Particle> particles;
    private static final int MAX_PARTICLES = 1000;
    private static final int PARTICLES_PER_UPDATE = 5;
    private static final float BASE_SPEED = 2.0f;

    // Animation parameters
    private int particleLifespan = 255;
    private float speedMultiplier = 1.0f;
    private float particleSize = 8.0f;
    private int particleColor;

    /**
     * Creates a new ParticleAnimation instance.
     */
    public ParticleAnimation() {
        this.particles = new ArrayList<>();
        this.particleColor = 0xFFFFFFFF; // White by default
    }

    @Override
    public void update(PApplet app, Player player) {
        // Create new particles based on player movement
        float speed = PApplet.mag(player.getX(), player.getY());
        if (speed > 0.1f) {
            createParticlesForPlayer(player, app);
        }

        // Update existing particles
        updateParticles();
    }

    @Override
    public void draw(PApplet app) {
        app.pushStyle();
        app.noStroke();

        for (Particle particle : particles) {
            float alpha = PApplet.map(particle.lifespan, 0, particleLifespan, 0, 255);
            app.fill(particleColor, alpha);
            app.ellipse(particle.position.x, particle.position.y,
                    particle.size, particle.size);
        }

        app.popStyle();
    }

    /**
     * Creates new particles at the player's position.
     */
    private void createParticlesForPlayer(Player player, PApplet app) {
        if (particles.size() < MAX_PARTICLES) {
            for (int i = 0; i < PARTICLES_PER_UPDATE; i++) {
                float angle = app.random(PApplet.TWO_PI);
                float speed = BASE_SPEED * speedMultiplier * app.random(0.5f, 1.5f);

                Particle particle = new Particle(
                        player.getX(),
                        player.getY(),
                        PApplet.cos(angle) * speed,
                        PApplet.sin(angle) * speed,
                        particleSize * app.random(0.5f, 1.0f),
                        particleLifespan
                );

                particles.add(particle);
            }
        }
    }

    /**
     * Updates all particles and removes dead ones.
     */
    private void updateParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle particle = it.next();
            particle.update();

            if (particle.isDead()) {
                it.remove();
            }
        }
    }

    @Override
    public void onPlayerMove(Player player, float x, float y, float velocityX, float velocityY) {
        // Additional movement-based effects could be added here
    }

    @Override
    public void onPlayerJump(Player player) {
        // Create a burst of particles on jump
        speedMultiplier = 2.0f;
        // Reset speed multiplier after 500ms
        new Thread(() -> {
            try {
                Thread.sleep(500);
                speedMultiplier = 1.0f;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void reset() {
        particles.clear();
        speedMultiplier = 1.0f;
    }

    @Override
    public String getName() {
        return "Particle Effect";
    }

    @Override
    public void setParameters(Object... params) {
        if (params.length >= 1 && params[0] instanceof Integer) {
            particleColor = (Integer) params[0];
        }
        if (params.length >= 2 && params[1] instanceof Float) {
            particleSize = (Float) params[1];
        }
        if (params.length >= 3 && params[2] instanceof Integer) {
            particleLifespan = (Integer) params[2];
        }
    }
}
