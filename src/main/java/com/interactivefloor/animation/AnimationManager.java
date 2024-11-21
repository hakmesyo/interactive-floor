/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.animation;

import processing.core.PApplet;
import com.interactivefloor.player.Player;
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

    /**
     * Creates a new AnimationManager instance.
     */
    public AnimationManager() {
        this.animations = new HashMap<>();
        this.transitionProgress = 0;
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
    public boolean setActiveAnimation(String name) {
        Animation newAnimation = animations.get(name);
        if (newAnimation != null && newAnimation != activeAnimation) {
            if (activeAnimation != null) {
                // Start transition
                transitionAnimation = newAnimation;
                transitionProgress = 0;
                newAnimation.reset();
            } else {
                // Direct switch if no active animation
                activeAnimation = newAnimation;
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
        if (activeAnimation != null) {
            activeAnimation.update(app, player);
        }

        if (transitionAnimation != null) {
            transitionAnimation.update(app, player);
            updateTransition();
        }
    }

    /**
     * Draws the current animation state.
     *
     * @param app Processing app instance
     */
    public void draw(PApplet app) {
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
}
