package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Sound;


import java.util.ArrayList;
/**
 * MiniGameScreen displays a mini-game where players must click buttons in a correct sequence.
 * Upon success, a win image and sound are shown, and the player can exit the game.
 */
public class MiniGameScreen implements Screen {
    private final MyGdxGame game;
    private Stage stage;
    private Texture background;
    private Texture winImage;
    private Texture exitTexture;

    private final ArrayList<Integer> userInput = new ArrayList<>();
    private final int[] correctOrder = {0, 1, 2, 3};

    private Table mainTable;
    private Image winDisplay;
    private ImageButton exitButton;

    private Sound winSound;

    /**
     * Constructs the MiniGameScreen, initializes UI elements and loads assets.
     * @param game Reference to the main game class.
     */
    public MiniGameScreen(MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = new Texture("background3.png");
        winImage = new Texture("you win.png");
        exitTexture = new Texture("exit.png");

        winSound = Gdx.audio.newSound(Gdx.files.internal("win.wav"));

        Skin skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default", font);
        TextButtonStyle textStyle = new TextButtonStyle();
        textStyle.font = font;
        skin.add("default", textStyle);

        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().center();
        stage.addActor(mainTable);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label instructionLabel = new Label("Try to click the buttons in the correct order", labelStyle);
        instructionLabel.setFontScale(2f);
        instructionLabel.setAlignment(Align.center);
        mainTable.add(instructionLabel).colspan(4).padTop(40).padBottom(60).row();

        String[] normalPaths = {
                "btn_red.png", "btn_green.png", "btn_blue.png", "btn_yellow.png"
        };
        String[] selectedPaths = {
                "btn_red_selected.png", "btn_green_selected.png", "btn_blue_selected.png", "btn_yellow_selected.png"
        };

        for (int i = 0; i < 4; i++) {
            final int index = i;

            TextureRegionDrawable up = new TextureRegionDrawable(new TextureRegion(new Texture(normalPaths[i])));
            TextureRegionDrawable down = new TextureRegionDrawable(new TextureRegion(new Texture(selectedPaths[i])));

            ImageButton button = new ImageButton(up, down);
            button.setSize(128, 128);
            button.getImageCell().size(128, 128);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleInput(index);
                }
            });

            mainTable.add(button).pad(25);
        }

        winDisplay = new Image(winImage);
        winDisplay.setVisible(false);
        winDisplay.setSize(600, 300);
        winDisplay.setPosition(
                Gdx.graphics.getWidth() / 2f - 300,
                Gdx.graphics.getHeight() / 2f + 60
        );
        stage.addActor(winDisplay);

        TextureRegionDrawable exitDrawable = new TextureRegionDrawable(new TextureRegion(exitTexture));
        exitButton = new ImageButton(exitDrawable);
        exitButton.setVisible(false);
        exitButton.setSize(669, 300);
        exitButton.setPosition(
        Gdx.graphics.getWidth() / 2f - 369 / 2f,
        Gdx.graphics.getHeight() / 2f - 300
    );

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);
    }
    /**
     * Handles button clicks in the mini-game. If the correct sequence is entered, it shows the win image and exits.
     * @param selected Index of the clicked button.
     */
    private void handleInput(int selected) {
        userInput.add(selected);

        if (userInput.size() == correctOrder.length) {
            boolean correct = true;
            for (int i = 0; i < correctOrder.length; i++) {
                if (!userInput.get(i).equals(correctOrder[i])) {
                    correct = false;
                    break;
                }
            }

            if (correct) {
                winDisplay.setVisible(true);
                exitButton.setVisible(true);
                mainTable.setVisible(false);
                winSound.play(); 
            } else {
                userInput.clear();
            }
        }
    }


    @Override
    public void show() {}
    /**
     * Renders the background and UI each frame.
     * @param delta Time since the last frame.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        float offsetX = 1200f; 
        game.batch.draw(background, offsetX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }
    /**
     * Called when the screen is resized.
     * @param width New width.
     * @param height New height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    /**
     * Releases all assets and resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        winImage.dispose();
        exitTexture.dispose();
        winSound.dispose();
    }
}
