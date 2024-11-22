package com.interactivefloor.animation;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

public class LogoAnimation {

    private final PApplet app;
    private PImage logo;
    private float scale = 0.001f;
    private float alpha = 255;
    private List<Particle> particles;
    private boolean isComplete = false;
    private float targetScale;

    private enum Stage {
        GROWING, // Logo büyüyor
        STABLE, // Logo bekliyor
        EXPLODING, // Logo patlıyor
        CIRCULAR, // Parçacıklar çember üzerinde dönüyor
        REFORMING, // Logo yeniden oluşuyor
        FADING, // Solma ve küçülme efekti
        COMPLETE       // Animasyon bitti
    }

    private float rotationAngle = 0;
    private float circleRadius = 300;
    private Stage currentStage = Stage.GROWING;
    private int stageTimer = 0;

    private class Particle {

        PVector pos;      // Mevcut pozisyon
        PVector original; // Başlangıç pozisyonu
        PVector vel;      // Hız
        float size;
        int color;

        Particle(float x, float y, int c) {
            pos = new PVector(x, y);
            original = new PVector(x, y);
            vel = new PVector();
            size = app.random(2, 4);
            color = c;
        }

        void update() {
            if (currentStage == Stage.EXPLODING) {
                vel.mult(0.98f); // 0.95f yerine 0.98f yaparak yavaşlamayı azalttık
                pos.add(vel);
            } else if (currentStage == Stage.REFORMING) {
                PVector direction = PVector.sub(original, pos);
                //direction.mult(0.015f);
                direction.mult(0.05f);
                pos.add(direction);
            }
        }

        void draw() {
            app.noStroke();
            app.fill(color);
            app.ellipse(pos.x, pos.y, size, size);
        }
    }

    public LogoAnimation(PApplet app) {
        this.app = app;
        this.particles = new ArrayList<>();
        this.logo = app.loadImage("images/cezerilab_logo.png");
        this.targetScale = (app.width * 0.75f) / logo.width;
    }

    public void draw() {
        app.pushStyle();
        app.background(0);

        stageTimer++;

        switch (currentStage) {
            case GROWING:
                drawGrowingLogo();
                break;

            case STABLE:
                drawStableLogo();
                if (stageTimer > 60) {
                    createExplosion();
                    currentStage = Stage.EXPLODING;
                    stageTimer = 0;
                }
                break;

            case EXPLODING:
                updateAndDrawParticles();
                if (stageTimer > 300) {
                    currentStage = Stage.REFORMING;
                    stageTimer = 0;
                }
                break;

            case REFORMING:
                updateAndDrawParticles();
                if (stageTimer > 300) {
                    currentStage = Stage.FADING;
                    stageTimer = 0;
                    alpha = 255;
                }
                break;

            case FADING:
                // Alpha değeri azalırken scale de azalsın
                alpha = PApplet.max(0, alpha - 1); // Yavaşça kaybol
                scale = PApplet.lerp(scale, 0.1f, 0.02f); // Yavaşça küçül

                // Parçacıkları çiz
                for (Particle p : particles) {
                    app.noStroke();
                    app.fill(p.color, alpha);
                    app.ellipse(p.pos.x, p.pos.y, p.size, p.size);
                }

                if (alpha <= 0) {
                    currentStage = Stage.COMPLETE;
                }
                break;

            case COMPLETE:
                isComplete = true;
                break;
        }

        app.popStyle();
    }

    private void drawGrowingLogo() {
        if (logo != null) {
            app.imageMode(PApplet.CENTER);
            scale = PApplet.lerp(scale, targetScale, 0.01f);

            app.tint(255, alpha);
            app.image(logo, app.width / 2, app.height / 2,
                    logo.width * scale,
                    logo.height * scale);

            if (Math.abs(targetScale - scale) < 0.01f) {
                currentStage = Stage.STABLE;
                stageTimer = 0;
            }
        }
    }

    private void drawStableLogo() {
        app.imageMode(PApplet.CENTER);
        app.tint(255, alpha);
        app.image(logo, app.width / 2, app.height / 2,
                logo.width * scale,
                logo.height * scale);
    }

    private void createExplosion() {
        float logoX = app.width / 2 - (logo.width * scale) / 2;
        float logoY = app.height / 2 - (logo.height * scale) / 2;

        for (int x = 0; x < logo.width; x += 8) {
            for (int y = 0; y < logo.height; y += 8) {
                int pixel = logo.get(x, y);
                if (app.alpha(pixel) > 0) {
                    float worldX = logoX + x * scale;
                    float worldY = logoY + y * scale;
                    Particle p = new Particle(worldX, worldY, pixel);

                    float angle = app.random(PApplet.TWO_PI);
                    float speed = app.random(3, 8); // Hızı artırdık (1,3 iken 3,8 yaptık)
                    p.vel.x = PApplet.cos(angle) * speed;
                    p.vel.y = PApplet.sin(angle) * speed;

                    particles.add(p);
                }
            }
        }
    }

    private void updateAndDrawParticles() {
        for (Particle p : particles) {
            p.update();
            p.draw();
        }
    }

    public boolean isComplete() {
        return isComplete;
    }
}
