/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.animation;

import com.interactivefloor.core.SoundManager;
import processing.core.PApplet;
import com.interactivefloor.player.Player;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages and coordinates different animations in the system. Handles animation
 * registration, switching, and rendering.
 */
public class AnimationManager {

    private final Map<String, Animation> animations;
    private Animation activeAnimation;
    private Animation transitionAnimation;
    private float transitionProgress;
    private static final float TRANSITION_SPEED = 0.05f;

    // Menü için eklenen değişkenler
    private boolean showMenu = false;
    private long menuStartTime = 0;
    private static final long MENU_TIMEOUT = 5000; // 5 saniye
    private String highlightedAnimation = null;
    private Player menuPlayer = null;
    // Performance optimizasyonu için ön hesaplanmış değerler
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 16; // yaklaşık 60 FPS
    private final Rectangle menuBounds;
    private final int itemHeight = 60;  // Menü öğesi yüksekliğini artırdık
    private final int menuWidth = 400;  // Menü genişliğini artırdık
    private final int menuItemPadding = 20; // Menü öğeleri arası boşluk

    private final SoundManager soundManager;

    /**
     * Creates a new AnimationManager instance.
     */
    public AnimationManager(PApplet app) {
        this.animations = new HashMap<>();
        this.transitionProgress = 0;

        // Menüyü ekranın ortasına yerleştir
        int menuX = (app.width - menuWidth) / 2;
        int menuY = (app.height - (itemHeight * 4)) / 2; // 4 yerine animations.size() kullanılabilir
        this.menuBounds = new Rectangle(menuX, menuY, menuWidth, itemHeight * 4 + menuItemPadding * 2);

        this.soundManager = new SoundManager();
    }

    /**
     * Registers a new animation with the system.
     *
     * @param name Unique identifier for the animation
     * @param animation The animation to register
     * @throws IllegalArgumentException if name is already registered
     */
    public void registerAnimation(String name, Animation animation) {
        if (animations.containsKey(name)) {
            throw new IllegalArgumentException("Animation already registered: " + name);
        }
        animations.put(name, animation);

        // Set as active if it's the first animation
        if (activeAnimation == null) {
            activeAnimation = animation;
        }
    }

    /**
     * Switches to a different animation with a smooth transition.
     *
     * @param name Name of the animation to switch to
     * @return true if the animation was found and switch initiated
     */
    // Animasyon değiştirme metodunu güncelle
    public boolean setActiveAnimation(String name) {
        Animation newAnimation = animations.get(name);
        if (newAnimation != null && newAnimation != activeAnimation) {
            if (activeAnimation != null) {
                transitionAnimation = newAnimation;
                transitionProgress = 0;
                newAnimation.reset();
                soundManager.playSound("transition"); // Geçiş sesi
                soundManager.changeAmbience(name); // Yeni animasyonun ambiyans sesi
            } else {
                activeAnimation = newAnimation;
                soundManager.changeAmbience(name);
            }
            return true;
        }
        return false;
    }

    /**
     * Updates all active animations.
     *
     * @param app Processing app instance
     * @param player Current player
     */
    public void update(PApplet app, Player player) {
        long currentTime = System.currentTimeMillis();

        // FPS sınırlaması
        if (currentTime - lastUpdateTime < UPDATE_INTERVAL) {
            return;
        }
        lastUpdateTime = currentTime;

        // Sadece aktif animasyonu güncelle
        if (activeAnimation != null) {
            activeAnimation.update(app, player);
        }

        // Geçiş varsa sadece o zaman transition animasyonunu güncelle
        if (transitionAnimation != null) {
            transitionAnimation.update(app, player);
            updateTransition();
        }

        // Menü açıksa ve oyuncu menü sınırları içindeyse güncelle
        if (showMenu && menuBounds.contains(player.getX(), player.getY())) {
            updateMenuSelection(player);
        }
    }

    public void updateMenuSelection(Player player) {
        if (!showMenu || !menuBounds.contains(player.getX(), player.getY())) {
            if (highlightedAnimation != null) {
                highlightedAnimation = null;
                menuPlayer = null;
            }
            return;
        }

        int relativeY = (int) (player.getY() - (menuBounds.y + 80));
        int selectedIndex = relativeY / itemHeight;

        if (selectedIndex >= 0 && selectedIndex < animations.size()) {
            String newSelection = (String) animations.keySet().toArray()[selectedIndex];

            if (!newSelection.equals(highlightedAnimation)) {
                highlightedAnimation = newSelection;
                menuStartTime = System.currentTimeMillis();
                menuPlayer = player;
                soundManager.playSound("menu_change"); // Menü seçim değişim sesi
            }
        }
    }

    private static class Rectangle {

        final float x, y, width, height;

        Rectangle(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean contains(float px, float py) {
            return px >= x && px <= x + width
                    && py >= y && py <= y + height;
        }
    }

    // Menu çizimini optimize et
    private void drawMenu(PApplet app) {
        app.pushStyle();

        int centerX = (int) menuBounds.x;
        int centerY = (int) menuBounds.y;

        // Dış dikdörtgen (border)
        app.fill(50, 50, 100, 230);  // Koyu mavi-gri, yarı saydam
        app.stroke(200, 200, 255);    // Açık mavi kenarlık
        app.strokeWeight(3);
        app.rect(centerX, centerY, menuBounds.width, menuBounds.height, 15);  // Yuvarlatılmış köşeler

        // İç dikdörtgen
        app.fill(30, 30, 60, 230);   // Daha koyu iç kısım
        app.noStroke();
        float innerPadding = 10;
        app.rect(centerX + innerPadding, centerY + innerPadding,
                menuBounds.width - 2 * innerPadding,
                menuBounds.height - 2 * innerPadding, 10);

        // Başlık
        app.textSize(30);
        app.textAlign(PApplet.CENTER, PApplet.TOP);
        app.fill(255);
        app.text("Animation Menu", centerX + menuBounds.width / 2, centerY + 20);

        // Menu öğeleri
        app.textSize(24);
        int y = centerY + 80;  // Başlıktan sonra başla

        for (Map.Entry<String, Animation> entry : animations.entrySet()) {
            // Seçili öğe için arka plan
            if (entry.getKey().equals(highlightedAnimation)) {
                app.fill(100, 100, 180, 200);
                app.rect(centerX + 20, y - itemHeight / 2 + 10,
                        menuBounds.width - 40, itemHeight - 20, 8);
                app.fill(255);
            } else {
                app.fill(200, 200, 255, 200);
            }

            // Animasyon adı
            app.textAlign(PApplet.CENTER, PApplet.CENTER);
            app.text(entry.getValue().getName(),
                    centerX + menuBounds.width / 2, y + 5);

            y += itemHeight;
        }

        // Timer'ı sadece seçim varsa göster
        if (highlightedAnimation != null) {
            long remainingTime = (MENU_TIMEOUT - (System.currentTimeMillis() - menuStartTime)) / 1000;
            if (remainingTime > 0) {
                app.fill(255);
                app.textSize(20);
                app.textAlign(PApplet.CENTER, PApplet.BOTTOM);
                app.text("Selection in: " + remainingTime + "s",
                        centerX + menuBounds.width / 2,
                        centerY + menuBounds.height - 20);
            }
        }

        app.popStyle();
    }

    /**
     * Draws the current animation state.
     *
     * @param app Processing app instance
     */
    public void draw(PApplet app) {
        if (showMenu) {
            drawMenu(app);
            checkMenuSelection();
        }

        if (transitionAnimation != null) {
            // Draw both animations during transition
            app.pushStyle();
            app.tint(255, (1 - transitionProgress) * 255);
            activeAnimation.draw(app);
            app.tint(255, transitionProgress * 255);
            transitionAnimation.draw(app);
            app.popStyle();
        } else if (activeAnimation != null) {
            activeAnimation.draw(app);
        }
    }

    /**
     * Updates the transition progress between animations.
     */
    private void updateTransition() {
        transitionProgress += TRANSITION_SPEED;
        if (transitionProgress >= 1) {
            // Transition complete
            activeAnimation = transitionAnimation;
            transitionAnimation = null;
            transitionProgress = 0;
        }
    }

    /**
     * Gets the currently active animation.
     *
     * @return Optional containing the active animation, or empty if none
     */
    public Optional<Animation> getActiveAnimation() {
        return Optional.ofNullable(activeAnimation);
    }

    /**
     * Gets a registered animation by name.
     *
     * @param name The name of the animation to retrieve
     * @return Optional containing the animation, or empty if not found
     */
    public Optional<Animation> getAnimation(String name) {
        return Optional.ofNullable(animations.get(name));
    }

    /**
     * Resets all animations to their initial state.
     */
    public void resetAll() {
        animations.values().forEach(Animation::reset);
        transitionAnimation = null;
        transitionProgress = 0;
    }

    /**
     * Checks if a transition is currently in progress.
     *
     * @return true if a transition is occurring
     */
    public boolean isTransitioning() {
        return transitionAnimation != null;
    }

    /**
     * Gets the current transition progress.
     *
     * @return transition progress from 0 to 1, or 0 if no transition
     */
    public float getTransitionProgress() {
        return transitionProgress;
    }

    /**
     * Toggles the animation menu visibility
     */
    // Menü açma/kapama metodunu güncelle
    public void toggleMenu() {
        showMenu = !showMenu;
        if (showMenu) {
            menuStartTime = System.currentTimeMillis();
            soundManager.playSound("menu_select"); 
        } else {
            highlightedAnimation = null;
            menuPlayer = null;
        }
    }

    /**
     * Checks if menu selection is complete
     */
    private void checkMenuSelection() {
        if (menuPlayer != null && highlightedAnimation != null) {
            long timeOnSelection = System.currentTimeMillis() - menuStartTime;
            if (timeOnSelection >= MENU_TIMEOUT) {
                soundManager.playSound("menu_select"); // Seçim onay sesi
                setActiveAnimation(highlightedAnimation);
                showMenu = false;
                menuPlayer = null;
                highlightedAnimation = null;
            }
        }
    }

    // Temizleme metodu ekle
    public void cleanup() {
        if (soundManager != null) {
            soundManager.cleanup();
        }
    }

    // Ses kontrolü için yeni metodlar
    public void toggleSound() {
        soundManager.toggleMute();
    }

    public void setVolume(float volume) {
        soundManager.setVolume(volume);
    }
    public boolean isMuted() {
        return soundManager.isMuted();
    }

    public String getActiveAnimationName() {
        return activeAnimation != null ? activeAnimation.getName() : "None";
    }

}
