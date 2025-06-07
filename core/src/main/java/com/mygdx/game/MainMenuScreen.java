package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

/**
 * Main menu screen displaying background, title image, start button and exit button.
 */
public class MainMenuScreen implements Screen {
    private final MyGdxGame game;
    private Texture background;
    private Texture startButton;
    private Texture exitButton;
    private Texture titleImage;

    private int screenWidth;
    private int screenHeight;

    /**
     * Initializes the main menu screen and loads assets.
     * @param game Main game reference
     */
    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        background = new Texture("background.png");
        startButton = new Texture("start.png");
        exitButton = new Texture("exit.png");
        titleImage = new Texture("mazeofmagic.png");

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(background, 0, 0, screenWidth, screenHeight);

        float titleScale = 0.6f;
        float titleWidth = titleImage.getWidth() * titleScale;
        float titleHeight = titleImage.getHeight() * titleScale;
        float titleX = (screenWidth - titleWidth) / 2f;
        float titleY = screenHeight - titleHeight - 100;
        game.batch.draw(titleImage, titleX, titleY, titleWidth, titleHeight);

        float buttonScale = 0.5f;
        float buttonWidth = startButton.getWidth() * buttonScale;
        float buttonHeight = startButton.getHeight() * buttonScale;
        float spacing = 60f;

        float totalWidth = buttonWidth * 2 + spacing;
        float startX = (screenWidth - totalWidth) / 2f;
        float exitX = startX + buttonWidth + spacing;
        float buttonY = screenHeight / 2f - buttonHeight / 2f - 280;

        game.batch.draw(startButton, startX, buttonY, buttonWidth, buttonHeight);

        game.batch.draw(exitButton, exitX, buttonY, buttonWidth, buttonHeight);

        game.batch.end();

        // === Input Detection for Both Buttons ===
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = screenHeight - Gdx.input.getY();

            if (touchX >= startX && touchX <= startX + buttonWidth &&
                touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                game.setScreen(new GameScreen(game));
                dispose();
            }

            if (touchX >= exitX && touchX <= exitX + buttonWidth &&
                touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                Gdx.app.exit();
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void show() {}

    /**
     * Disposes of all resources used by the screen.
     */
    @Override
    public void dispose() {
        background.dispose();
        startButton.dispose();
        exitButton.dispose();
        titleImage.dispose();
    }
}
