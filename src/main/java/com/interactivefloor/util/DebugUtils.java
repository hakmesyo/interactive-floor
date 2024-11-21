/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interactivefloor.util;

import processing.core.PApplet;
import com.interactivefloor.player.Player;
import java.util.List;

/**
 * Utility class for debugging and visualization purposes. Provides methods to
 * draw debug information on screen.
 */
public class DebugUtils {

    private static final int DEBUG_TEXT_COLOR = 0xFFFFFF00;  // Yellow
    private static final int DEBUG_SHAPE_COLOR = 0xFFFF0000; // Red
    private static final int BACKGROUND_COLOR = 0x77000000;  // Semi-transparent black
    private static final int TEXT_SIZE = 14;
    private static final int PADDING = 10;

    /**
     * Private constructor to prevent instantiation.
     */
    private DebugUtils() {
        // Utility class should not be instantiated
    }

    /**
     * Draws debug information for all players.
     *
     * @param app Processing app instance
     * @param players List of players to debug
     */
    public static void drawDebugInfo(PApplet app, List<Player> players) {
        app.pushStyle();

        // Draw player information
        for (Player player : players) {
            drawPlayerDebug(app, player);
        }

        // Draw system statistics
        drawSystemStats(app, players);

        app.popStyle();
    }

    /**
     * Draws debug information for a single player.
     *
     * @param app Processing app instance
     * @param player Player to debug
     */
    private static void drawPlayerDebug(PApplet app, Player player) {
        // Draw player position marker
        app.stroke(DEBUG_SHAPE_COLOR);
        app.noFill();
        app.ellipse(player.getX(), player.getY(), 30, 30);

        // Draw velocity vector
        float velocityScale = 10.0f;
        app.line(
                player.getX(),
                player.getY(),
                player.getX() + player.getX() * velocityScale,
                player.getY() + player.getY() * velocityScale
        );

        // Draw player info text
        app.fill(BACKGROUND_COLOR);
        app.noStroke();
        app.rect(
                player.getX() - 50,
                player.getY() - 50,
                100,
                40
        );

        app.fill(DEBUG_TEXT_COLOR);
        app.textSize(TEXT_SIZE);
        app.textAlign(PApplet.CENTER);

        String info = String.format(
                "ID: %d\nPos: (%.1f, %.1f)",
                player.getId(),
                player.getX(),
                player.getY()
        );
        app.text(info, player.getX(), player.getY() - 30);

        // Draw jump indicator if player is jumping
        if (player.getState().equals(Player.PlayerState.JUMPING)) {
            app.text("JUMPING!", player.getX(), player.getY() - 60);
        }
    }

    /**
     * Draws system statistics and performance metrics.
     *
     * @param app Processing app instance
     * @param players List of current players
     */
    private static void drawSystemStats(PApplet app, List<Player> players) {
        app.fill(BACKGROUND_COLOR);
        app.noStroke();
        app.rect(PADDING, PADDING, 200, 80);

        app.fill(DEBUG_TEXT_COLOR);
        app.textSize(TEXT_SIZE);
        app.textAlign(PApplet.LEFT);

        String stats = String.format(
                "FPS: %.1f\n"
                + "Players: %d\n"
                + "Frame: %d\n"
                + "Memory: %.1f MB",
                app.frameRate,
                players.size(),
                app.frameCount,
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576f
        );

        app.text(stats, 2 * PADDING, 2 * PADDING);
    }

    /**
     * Draws frame rate in the corner of the screen.
     *
     * @param app Processing app instance
     */
    public static void drawFrameRate(PApplet app) {
        app.pushStyle();

        app.fill(BACKGROUND_COLOR);
        app.noStroke();
        app.rect(
                app.width - 70,
                PADDING,
                60,
                25
        );

        app.fill(DEBUG_TEXT_COLOR);
        app.textSize(TEXT_SIZE);
        app.textAlign(PApplet.RIGHT);
        app.text(
                String.format("%.1f", app.frameRate),
                app.width - PADDING,
                2 * PADDING
        );

        app.popStyle();
    }

    /**
     * Draws a grid overlay for spatial reference.
     *
     * @param app Processing app instance
     * @param gridSize Size of each grid cell
     */
    public static void drawGrid(PApplet app, int gridSize) {
        app.pushStyle();

        app.stroke(DEBUG_SHAPE_COLOR, 40);
        app.strokeWeight(1);

        // Draw vertical lines
        for (int x = 0; x < app.width; x += gridSize) {
            app.line(x, 0, x, app.height);
        }

        // Draw horizontal lines
        for (int y = 0; y < app.height; y += gridSize) {
            app.line(0, y, app.width, y);
        }

        app.popStyle();
    }

    /**
     * Draws memory usage graph in the corner.
     *
     * @param app Processing app instance
     */
    public static void drawMemoryGraph(PApplet app) {
        int graphWidth = 100;
        int graphHeight = 40;
        int x = app.width - graphWidth - PADDING;
        int y = app.height - graphHeight - PADDING;

        app.pushStyle();

        // Draw background
        app.fill(BACKGROUND_COLOR);
        app.noStroke();
        app.rect(x, y, graphWidth, graphHeight);

        // Draw memory usage
        float memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                / (float) Runtime.getRuntime().maxMemory();

        app.fill(
                PApplet.map(memoryUsage, 0, 1, 0, 255),
                PApplet.map(memoryUsage, 0, 1, 255, 0),
                0
        );

        app.rect(
                x,
                y + graphHeight * (1 - memoryUsage),
                graphWidth,
                graphHeight * memoryUsage
        );

        app.popStyle();
    }
}
