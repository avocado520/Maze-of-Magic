package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Represents a placeholder mini game screen.
 */
public class MiniGameScreen implements Screen {

    private final MyGdxGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    /**
     * Constructs the mini game screen.
     * @param game Main game reference
     */
    public MiniGameScreen(MyGdxGame game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont(); // Default font
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Mini Game Screen ", 100, 200);
        batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Disposes resources.
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
