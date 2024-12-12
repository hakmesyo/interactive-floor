# Interactive Floor System API Reference

## Core Classes

### InteractiveFloor

Main system controller class.

```java
public class InteractiveFloor extends PApplet {
    public InputManager getInputManager()
    public AnimationManager getAnimationManager()
    public void setup()
    public void draw()
}
```

Example Usage:
```java
public class CustomApplication {
    public static void main(String[] args) {
        InteractiveFloor floor = new InteractiveFloor();
        floor.setup();
        floor.getAnimationManager().registerAnimation("custom", new CustomAnimation());
        floor.start();
    }
}
```

### InputManager

Handles camera input and player tracking.

```java
public class InputManager {
    public void initialize()
    public List<Player> updatePlayers()
    public int getPlayerCount()
    public int getCameraWidth()
    public int getCameraHeight()
    public void cleanup()
}
```

Configuration Example:
```java
InputManager input = new InputManager(app);
input.initialize();
List<Player> players = input.updatePlayers();
```

### AnimationManager

Manages animations and transitions.

```java
public class AnimationManager {
    public void registerAnimation(String name, Animation animation)
    public boolean setActiveAnimation(String name)
    public void update(PApplet app, Player player)
    public void draw(PApplet app)
    public Optional<Animation> getActiveAnimation()
    public void toggleMenu()
    public void setVolume(float volume)
    public boolean isMuted()
}
```

Animation Registration:
```java
AnimationManager manager = new AnimationManager(app);
manager.registerAnimation("particles", new ParticleAnimation());
manager.setActiveAnimation("particles");
```

## Player Tracking

### Player

Represents a tracked user.

```java
public class Player {
    public enum PlayerState {
        STATIC, WALKING, RUNNING, JUMPING, AIRBORNE
    }
    
    public enum MovementType {
        LINEAR, CIRCULAR, ERRATIC
    }
    
    public int getId()
    public float getX()
    public float getY()
    public PVector getPosition()
    public PVector getVelocity()
    public PlayerState getState()
    public MovementType getMovementType()
    public float getSpeed()
    public void addStateListener(PlayerStateListener listener)
}
```

Player Usage:
```java
Player player = new Player(1, x, y);
player.addStateListener(new PlayerStateListener() {
    @Override
    public void onPlayerStateChange(Player player, PlayerState newState) {
        // Handle state change
    }
});
```

### Blob Detection

#### BlobDetector

Implements motion detection.

```java
public class BlobDetector {
    public List<Blob> detectBlobs(BufferedImage image, int threshold, int range)
    public int getMinBlobMass()
    public int getMaxBlobMass()
}
```

Usage Example:
```java
BlobDetector detector = new BlobDetector(640, 480);
List<Blob> blobs = detector.detectBlobs(image, 200, 20);
```

#### BlobTracker

Maintains blob identity across frames.

```java
public class BlobTracker {
    public List<Blob> updateTracking(List<Blob> newBlobs)
    public int getTrackedBlobCount()
    public void reset()
    public Blob getTrackedBlob(int id)
}
```

Tracking Example:
```java
BlobTracker tracker = new BlobTracker();
List<Blob> trackedBlobs = tracker.updateTracking(detectedBlobs);
```

## Animation System

### Animation Interface

Base interface for all animations.

```java
public interface Animation {
    void update(PApplet app, Player player)
    void draw(PApplet app)
    void reset()
    String getName()
    default void setParameters(Object... params)
    default boolean isActive()
}
```

Custom Animation Example:
```java
public class CustomAnimation implements Animation {
    @Override
    public void update(PApplet app, Player player) {
        // Update logic
    }
    
    @Override
    public void draw(PApplet app) {
        // Drawing code
    }
}
```

### Event Listeners

#### PlayerListener

Interface for player movement events.

```java
public interface PlayerListener {
    void onPlayerMove(Player player, float x, float y, float vx, float vy)
    void onPlayerJump(Player player)
}
```

Implementation Example:
```java
public class MovementHandler implements PlayerListener {
    @Override
    public void onPlayerMove(Player player, float x, float y, float vx, float vy) {
        // Handle movement
    }
    
    @Override
    public void onPlayerJump(Player player) {
        // Handle jump
    }
}
```

#### PlayerStateListener

Interface for player state changes.

```java
public interface PlayerStateListener {
    void onPlayerStateChange(Player player, PlayerState newState)
}
```

## Sound System

### SoundManager

Handles audio playback and effects.

```java
public class SoundManager {
    public void playSound(String soundName)
    public void changeAmbience(String animationType)
    public void stopAmbience()
    public void toggleMute()
    public void setVolume(double volume)
    public boolean isMuted()
    public void cleanup()
}
```

Audio Example:
```java
SoundManager sound = new SoundManager();
sound.playSound("jump");
sound.changeAmbience("particles");
```

## Utility Classes

### DebugUtils

Debugging and development tools.

```java
public class DebugUtils {
    public static void drawDebugInfo(PApplet app, List<Player> players)
    public static void drawFrameRate(PApplet app)
    public static void drawGrid(PApplet app, int gridSize)
    public static void drawCameraBounds(PApplet app, InputManager inputManager)
    public static void drawMemoryGraph(PApplet app)
}
```

Debug Usage:
```java
DebugUtils.drawFrameRate(app);
DebugUtils.drawGrid(app, 50);
```

## Error Handling

Common exceptions and handling strategies:

```java
try {
    inputManager.initialize();
} catch (RuntimeException e) {
    System.err.println("Camera initialization failed: " + e.getMessage());
    // Handle error
}
```

## Best Practices

1. Resource Management
```java
@Override
public void dispose() {
    inputManager.cleanup();
    animationManager.cleanup();
    super.dispose();
}
```

2. Event Handling
```java
player.addStateListener(new PlayerStateListener() {
    @Override
    public void onPlayerStateChange(Player player, PlayerState newState) {
        // Use weak references for listeners
        // Clean up resources when done
    }
});
```

3. Thread Safety
```java
// Ensure thread-safe access to shared resources
synchronized(players) {
    // Update player states
}
```

4. Performance Optimization
```java
// Use object pooling for frequently created objects
private final Queue<Particle> particlePool = new LinkedList<>();
private Particle getParticle() {
    return particlePool.isEmpty() ? new Particle() : particlePool.poll();
}
```

## Configuration

Example configuration file:
```json
{
    "camera": {
        "width": 640,
        "height": 480,
        "threshold": 200
    },
    "animation": {
        "maxParticles": 1000,
        "transitionTime": 0.5
    }
}
```

## Version Information

Current API Version: 1.0
- Java 17 required
- Processing 3.3.7
- Webcam Capture 0.3.12
