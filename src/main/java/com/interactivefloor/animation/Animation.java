/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.interactivefloor.animation;

import processing.core.PApplet;
import com.interactivefloor.player.Player;

/**
 * Interface for all animations in the system. Each animation type should
 * implement this interface.
 */
public interface Animation {

    /**
     * Updates the animation state based on player movement.
     *
     * @param app Processing app instance for drawing
     * @param player The player to animate for
     */
    void update(PApplet app, Player player);

    /**
     * Draws the animation to the screen.
     *
     * @param app Processing app instance for drawing
     */
    void draw(PApplet app);

    /**
     * Resets the animation state.
     */
    void reset();

    /**
     * Gets the name of the animation.
     *
     * @return Animation name
     */
    String getName();

    /**
     * Sets animation-specific parameters.
     *
     * @param params Variable number of parameters
     */
    default void setParameters(Object... params) {
        // Default empty implementation
    }

    /**
     * Checks if the animation is active.
     *
     * @return true if the animation is currently active
     */
    default boolean isActive() {
        return true;
    }
}
