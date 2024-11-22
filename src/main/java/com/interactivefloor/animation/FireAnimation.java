/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.animation;

import com.interactivefloor.player.Player;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author cezerilab
 */
public class FireAnimation implements Animation {

    private List<FireParticle> flames = new ArrayList<>();
    private static final int MAX_FLAMES = 200;

    @Override
    public void update(PApplet app, Player player) {
        // Oyuncu hareketine göre ateş parçacıkları oluştur
        if (flames.size() < MAX_FLAMES) {
            for (int i = 0; i < 5; i++) {
                flames.add(new FireParticle(player.getX(), player.getY()));
            }
        }

        // Alevleri güncelle ve ölenleri kaldır
        flames.removeIf(flame -> {
            flame.update();
            return flame.isDead();
        });
    }

    @Override
    public void draw(PApplet app) {
        app.pushStyle();
        app.blendMode(PApplet.ADD);
        app.noStroke();
        for (FireParticle flame : flames) {
            app.fill(255, flame.brightness * 128, 0, flame.alpha);
            app.ellipse(flame.x, flame.y, flame.size, flame.size);
        }
        app.blendMode(PApplet.BLEND);
        app.popStyle();
    }

    @Override
    public String getName() {
        return "Fire Effect";
    }

    @Override
    public void reset() {
        flames.clear();
    }
}
