package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Star {
    public Vector2 position;
    public Vector2 velocity;
    public static final float SPEED = 400f;
    private final Texture texture;

    public Star(Vector2 startPos, Vector2 targetPos, Texture texture) {
        this.texture = texture;
        this.position = new Vector2(startPos);
        this.velocity = new Vector2(targetPos).sub(startPos).nor().scl(SPEED);
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y,
                texture.getWidth() * 0.1f, texture.getHeight() * 0.1f);
    }

    public Texture getTexture() {
        return texture;
    }
}
