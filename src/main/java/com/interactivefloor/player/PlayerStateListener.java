/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.interactivefloor.player;

import com.interactivefloor.player.Player.PlayerState;

/**
 *
 * @author cezerilab
 */
public interface PlayerStateListener {
    void onPlayerStateChange(Player player, PlayerState newState);
}
