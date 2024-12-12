# Interactive Floor System Animation Guide

## Animation System Overview

The animation system is built on a flexible, event-driven architecture that allows for real-time visual responses to player movements. All animations implement the `Animation` interface and can be easily integrated into the system.

## Core Animation Interface

```java
public interface Animation {
    void update(PApplet app, Player player);
    void draw(PApplet app);
    void reset();
    String getName();
}
```

## Built-in Animations

### 1. Particle Animation
A versatile particle system that responds to player movement.

Key Features:
- Dynamic particle generation
- Physics-based movement
- Color and size variation
- Player velocity influence

Implementation Example:
```java
public class ParticleAnimation implements Animation, PlayerListener {
    private static final int MAX_PARTICLES = 1000;
    private static final float BASE_SPEED = 2.0f;
    
    @Override
    public void update(PApplet app, Player player) {
        float speed = player.getSpeed();
        if (speed > 0.1f) {
            createParticlesForPlayer(player, app);
        }
        updateParticles();
    }
}
```

Settings:
- Particle count: 1000 maximum
- Particle lifespan: 255 frames
- Base speed: 2.0 units
- Size range: 4-8 pixels

### 2. Fire Animation
Creates dynamic fire effects that follow player movement.

Features:
- Realistic flame behavior
- Color gradient effects
- Intensity based on movement
- Additive blending

Key Components:
```java
public class FireParticle {
    float x, y;
    float vx, vy;
    float brightness;
    float alpha;
    float size;
    // Physics and rendering properties
}
```

### 3. Water Animation
Simulates fluid-like motion responding to player interaction.

Characteristics:
- Wave propagation
- Ripple effects
- Surface reflection
- Transparency effects

### 4. Logo Animation
Special animation for system introduction and transitions.

Stages:
1. Growing: Logo scales up
2. Stable: Static display
3. Exploding: Particle dispersion
4. Reforming: Particles reconverge
5. Fading: Gradual disappearance

## Creating Custom Animations

### Basic Template
```java
public class CustomAnimation implements Animation {
    @Override
    public void update(PApplet app, Player player) {
        // Update animation state based on player
    }
    
    @Override
    public void draw(PApplet app) {
        // Draw the animation
    }
    
    @Override
    public void reset() {
        // Reset animation state
    }
    
    @Override
    public String getName() {
        return "Custom Animation";
    }
}
```

### Player Interaction
Implement PlayerListener for enhanced interaction:
```java
public class CustomAnimation implements Animation, PlayerListener {
    @Override
    public void onPlayerMove(Player player, float x, float y, float vx, float vy) {
        // React to player movement
    }
    
    @Override
    public void onPlayerJump(Player player) {
        // Special effects for jumps
    }
}
```

## Animation Management

### Registration
```java
AnimationManager manager = new AnimationManager(app);
manager.registerAnimation("custom", new CustomAnimation());
```

### Transitions
The system supports smooth transitions between animations:
1. Fade out current animation
2. Initialize new animation
3. Fade in new animation

Transition timing: 0.5 seconds default

## Performance Optimization

### Particle System Tips
1. Use object pooling
2. Limit particle count
3. Optimize drawing calls
4. Use efficient data structures

Example Pool Implementation:
```java
public class ParticlePool {
    private Queue<Particle> pool;
    private static final int POOL_SIZE = 1000;
    
    public Particle acquire() {
        return pool.isEmpty() ? new Particle() : pool.poll();
    }
    
    public void release(Particle particle) {
        if (pool.size() < POOL_SIZE) {
            pool.offer(particle);
        }
    }
}
```

### Drawing Optimization
1. Use PGraphics for complex effects
2. Batch similar drawing operations
3. Minimize state changes
4. Use appropriate blend modes

## Event System Integration

### Animation Events
1. PlayerMove: Position updates
2. PlayerJump: Jump detection
3. StateChange: Player state transitions

### Event Handling Example
```java
@Override
public void onPlayerStateChange(Player player, PlayerState newState) {
    switch (newState) {
        case JUMPING:
            createJumpEffect(player);
            break;
        case RUNNING:
            increaseIntensity(player);
            break;
        // Handle other states
    }
}
```

## Debug Support

### Visual Debugging
1. Particle count display
2. Performance metrics
3. State visualization
4. Boundary display

### Debug Controls
- 'D': Toggle debug mode
- Mouse interaction testing
- Parameter adjustment
- State inspection

## Best Practices

### Animation Design
1. Keep effects modular
2. Use consistent style
3. Consider performance
4. Handle edge cases
5. Implement proper cleanup

### Resource Management
1. Dispose unused resources
2. Monitor memory usage
3. Clean up on reset
4. Handle transitions properly

## Examples

### Simple Effect
```java
public class RippleAnimation implements Animation {
    private List<Ripple> ripples = new ArrayList<>();
    
    @Override
    public void update(PApplet app, Player player) {
        ripples.add(new Ripple(player.getX(), player.getY()));
        ripples.removeIf(Ripple::isDone);
        ripples.forEach(Ripple::update);
    }
    
    @Override
    public void draw(PApplet app) {
        app.pushStyle();
        app.noFill();
        ripples.forEach(r -> r.draw(app));
        app.popStyle();
    }
}
```

### Complex Effect
```java
public class ParticleFieldAnimation implements Animation {
    private ParticleSystem system;
    private ForceField field;
    
    @Override
    public void update(PApplet app, Player player) {
        field.applyForce(player.getPosition(), player.getVelocity());
        system.update(field);
    }
}
```

## Troubleshooting

### Common Issues
1. Performance problems
   - Reduce particle count
   - Optimize drawing
   - Check event handlers
   
2. Visual glitches
   - Verify blend modes
   - Check drawing order
   - Update view bounds

3. Memory leaks
   - Implement proper cleanup
   - Use object pooling
   - Monitor resource usage
