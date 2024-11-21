package com.interactivefloor.test;

import processing.core.PApplet;
import processing.core.PImage;
import com.github.sarxos.webcam.Webcam;
import com.interactivefloor.detection.Blob;
import com.interactivefloor.detection.BlobDetector;
import com.interactivefloor.detection.BlobTracker;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Test program for blob detection functionality with tracking.
 * Shows camera feed and detected blobs in real-time.
 */
public class BlobDetectionTest extends PApplet {
    private Webcam webcam;
    private BlobDetector blobDetector;
    private BlobTracker blobTracker;
    private PImage cameraImage;
    private PImage binaryImage;
    private boolean showBinaryImage = false;
    private int threshold = 200;  // IR threshold value
    private int thresholdRange = 20; // Range around threshold value
    
    public static void main(String[] args) {
        PApplet.main(BlobDetectionTest.class.getName());
    }
    
    @Override
    public void settings() {
        size(1280, 720);
    }
    
    @Override
    public void setup() {
        surface.setTitle("Blob Detection Test");
        frameRate(30);
        
        // Initialize webcam
        try {
            webcam = Webcam.getDefault();
            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();
            println("Camera initialized: " + webcam.getName());
            
            // Create buffer for binary image
            binaryImage = createImage(640, 480, RGB);
            
        } catch (Exception e) {
            println("Camera error: " + e.getMessage());
            exit();
        }
        
        // Initialize detectors
        blobDetector = new BlobDetector(640, 480);
        blobTracker = new BlobTracker();
    }
    
    @Override
    public void draw() {
        background(0);
        
        if (webcam.isOpen()) {
            // Get camera image
            BufferedImage buffImg = webcam.getImage();
            if (buffImg != null) {
                cameraImage = new PImage(buffImg);
                
                // Display camera feed on the left
                image(cameraImage, 0, 0);
                
                // Process image and detect blobs with tracking
                List<Blob> detectedBlobs = blobDetector.detectBlobs(buffImg, threshold, thresholdRange);
                List<Blob> trackedBlobs = blobTracker.updateTracking(detectedBlobs);
                
                // If binary view is enabled
                if (showBinaryImage) {
                    // Prepare binary image
                    binaryImage.loadPixels();
                    for (int y = 0; y < cameraImage.height; y++) {
                        for (int x = 0; x < cameraImage.width; x++) {
                            int loc = x + y * cameraImage.width;
                            int pixel = cameraImage.pixels[loc];
                            int brightness = (pixel >> 16) & 0xFF;
                            boolean isInRange = brightness >= (threshold - thresholdRange) && 
                                              brightness <= (threshold + thresholdRange);
                            binaryImage.pixels[loc] = isInRange ? color(255) : color(0);
                        }
                    }
                    binaryImage.updatePixels();
                    
                    // Show binary image on the right
                    image(binaryImage, cameraImage.width, 0);
                }
                
                // Draw detected and tracked blobs
                pushStyle();
                noFill();
                strokeWeight(2);
                
                for (Blob blob : trackedBlobs) {
                    // Draw blob bounding box
                    stroke(0, 255, 0);
                    rect(blob.getMinX(), blob.getMinY(), 
                         blob.getWidth(), blob.getHeight());
                         
                    // Draw blob center
                    stroke(255, 0, 0);
                    float centerX = (float) blob.getCenterX();
                    float centerY = (float) blob.getCenterY();
                    line(centerX - 10, centerY, centerX + 10, centerY);
                    line(centerX, centerY - 10, centerX, centerY + 10);
                    
                    // Draw blob info
                    fill(0, 255, 0);
                    noStroke();
                    text("ID: " + blob.getId() + "\nMass: " + blob.getMass(), 
                         centerX + 15, centerY);
                         
                    // If binary view is enabled, draw on right side too
                    if (showBinaryImage) {
                        float rightCenterX = centerX + cameraImage.width;
                        
                        stroke(0, 255, 0);
                        noFill();
                        rect(blob.getMinX() + cameraImage.width, blob.getMinY(), 
                             blob.getWidth(), blob.getHeight());
                             
                        stroke(255, 0, 0);
                        line(rightCenterX - 10, centerY, rightCenterX + 10, centerY);
                        line(rightCenterX, centerY - 10, rightCenterX, centerY + 10);
                        
                        fill(0, 255, 0);
                        noStroke();
                        text("ID: " + blob.getId() + "\nMass: " + blob.getMass(), 
                             rightCenterX + 15, centerY);
                    }
                }
                popStyle();
                
                drawInterface();
            }
        }
    }
    
    private void drawInterface() {
        fill(255);
        noStroke();
        text("Threshold: " + threshold + " ± " + thresholdRange + " (UP/DOWN arrows for threshold)", 10, height - 80);
        text("Range: " + thresholdRange + " (LEFT/RIGHT arrows for range)", 10, height - 60);
        text("Toggle Binary View: B (current: " + showBinaryImage + ")", 10, height - 40);
        text("FPS: " + nf(frameRate, 0, 1), 10, height - 20);
        text("Tracked Blobs: " + blobTracker.getTrackedBlobCount(), 10, height - 100);
        
        if (cameraImage != null) {
            drawHistogram();
        }
    }
    
    private void drawHistogram() {
        int[] histogram = new int[256];
        for (int i = 0; i < cameraImage.pixels.length; i++) {
            int bright = (cameraImage.pixels[i] >> 16) & 0xFF;
            histogram[bright]++;
        }
        
        int histMax = max(histogram);
        
        // Draw histogram
        stroke(128);
        for (int i = 0; i < 256; i++) {
            float x = map(i, 0, 255, width - 280, width - 20);
            float h = map(histogram[i], 0, histMax, 0, 100);
            line(x, height - 20, x, height - 20 - h);
        }
        
        // Draw threshold range
        stroke(255, 0, 0);
        float thresholdX = map(threshold, 0, 255, width - 280, width - 20);
        line(thresholdX, height - 20, thresholdX, height - 120);
        
        stroke(255, 255, 0);
        float lowerX = map(threshold - thresholdRange, 0, 255, width - 280, width - 20);
        float upperX = map(threshold + thresholdRange, 0, 255, width - 280, width - 20);
        line(lowerX, height - 20, lowerX, height - 120);
        line(upperX, height - 20, upperX, height - 120);
    }
    
    @Override
    public void keyPressed() {
        if (key == 'b' || key == 'B') {
            showBinaryImage = !showBinaryImage;
        }
        else if (keyCode == UP) {
            threshold = constrain(threshold + 5, 0, 255);
        }
        else if (keyCode == DOWN) {
            threshold = constrain(threshold - 5, 0, 255);
        }
        else if (keyCode == LEFT) {
            thresholdRange = constrain(thresholdRange - 1, 1, 50);
        }
        else if (keyCode == RIGHT) {
            thresholdRange = constrain(thresholdRange + 1, 1, 50);
        }
    }
    
    @Override
    public void dispose() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        super.dispose();
    }
}