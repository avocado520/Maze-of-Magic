package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents an enemy that spawns randomly around the screen and moves toward the player.
 */
public class Enemy {
    public Vector2 position;
    private Vector2 velocity;
    private final int width;
    private final int height;

    /**
     * Constructs an enemy with a fixed size that spawns on a random edge.
     * @param width Width of the enemy texture
     * @param height Height of the enemy texture
     */
    public Enemy(int width, int height) {
        this.width = width;
        this.height = height;

        position = new Vector2();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // Random spawn from top, bottom, left, or right
        int side = MathUtils.random(3);
        switch (side) {
            case 0: // Top
                position.set(MathUtils.random(screenWidth), screenHeight);
                break;
            case 1: // Bottom
                position.set(MathUtils.random(screenWidth), -height);
                break;
            case 2: // Left
                position.set(-width, MathUtils.random(screenHeight));
                break;
            case 3: // Right
                position.set(screenWidth, MathUtils.random(screenHeight));
                break;
        }

        velocity = new Vector2();
    }

    /**
     * Updates enemy movement toward the player's position.
     * @param delta Time since last frame
     * @param playerPos Position of the player
     */
    public void update(float delta, Vector2 playerPos) {
        velocity.set(playerPos).sub(position).nor().scl(100f);
        position.mulAdd(velocity, delta);
    }

    /**
     * @return A rectangle representing the enemy's current position and size
     */
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }
}
