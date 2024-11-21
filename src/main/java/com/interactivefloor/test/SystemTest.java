/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.test;

import processing.core.PApplet;
import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;

/**
 * Basic system test to verify camera and display functionality.
 */
public class SystemTest extends PApplet {

    private Webcam webcam;
    private boolean cameraWorking = false;
    private long lastFrameTime;
    private float currentFps;

    public static void main(String[] args) {
        PApplet.main(SystemTest.class.getName());
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
        background(0);
        frameRate(60);
        textSize(16);

        // Initialize camera
        try {
            println("Available cameras:");
            for (Webcam cam : Webcam.getWebcams()) {
                println("- " + cam.getName());
            }

            webcam = Webcam.getDefault();
            if (webcam != null) {
                webcam.setViewSize(new Dimension(640, 480));
                webcam.open();
                cameraWorking = webcam.isOpen();
                println("Camera initialized: " + webcam.getName());
            }
        } catch (Exception e) {
            println("Camera error: " + e.getMessage());
        }

        lastFrameTime = System.nanoTime();
    }

    @Override
    public void draw() {
        // Clear background
        background(0);

        // Calculate FPS
        long currentTime = System.nanoTime();
        float frameTime = (currentTime - lastFrameTime) / 1e9f; // Convert to seconds
        currentFps = 1.0f / frameTime;
        lastFrameTime = currentTime;

        // Draw system status
        fill(255);
        text("System Test", 20, 30);
        text("FPS: " + nf(currentFps, 0, 1), 20, 60);
        text("Camera Status: " + (cameraWorking ? "OK" : "Not Working"), 20, 90);

        if (webcam != null && webcam.isOpen()) {
            // Try to get camera image
            try {
                text("Image Size: " + webcam.getViewSize().width
                        + "x" + webcam.getViewSize().height, 20, 120);

                if (webcam.getImage() != null) {
                    text("Camera Feed: Active", 20, 150);
                } else {
                    text("Camera Feed: No Image", 20, 150);
                }
            } catch (Exception e) {
                text("Camera Error: " + e.getMessage(), 20, 150);
            }
        }

        // Memory usage
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        text(String.format("Memory Usage: %.1f MB", usedMemory / 1048576.0f), 20, 180);

        // Draw test pattern
        drawTestPattern();

        // Instructions
        fill(200);
        text("Press 'ESC' to exit", 20, height - 20);
    }

    private void drawTestPattern() {
        // Draw a moving circle to verify rendering
        float t = frameCount * 0.05f;
        float x = width / 2 + cos(t) * 100;
        float y = height / 2 + sin(t) * 100;

        noFill();
        stroke(255);
        ellipse(x, y, 30, 30);

        // Draw coordinate grid
        stroke(50);
        for (int i = 0; i < width; i += 50) {
            line(i, 0, i, height);
        }
        for (int i = 0; i < height; i += 50) {
            line(0, i, width, i);
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
