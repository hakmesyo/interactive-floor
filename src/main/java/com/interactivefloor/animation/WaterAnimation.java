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
public class WaterAnimation implements Animation {

    private List<WaterRipple> ripples = new ArrayList<>();
    private static final int MAX_RIPPLES = 100;

    @Override
    public void update(PApplet app, Player player) {
        // Oyuncu hareketine göre su dalgaları oluştur
        if (player.getSpeed() > 0.1f) {
            if (ripples.size() < MAX_RIPPLES) {
                ripples.add(new WaterRipple(player.getX(), player.getY()));
            }
        }

        // Dalgaları güncelle ve ölenleri kaldır
        ripples.removeIf(ripple -> {
            ripple.update();
            return ripple.isDead();
        });
    }

    @Override
    public void draw(PApplet app) {
        app.pushStyle();
        app.noStroke();
        for (WaterRipple ripple : ripples) {
            float alpha = PApplet.map(ripple.getLife(), 0, 100, 0, 255);
            app.fill(0, 150, 255, alpha);
            app.ellipse(ripple.x, ripple.y, ripple.size, ripple.size);
        }
        app.popStyle();
    }

    @Override
    public String getName() {
        return "Water Ripples";
    }

    @Override
    public void reset() {
        ripples.clear();
    }
}
