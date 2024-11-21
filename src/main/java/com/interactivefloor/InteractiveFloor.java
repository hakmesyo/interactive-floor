package com.interactivefloor;

import com.interactivefloor.animation.AnimationManager;
import processing.core.PApplet;
import java.util.List;
import com.interactivefloor.core.InputManager;
import com.interactivefloor.player.Player;
import com.interactivefloor.animation.ParticleAnimation;
import com.interactivefloor.util.DebugUtils;

/**
 * Main class for the Interactive Floor System. This class handles the
 * Processing sketch setup and main loop, manages input processing and animation
 * rendering.
 */
public class InteractiveFloor extends PApplet {

    private InputManager inputManager;
    private AnimationManager animationManager;
    private boolean debugMode = false;

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

        // Initialize managers
        inputManager = new InputManager();
        animationManager = new AnimationManager();

        try {
            // Initialize input handling
            inputManager.initialize();

            // Register and set default animation
            animationManager.registerAnimation("particles", new ParticleAnimation());
            animationManager.setActiveAnimation("particles");

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
        // Clear background
        background(0);

        try {
            // Update input and get players
            List<Player> players = inputManager.updatePlayers();

            // Update and draw animations
            for (Player player : players) {
                animationManager.update(this, player);
            }
            animationManager.draw(this);

            // Show debug information if enabled
            if (debugMode) {
                DebugUtils.drawDebugInfo(this, players);
                DebugUtils.drawFrameRate(this);
            }

        } catch (Exception e) {
            System.err.println("Runtime error: " + e.getMessage());
        }
    }

    /**
     * Handles keyboard input. 'D' toggles debug mode 'ESC' exits the
     * application
     */
    @Override
    public void keyPressed() {
        if (key == 'd' || key == 'D') {
            debugMode = !debugMode;
        } else if (key == ESC) {
            exit();
        }
    }

    /**
     * Cleanup method called on application exit. Ensures proper resource
     * disposal.
     */
    @Override
    public void dispose() {
        if (inputManager != null) {
            inputManager.cleanup();
        }
        super.dispose();
    }

    /**
     * Gets the input manager instance.
     *
     * @return The input manager
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Gets the animation manager instance.
     *
     * @return The animation manager
     */
    public AnimationManager getAnimationManager() {
        return animationManager;
    }
}
