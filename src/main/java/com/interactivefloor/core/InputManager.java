/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.core;

/**
 *
 * @author cezerilab
 */

import com.github.sarxos.webcam.Webcam;
import com.interactivefloor.detection.Blob;
import com.interactivefloor.detection.BlobDetector;
import com.interactivefloor.detection.BlobTracker;
import com.interactivefloor.player.Player;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages input processing from the IR camera and player tracking.
 * Coordinates between the blob detection system and player management.
 */
public class InputManager {
    private Webcam webcam;
    private BlobDetector blobDetector;
    private BlobTracker blobTracker;
    private Map<Integer, Player> players;
    
    // Camera resolution settings
    private static final int CAMERA_WIDTH = 640;
    private static final int CAMERA_HEIGHT = 480;
    private static final int BRIGHTNESS_THRESHOLD = 200;
    private int thresholdRange = 20; // Range around threshold value
    
    /**
     * Creates a new InputManager instance.
     * Initializes the tracking systems.
     */
    public InputManager() {
        this.blobDetector = new BlobDetector(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.blobTracker = new BlobTracker();
        this.players = new HashMap<>();
    }
    
    /**
     * Initializes the webcam and prepares the input system.
     * @throws RuntimeException if webcam initialization fails
     */
    public void initialize() {
        try {
            webcam = Webcam.getDefault();
            webcam.setViewSize(new Dimension(CAMERA_WIDTH, CAMERA_HEIGHT));
            webcam.open();
            
            if (!webcam.isOpen()) {
                throw new RuntimeException("Failed to open webcam");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize webcam: " + e.getMessage());
        }
    }
    
    /**
     * Updates and returns the current list of tracked players.
     * This method processes the camera input, detects blobs,
     * and updates player positions.
     * 
     * @return List of currently tracked players
     */
    public List<Player> updatePlayers() {
        // Get current frame from webcam
        BufferedImage image = webcam.getImage();
        if (image == null) {
            return new ArrayList<>(players.values());
        }
        
        // Detect blobs in the current frame
        List<Blob> blobs = blobDetector.detectBlobs(image, BRIGHTNESS_THRESHOLD,thresholdRange);
        
        // Track blobs across frames
        List<Blob> trackedBlobs = blobTracker.updateTracking(blobs);
        
        // Update player states
        updatePlayerStates(trackedBlobs);
        
        return new ArrayList<>(players.values());
    }
    
    /**
     * Updates the states of all tracked players based on detected blobs.
     * Creates new players for new blobs and removes players for lost blobs.
     * 
     * @param trackedBlobs List of currently tracked blobs
     */
    private void updatePlayerStates(List<Blob> trackedBlobs) {
        // Create a map for current frame players
        Map<Integer, Player> currentPlayers = new HashMap<>();
        
        for (Blob blob : trackedBlobs) {
            Player existingPlayer = players.get(blob.getId());
            
            if (existingPlayer != null) {
                // Update existing player
                existingPlayer.update((float)blob.getCenterX(), (float)blob.getCenterY());
                currentPlayers.put(blob.getId(), existingPlayer);
            } else {
                // Create new player
                Player newPlayer = new Player(
                    blob.getId(),
                    (float)blob.getCenterX(),
                    (float)blob.getCenterY()
                );
                currentPlayers.put(blob.getId(), newPlayer);
            }
        }
        
        // Replace old player map with current one
        players = currentPlayers;
    }
    
    /**
     * Releases all resources used by the input system.
     */
    public void cleanup() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }
    
    /**
     * Gets the current camera resolution width.
     * @return The camera width in pixels
     */
    public int getCameraWidth() {
        return CAMERA_WIDTH;
    }
    
    /**
     * Gets the current camera resolution height.
     * @return The camera height in pixels
     */
    public int getCameraHeight() {
        return CAMERA_HEIGHT;
    }
}