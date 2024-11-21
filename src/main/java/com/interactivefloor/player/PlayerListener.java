/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.interactivefloor.player;

/**
 * Interface for listening to player events.
 * Implementations can react to player movements and actions.
 */
public interface PlayerListener {
    /**
     * Called when a player moves.
     *
     * @param player The player that moved
     * @param x Current X position
     * @param y Current Y position
     * @param velocityX X velocity
     * @param velocityY Y velocity
     */
    void onPlayerMove(Player player, float x, float y, float velocityX, float velocityY);
    
    /**
     * Called when a player jumps.
     *
     * @param player The player that jumped
     */
    void onPlayerJump(Player player);
}