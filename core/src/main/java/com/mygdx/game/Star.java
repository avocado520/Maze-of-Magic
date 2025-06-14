package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * The Star class represents a projectile fired by the player.
 * It moves in the direction of the target position and is used to hit enemies.
 */
public class Star {
    public Vector2 position;
    public Vector2 velocity;
    public static final float SPEED = 400f;
    private final Texture texture;
    /**
     * Constructs a Star object and calculates its velocity toward a target.
     * @param startPos The starting position (usually player's position).
     * @param targetPos The target position (usually where mouse was clicked).
     * @param texture The texture used to render the star.
     */
    public Star(Vector2 startPos, Vector2 targetPos, Texture texture) {
        this.texture = texture;
        this.position = new Vector2(startPos);
        this.velocity = new Vector2(targetPos).sub(startPos).nor().scl(SPEED);
    }
    /**
     * Updates the star's position based on its velocity.
     * @param delta Time since last frame (used for smooth movement).
     */
    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }
    /**
     * Returns the bounding rectangle used for collision detection.
     * @return Rectangle representing the star's hitbox.
     */
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y,
                texture.getWidth() * 0.1f, texture.getHeight() * 0.1f);
    }
    /**
     * Returns the texture used to render the star.
     * @return Texture of the star.
     */
    public Texture getTexture() {
        return texture;
    }
}
