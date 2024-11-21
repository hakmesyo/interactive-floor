package com.interactivefloor.detection;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implements blob detection algorithm using flood fill method.
 * Detects connected regions in an image based on brightness threshold and range.
 */
public class BlobDetector {
    // Configuration constants
    private static final int MIN_BLOB_MASS = 100;   // Minimum blob size in pixels
    private static final int MAX_BLOB_MASS = 5000;  // Maximum blob size in pixels
    
    // Image dimensions
    private final int width;
    private final int height;
    
    // Visited pixels tracking for flood fill
    private boolean[][] visited;
    
    /**
     * Creates a new BlobDetector for the specified image dimensions.
     *
     * @param width Image width in pixels
     * @param height Image height in pixels
     */
    public BlobDetector(int width, int height) {
        this.width = width;
        this.height = height;
        this.visited = new boolean[height][width];
    }
    
    /**
     * Detects blobs in the given image using the threshold range method.
     *
     * @param image The source image to process
     * @param threshold Base threshold value for brightness
     * @param range Range around threshold to consider
     * @return List of detected blobs
     */
    public List<Blob> detectBlobs(BufferedImage image, int threshold, int range) {
        List<Blob> blobs = new ArrayList<>();
        clearVisitedArray();
        
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        
        // Scan the image for unvisited pixels within threshold range
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!visited[y][x] && isPixelInRange(pixels[y * width + x], threshold, range)) {
                    Blob blob = floodFill(pixels, x, y, threshold, range);
                    if (blob.isValidSize(MIN_BLOB_MASS, MAX_BLOB_MASS)) {
                        blobs.add(blob);
                    }
                }
            }
        }
        
        return blobs;
    }
    
    /**
     * Checks if a pixel's brightness falls within the threshold range.
     *
     * @param pixel The pixel value in RGB format
     * @param threshold Base threshold value
     * @param range Range around threshold
     * @return true if pixel is within range
     */
    private boolean isPixelInRange(int pixel, int threshold, int range) {
        // Extract red channel (most sensitive to IR)
        int brightness = (pixel >> 16) & 0xFF;
        int lowerThreshold = threshold - range;
        int upperThreshold = threshold + range;
        return brightness >= lowerThreshold && brightness <= upperThreshold;
    }
    
    /**
     * Performs flood fill algorithm starting from a seed point.
     * Collects connected pixels within threshold range into a blob.
     *
     * @param pixels Image pixel data
     * @param startX Starting X coordinate
     * @param startY Starting Y coordinate
     * @param threshold Base threshold value
     * @param range Range around threshold
     * @return The detected blob
     */
    private Blob floodFill(int[] pixels, int startX, int startY, int threshold, int range) {
        Blob blob = new Blob();
        Queue<Point> queue = new LinkedList<>();
        
        // Start flood fill from the seed point
        queue.add(new Point(startX, startY));
        visited[startY][startX] = true;
        
        // Process all connected pixels
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            blob.addPixel(p.x, p.y);
            
            // Check all 8 neighboring pixels
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nx = p.x + i;
                    int ny = p.y + j;
                    
                    if (isValidCoordinate(nx, ny) && 
                        !visited[ny][nx] && 
                        isPixelInRange(pixels[ny * width + nx], threshold, range)) {
                        
                        queue.add(new Point(nx, ny));
                        visited[ny][nx] = true;
                    }
                }
            }
        }
        
        return blob;
    }
    
    /**
     * Checks if coordinates are within image bounds.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if coordinates are valid
     */
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * Resets the visited pixels array.
     */
    private void clearVisitedArray() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                visited[y][x] = false;
            }
        }
    }
    
    /**
     * Gets the minimum allowed blob size.
     *
     * @return Minimum blob size in pixels
     */
    public int getMinBlobMass() {
        return MIN_BLOB_MASS;
    }
    
    /**
     * Gets the maximum allowed blob size.
     *
     * @return Maximum blob size in pixels
     */
    public int getMaxBlobMass() {
        return MAX_BLOB_MASS;
    }
    
    /**
     * Helper class to represent a point in the image.
     */
    private static class Point {
        final int x, y;
        
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}