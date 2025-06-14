package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class that sets up the initial screen and manages shared resources.
 */
public class MyGdxGame extends Game {
    /** Shared SpriteBatch used for drawing across screens */
    public SpriteBatch batch;

    /**
     * Called once when the application is started.
     * Initializes the SpriteBatch and sets the initial screen.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
    }

    /**
     * Delegates rendering to the current screen.
     */
    @Override
    public void render() {
        super.render(); // Delegates to active screen
    }
    /**
     * Releases all assets and resources.
     */
    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}