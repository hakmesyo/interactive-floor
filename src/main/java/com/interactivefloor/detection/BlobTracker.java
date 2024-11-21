/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.detection;

import java.util.*;

/**
 * Tracks blobs across consecutive frames. Maintains blob identities by matching
 * blobs between frames based on their positions and characteristics.
 */
public class BlobTracker {

    private static final double MAX_MATCHING_DISTANCE = 50.0;  // Maximum distance for blob matching
    private static final long BLOB_TIMEOUT = 500;             // Milliseconds before considering a blob lost

    private int nextId;                                       // Next available blob ID
    private Map<Integer, Blob> trackedBlobs;                 // Currently tracked blobs

    /**
     * Creates a new BlobTracker instance.
     */
    public BlobTracker() {
        this.nextId = 0;
        this.trackedBlobs = new HashMap<>();
    }

    /**
     * Updates tracking information based on newly detected blobs. Matches new
     * blobs with existing ones and maintains tracking ids.
     *
     * @param newBlobs List of newly detected blobs
     * @return List of tracked blobs with persistent IDs
     */
    public List<Blob> updateTracking(List<Blob> newBlobs) {
        Map<Integer, Blob> newTrackedBlobs = new HashMap<>();
        Set<Blob> unmatched = new HashSet<>(newBlobs);

        // Try to match existing blobs with new ones
        for (Blob trackedBlob : trackedBlobs.values()) {
            Blob bestMatch = findBestMatch(trackedBlob, unmatched);

            if (bestMatch != null) {
                // Update existing blob tracking
                bestMatch.setId(trackedBlob.getId());
                bestMatch.updateTime();
                newTrackedBlobs.put(bestMatch.getId(), bestMatch);
                unmatched.remove(bestMatch);
            }
        }

        // Assign new IDs to unmatched blobs
        for (Blob blob : unmatched) {
            blob.setId(nextId++);
            blob.updateTime();
            newTrackedBlobs.put(blob.getId(), blob);
        }

        // Update tracking state
        removeTimedOutBlobs(newTrackedBlobs);
        trackedBlobs = newTrackedBlobs;

        return new ArrayList<>(trackedBlobs.values());
    }

    /**
     * Finds the best matching blob from candidates for a tracked blob. Uses
     * distance and size criteria for matching.
     *
     * @param trackedBlob The blob being tracked
     * @param candidates Set of potential matching blobs
     * @return The best matching blob or null if no match found
     */
    private Blob findBestMatch(Blob trackedBlob, Set<Blob> candidates) {
        Blob bestMatch = null;
        double bestDistance = MAX_MATCHING_DISTANCE;

        for (Blob candidate : candidates) {
            double distance = trackedBlob.distanceTo(candidate);
            if (distance < bestDistance && isGoodMatch(trackedBlob, candidate)) {
                bestMatch = candidate;
                bestDistance = distance;
            }
        }

        return bestMatch;
    }

    /**
     * Determines if two blobs are a good match based on their characteristics.
     * Considers size and shape similarity in addition to position.
     *
     * @param blob1 First blob
     * @param blob2 Second blob
     * @return true if the blobs are considered a good match
     */
    private boolean isGoodMatch(Blob blob1, Blob blob2) {
        // Check mass similarity (allow 30% difference)
        double massRatio = (double) blob1.getMass() / blob2.getMass();
        if (massRatio < 0.7 || massRatio > 1.3) {
            return false;
        }

        // Check area ratio similarity (allow 20% difference)
        double areaRatio1 = blob1.getAreaRatio();
        double areaRatio2 = blob2.getAreaRatio();
        double ratioDiff = Math.abs(areaRatio1 - areaRatio2);
        if (ratioDiff > 0.2) {
            return false;
        }

        return true;
    }

    /**
     * Removes blobs that haven't been updated within the timeout period.
     *
     * @param blobs Map of tracked blobs to check
     */
    private void removeTimedOutBlobs(Map<Integer, Blob> blobs) {
        blobs.entrySet().removeIf(entry
                -> entry.getValue().hasTimedOut(BLOB_TIMEOUT)
        );
    }

    /**
     * Gets the number of currently tracked blobs.
     *
     * @return Number of tracked blobs
     */
    public int getTrackedBlobCount() {
        return trackedBlobs.size();
    }

    /**
     * Clears all tracking data and resets the ID counter.
     */
    public void reset() {
        trackedBlobs.clear();
        nextId = 0;
    }

    /**
     * Gets a tracked blob by its ID.
     *
     * @param id The blob ID to look up
     * @return The tracked blob, or null if not found
     */
    public Blob getTrackedBlob(int id) {
        return trackedBlobs.get(id);
    }
}
