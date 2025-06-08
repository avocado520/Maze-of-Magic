package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {

    private final MyGdxGame game;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture gameOverTexture;
    private Texture restartTexture;
    private Texture exitTexture;
    private Image gameOverImage;
    private ImageButton restartButton;
    private ImageButton exitButton;

    public GameOverScreen(MyGdxGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("background4.png");
        gameOverTexture = new Texture("GameOver.png");
        restartTexture = new Texture("restart.png");
        exitTexture = new Texture("exit.png");

        Image bg = new Image(backgroundTexture);
        bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bg.setPosition(0, 0);
        stage.addActor(bg);

        gameOverImage = new Image(gameOverTexture);
        gameOverImage.setSize(800, 320);
        gameOverImage.setPosition(
                (Gdx.graphics.getWidth() - 800) / 2f,
                Gdx.graphics.getHeight() / 2f
        );
        stage.addActor(gameOverImage);

        TextureRegionDrawable restartDrawable = new TextureRegionDrawable(new TextureRegion(restartTexture));
        restartButton = new ImageButton(restartDrawable);
        restartButton.setSize(1200, 440);
        restartButton.setPosition(
                Gdx.graphics.getWidth() / 4f - 600,
                100
        );
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(restartButton);

        TextureRegionDrawable exitDrawable = new TextureRegionDrawable(new TextureRegion(exitTexture));
        exitButton = new ImageButton(exitDrawable);
        exitButton.setSize(1200, 440);
        exitButton.setPosition(
                Gdx.graphics.getWidth() * 3f / 4f - 600,
                100
        );
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        gameOverTexture.dispose();
        restartTexture.dispose();
        exitTexture.dispose();
    }
}
