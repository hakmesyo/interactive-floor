/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.detection;

/**
 * Represents a detected blob in the image. A blob is a connected region of
 * pixels that meets certain criteria (brightness, size, etc.) and potentially
 * corresponds to a player.
 */
public class Blob {

    private double centerX;
    private double centerY;
    private int minX, minY, maxX, maxY;
    private int mass;
    private int id;
    private long lastUpdateTime;

    /**
     * Creates a new Blob instance. Initializes boundaries and mass for the blob
     * detection process.
     */
    public Blob() {
        this.mass = 0;
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * Adds a pixel to this blob and updates its properties. This method is
     * called during the flood fill process of blob detection.
     *
     * @param x The x-coordinate of the pixel
     * @param y The y-coordinate of the pixel
     */
    public void addPixel(int x, int y) {
        mass++;
        minX = Math.min(minX, x);
        minY = Math.min(minY, y);
        maxX = Math.max(maxX, x);
        maxY = Math.max(maxY, y);
        centerX = (minX + maxX) / 2.0;
        centerY = (minY + maxY) / 2.0;
    }

    /**
     * Calculates the distance to another blob. Used for blob tracking between
     * frames.
     *
     * @param other The other blob to compare with
     * @return The Euclidean distance between blob centers
     */
    public double distanceTo(Blob other) {
        double dx = this.centerX - other.centerX;
        double dy = this.centerY - other.centerY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Checks if this blob meets the minimum size requirements. Used to filter
     * out noise and small artifacts.
     *
     * @param minMass The minimum required mass
     * @param maxMass The maximum allowed mass
     * @return true if the blob size is within acceptable range
     */
    public boolean isValidSize(int minMass, int maxMass) {
        return this.mass >= minMass && this.mass <= maxMass;
    }

    /**
     * Gets the bounding box width of the blob.
     *
     * @return Width in pixels
     */
    public int getWidth() {
        return maxX - minX;
    }

    /**
     * Gets the bounding box height of the blob.
     *
     * @return Height in pixels
     */
    public int getHeight() {
        return maxY - minY;
    }

    /**
     * Gets the area ratio (mass / bounding box area). Used to determine blob
     * density.
     *
     * @return Ratio between 0 and 1
     */
    public double getAreaRatio() {
        int boundingArea = getWidth() * getHeight();
        return boundingArea > 0 ? (double) mass / boundingArea : 0;
    }

    /**
     * Updates the last detection time of this blob. Used for tracking and
     * timeout purposes.
     */
    public void updateTime() {
        this.lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * Checks if the blob has timed out.
     *
     * @param timeout Timeout duration in milliseconds
     * @return true if the blob hasn't been updated within the timeout period
     */
    public boolean hasTimedOut(long timeout) {
        return System.currentTimeMillis() - lastUpdateTime > timeout;
    }

    // Getters and Setters
    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public int getMass() {
        return mass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public String toString() {
        return String.format("Blob[id=%d, center=(%.1f,%.1f), mass=%d]",
                id, centerX, centerY, mass);
    }
}
