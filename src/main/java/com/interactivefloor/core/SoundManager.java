package com.interactivefloor.core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;

public class SoundManager {

    private final Map<String, String> soundPaths;
    private final Map<String, MediaPlayer> soundPlayers;
    private MediaPlayer currentAmbience;
    private boolean isMuted = false;
    private double volume = 1.0;

    public SoundManager() {
        // JavaFX toolkit'i başlat
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
            // Toolkit zaten başlatılmış, ignore
        }

        this.soundPaths = new HashMap<>();
        this.soundPlayers = new HashMap<>();
        initializeSoundPaths();
    }

    private void initializeSoundPaths() {
        // Menu sesleri
        soundPaths.put("intro", "/sounds/intro.wav");
        soundPaths.put("menu_select", "/sounds/menu_select.wav");
        soundPaths.put("menu_change", "/sounds/menu_change.wav");
        soundPaths.put("transition", "/sounds/transition.wav");

        // Oyuncu sesleri
        soundPaths.put("jump", "/sounds/jump.wav");
        soundPaths.put("collision", "/sounds/collision.wav");

        // Ambiyans sesleri
        soundPaths.put("particles_ambience", "/sounds/ambience.wav");
        soundPaths.put("water_ambience", "/sounds/ambience.wav");
        soundPaths.put("fire_ambience", "/sounds/ambience.wav");

        // Önceden MediaPlayer'ları oluştur
        for (Map.Entry<String, String> entry : soundPaths.entrySet()) {
            try {
                URL resource = getClass().getResource(entry.getValue());
                if (resource != null) {
                    Media media = new Media(resource.toString());
                    MediaPlayer player = new MediaPlayer(media);
                    player.setVolume(volume);
                    soundPlayers.put(entry.getKey(), player);
                }
            } catch (Exception e) {
                System.err.println("Could not load sound: " + entry.getValue());
                e.printStackTrace();
            }
        }
    }

    public void playSound(String soundName) {
        if (isMuted) {
            return;
        }

        MediaPlayer player = soundPlayers.get(soundName);
        if (player != null) {
            player.seek(Duration.ZERO);
            player.play();
        }
    }

    public void changeAmbience(String animationType) {
        stopAmbience();

        if (isMuted) {
            return;
        }

        String ambienceName = animationType + "_ambience";
        MediaPlayer player = soundPlayers.get(ambienceName);

        if (player != null) {
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.seek(Duration.ZERO);
            player.play();
            currentAmbience = player;
        }
    }

    public void stopAmbience() {
        if (currentAmbience != null) {
            currentAmbience.stop();
            currentAmbience = null;
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            stopAmbience();
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        for (MediaPlayer player : soundPlayers.values()) {
            player.setVolume(volume);
        }
    }

    public void cleanup() {
        stopAmbience();
        for (MediaPlayer player : soundPlayers.values()) {
            player.dispose();
        }
        soundPlayers.clear();

        // JavaFX toolkit'i kapat
        Platform.exit();
    }

    public boolean isMuted() {
        return isMuted;
    }
}
