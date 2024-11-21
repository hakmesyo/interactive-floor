package com.interactivefloor.player;

import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

public class Player {

    // Core properties
    private final int id;
    private PVector position;
    private PVector previousPosition;
    private PVector velocity;
    private PVector acceleration;

    // Movement states
    private PlayerState currentState;
    private MovementType movementType;

    // Movement thresholds
    private static final float WALK_SPEED_THRESHOLD = 2.0f;
    private static final float RUN_SPEED_THRESHOLD = 5.0f;
    private static final float JUMP_THRESHOLD = 15.0f;
    private static final float STATIONARY_THRESHOLD = 0.5f;

    // Time tracking
    private long lastUpdateTime;
    private long stateStartTime;

    // Movement history
    private List<PVector> positionHistory;
    private static final int HISTORY_SIZE = 10;

    public enum PlayerState {
        STATIC,
        WALKING,
        RUNNING,
        JUMPING,
        AIRBORNE    // Jumping while moving
    }

    public enum MovementType {
        LINEAR,
        CIRCULAR,
        ERRATIC
    }

    public Player(int id, float x, float y) {
        this.id = id;
        this.position = new PVector(x, y);
        this.previousPosition = new PVector(x, y);
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        this.currentState = PlayerState.STATIC;
        this.movementType = MovementType.LINEAR;
        this.lastUpdateTime = System.currentTimeMillis();
        this.stateStartTime = lastUpdateTime;
        this.positionHistory = new ArrayList<>();
    }

    public void update(float x, float y) {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000f;

        // Update position history
        if (positionHistory.size() >= HISTORY_SIZE) {
            positionHistory.remove(0);
        }
        positionHistory.add(position.copy());

        // Update positions and calculate velocity
        previousPosition.set(position);
        position.set(x, y);

        PVector newVelocity = PVector.sub(position, previousPosition);
        newVelocity.div(deltaTime);

        // Apply smoothing to velocity
        velocity.lerp(newVelocity, 0.3f);

        // Calculate acceleration
        acceleration = PVector.sub(velocity, previousPosition);
        acceleration.div(deltaTime);

        // Update player state
        updateState();

        // Update movement type
        updateMovementType();

        lastUpdateTime = currentTime;
    }

    private void updateState() {
        float speed = velocity.mag();
        float verticalChange = previousPosition.y - position.y;

        PlayerState newState;

        if (verticalChange > JUMP_THRESHOLD) {
            newState = speed > WALK_SPEED_THRESHOLD ? PlayerState.AIRBORNE : PlayerState.JUMPING;
        } else if (speed < STATIONARY_THRESHOLD) {
            newState = PlayerState.STATIC;
        } else if (speed < WALK_SPEED_THRESHOLD) {
            newState = PlayerState.WALKING;
        } else {
            newState = PlayerState.RUNNING;
        }

        if (newState != currentState) {
            currentState = newState;
            stateStartTime = System.currentTimeMillis();
            notifyStateChange();
        }
    }

    private void updateMovementType() {
        if (positionHistory.size() < HISTORY_SIZE) {
            return;
        }

        // Check for circular motion
        float angleSum = 0;
        for (int i = 2; i < positionHistory.size(); i++) {
            PVector v1 = PVector.sub(positionHistory.get(i - 1), positionHistory.get(i - 2));
            PVector v2 = PVector.sub(positionHistory.get(i), positionHistory.get(i - 1));
            angleSum += PVector.angleBetween(v1, v2);
        }

        // Determine movement pattern
        if (Math.abs(angleSum) > Math.PI * 1.5f) {
            movementType = MovementType.CIRCULAR;
        } else if (acceleration.mag() > 10) {
            movementType = MovementType.ERRATIC;
        } else {
            movementType = MovementType.LINEAR;
        }
    }

    // Event handling
    private List<PlayerStateListener> stateListeners = new ArrayList<>();

    public void addStateListener(PlayerStateListener listener) {
        if (!stateListeners.contains(listener)) {
            stateListeners.add(listener);
        }
    }

    private void notifyStateChange() {
        for (PlayerStateListener listener : stateListeners) {
            listener.onPlayerStateChange(this, currentState);
        }
    }

    // Collision detection with other players
    public boolean isColliding(Player other) {
        float distance = PVector.dist(position, other.position);
        return distance < 30; // Adjust collision radius as needed
    }

    // Getters
    public int getId() {
        return id;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public PVector getPosition() {
        return position.copy();
    }

    public PVector getVelocity() {
        return velocity.copy();
    }

    public PlayerState getState() {
        return currentState;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public float getSpeed() {
        return velocity.mag();
    }

    public float getStateTime() {
        return (System.currentTimeMillis() - stateStartTime) / 1000f;
    }
}
