package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Enemy that spawns randomly around screen edges and moves toward player.
 */
public class Enemy {
    public Vector2 position;
    private Vector2 velocity;
    private final int width;
    private final int height;

    private static final float ENEMY_SCALE = 0.2f;

    /**
     * Constructor that spawns enemy away from the player to avoid instant collision.
     */
    public Enemy(int width, int height, Vector2 playerPos) {
        this.width = width;
        this.height = height;
        velocity = new Vector2();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        position = new Vector2();
        do {
            int side = MathUtils.random(3);
            switch (side) {
                case 0: position.set(MathUtils.random(screenWidth), screenHeight); break;   
                case 1: position.set(MathUtils.random(screenWidth), -height); break;         
                case 2: position.set(-width, MathUtils.random(screenHeight)); break; 
                case 3: position.set(screenWidth, MathUtils.random(screenHeight)); break;   
            }
        } while (position.dst(playerPos) < 200); 
    }

    /**
     * Updates enemy movement toward the player's position.
     */
    public void update(float delta, Vector2 playerPos) {
        velocity.set(playerPos).sub(position).nor().scl(100f);
        position.mulAdd(velocity, delta);
    }

    /**
     * Returns the enemy's collision bounds (scaled).
     */
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y,
                width * ENEMY_SCALE, height * ENEMY_SCALE);
    }
}