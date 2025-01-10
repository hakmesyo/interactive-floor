package com.interactivefloor;

import com.interactivefloor.animation.AnimationManager;
import com.interactivefloor.animation.FireAnimation;
import processing.core.PApplet;
import java.util.List;
import com.interactivefloor.core.InputManager;
import com.interactivefloor.player.Player;
import com.interactivefloor.animation.ParticleAnimation;
import com.interactivefloor.animation.WaterAnimation;
import com.interactivefloor.core.SoundManager;
import com.interactivefloor.util.DebugUtils;
import com.interactivefloor.animation.LogoAnimation;

/**
 * Main class for the Interactive Floor System. This class handles the
 * Processing sketch setup and main loop, manages input processing and animation
 * rendering.
 */
public class InteractiveFloor extends PApplet {

    private InputManager inputManager;
    private AnimationManager animationManager;
    private boolean debugMode = false;
    private float volume = 1.0f;
    private String statusMessage = "";
    private int messageTimer = 0;
    private static final int MESSAGE_DURATION = 30;
    private SoundManager soundManager;
    private LogoAnimation logoAnimation;
    private boolean introComplete = false;
    private long introStartTime;

    /**
     * Entry point of the application. Initializes the Processing sketch in
     * fullscreen mode.
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{
            "--present", // Fullscreen mode
            InteractiveFloor.class.getName()
        });
    }

    /**
     * Processing settings configuration. Sets up the display mode and renderer.
     */
    @Override
    public void settings() {
        fullScreen(P2D);  // Fullscreen mode with 2D renderer
    }

    /**
     * Initial setup of the application. Initializes managers and sets up the
     * default animation.
     */
    @Override
    public void setup() {
        // Basic setup
        frameRate(60);
        colorMode(RGB);
        background(0);

        // Initialize managers and components
        inputManager = new InputManager(this);
        animationManager = new AnimationManager(this);
        soundManager = new SoundManager();
        logoAnimation = new LogoAnimation(this);
        introStartTime = System.currentTimeMillis();

        try {
            // Initialize input handling
            inputManager.initialize();

            // Register animations
            ParticleAnimation particleAnim = new ParticleAnimation();
            animationManager.registerAnimation("particles", particleAnim);
            animationManager.registerAnimation("water", new WaterAnimation());
            animationManager.registerAnimation("fire", new FireAnimation());

            // Set default animation
            animationManager.setActiveAnimation("particles");

            // Menüyü başlangıçta açık yap
            animationManager.showMenu = true; // Bu satırı ekleyin

            // Play intro sound
            soundManager.playSound("intro");

        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            exit();
        }
    }

    /**
     * Main draw loop. Updates player positions and renders animations.
     */
    @Override
    public void draw() {
        if (!introComplete) {
            // Show intro animation
            logoAnimation.draw();

            if (logoAnimation.isComplete()) {
                introComplete = true;
                animationManager.showMenu = true;  // Direkt true yapıyoruz
            }
            return;
        }

        // Normal application flow
        background(255);

        try {
            // Mouse kontrolü için animasyon güncellemesi
            animationManager.update(this, null);

            // Mouse menü kontrolü
            animationManager.updateMenuSelection(null);

            // Player kontrollerine geçmeden önce player listesini al
            List<Player> players = inputManager.updatePlayers();

            // Eğer player varsa onlar için güncelleme yap
            if (players != null && !players.isEmpty()) {
                for (Player player : players) {
                    if (player != null) {  // Ek null kontrolü
                        animationManager.update(this, player);
                        animationManager.updateMenuSelection(player);
                    }
                }
            }

            // En son olarak çizimi yap
            animationManager.draw(this);

            // Show debug information if enabled
            if (debugMode) {
                drawDebugInfo();
                DebugUtils.drawFrameRate(this);
                DebugUtils.drawCameraBounds(this, inputManager);
            }

            // Show status messages
            if (messageTimer > 0) {
                drawStatusMessage();
                messageTimer--;
            }

        } catch (Exception e) {
            System.err.println("Runtime error: " + e.getMessage());
            e.printStackTrace();  // Daha detaylı hata bilgisi için
        }
    }

    private void drawStatusMessage() {
        pushStyle();

        // Panel dimensions
        int panelWidth = 300;
        int panelHeight = 100;
        int centerX = width / 2 - panelWidth / 2;
        int centerY = height / 2 - panelHeight / 2;

        // Outer rectangle
        fill(50, 50, 100, 230);
        stroke(200, 200, 255);
        strokeWeight(3);
        rect(centerX, centerY, panelWidth, panelHeight, 15);

        // Inner rectangle
        fill(30, 30, 60, 230);
        noStroke();
        float innerPadding = 10;
        rect(centerX + innerPadding, centerY + innerPadding,
                panelWidth - 2 * innerPadding, panelHeight - 2 * innerPadding, 10);

        // Message
        fill(255);
        textAlign(CENTER, CENTER);
        textSize(24);
        text(statusMessage, width / 2, height / 2);

        popStyle();
    }

    private void drawDebugInfo() {
        pushStyle();

        // Panel dimensions and position
        int panelWidth = 400;
        int panelHeight = 300;
        int centerX = width - panelWidth - 50;
        int centerY = 50;

        // Outer rectangle (border)
        fill(50, 50, 100, 230);
        stroke(200, 200, 255);
        strokeWeight(3);
        rect(centerX, centerY, panelWidth, panelHeight, 15);

        // Inner rectangle
        fill(30, 30, 60, 230);
        noStroke();
        float innerPadding = 10;
        rect(centerX + innerPadding, centerY + innerPadding,
                panelWidth - 2 * innerPadding, panelHeight - 2 * innerPadding, 10);

        // Title
        textSize(30);
        textAlign(CENTER, TOP);
        fill(255);
        text("Debug Information", centerX + panelWidth / 2, centerY + 20);

        // Debug information
        textSize(20);
        textAlign(LEFT);
        int x = centerX + 30;
        int y = centerY + 80;
        int lineHeight = 35;

        // System Information
        fill(200, 200, 255);
        text("System:", x, y);
        fill(255);
        text("FPS: " + nf(frameRate, 0, 1), x + 150, y);

        // Sound Information
        y += lineHeight;
        fill(200, 200, 255);
        text("Sound:", x, y);
        fill(255);
        text(animationManager.isMuted() ? "OFF" : "ON", x + 150, y);

        y += lineHeight;
        fill(200, 200, 255);
        text("Volume:", x, y);
        fill(255);
        text(nf((float) volume * 100, 0, 0) + "%", x + 150, y);

        // Animation Information
        y += lineHeight;
        fill(200, 200, 255);
        text("Animation:", x, y);
        fill(255);
        text(animationManager.getActiveAnimationName(), x + 150, y);

        // Player Information
        y += lineHeight;
        fill(200, 200, 255);
        text("Players:", x, y);
        fill(255);
        text(String.valueOf(inputManager.getPlayerCount()), x + 150, y);

        // Controls Information
        y += lineHeight * 1.5;
        fill(200, 200, 255);
        textSize(18);
        text("Controls:", x, y);
        fill(255, 255, 255, 200);
        textSize(16);
        y += lineHeight * 0.8;
        text("D: Toggle Debug  |  M: Menu  |  S: Sound", x, y);
        y += lineHeight * 0.8;
        text("+/-: Volume  |  ESC: Exit", x, y);

        popStyle();
    }

    /**
     * Handles keyboard input
     */
    @Override
    public void keyPressed() {
        if (key == 'd' || key == 'D') {
            debugMode = !debugMode;
            soundManager.playSound("menu_select");
            showMessage("Debug Mode: " + (debugMode ? "ON" : "OFF"));
        } else if (key == 'm' || key == 'M') {
            animationManager.toggleMenu();
        } else if (key == 's' || key == 'S') {
            animationManager.toggleSound();
            soundManager.playSound("menu_select");
            showMessage("Sound: " + (animationManager.isMuted() ? "OFF" : "ON"));
        } else if (key == '+' || key == '=') {
            soundManager.playSound("menu_select");
            volume = Math.min(1.0f, volume + 0.1f);
            animationManager.setVolume(volume);
            showMessage("Volume: " + nf((float) volume * 100, 0, 0) + "%");
        } else if (key == '-' || key == '_') {
            soundManager.playSound("menu_select");
            volume = Math.max(0.0f, volume - 0.1f);
            animationManager.setVolume(volume);
            showMessage("Volume: " + nf((float) volume * 100, 0, 0) + "%");
        } else if (key == ESC) {
            exit();
        }
    }

    private void showMessage(String message) {
        statusMessage = message;
        messageTimer = MESSAGE_DURATION;
    }

    /**
     * Cleanup method called on application exit
     */
    @Override
    public void dispose() {
        if (inputManager != null) {
            inputManager.cleanup();
        }
        if (animationManager != null) {
            animationManager.cleanup();
        }
        super.dispose();
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }
}
