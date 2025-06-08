package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private final MyGdxGame game;

    private Texture background;
    private Texture playerTexture;
    private Texture enemyTexture;
    private Texture goalTexture;

    private Vector2 playerPos;
    private Rectangle goalBounds;

    private static final float PLAYER_SPEED = 200f;
    private static final float PLAYER_SCALE = 0.2f;
    private static final float ENEMY_SCALE = 0.2f;
    private static final float GOAL_SCALE = 0.4f;

    private Array<Enemy> enemies;
    private float enemySpawnTimer;
    private float enemySpawnInterval = 2f;

    public GameScreen(MyGdxGame game) {
        this.game = game;

        background = new Texture("background2.png");
        playerTexture = new Texture("player.png");
        enemyTexture = new Texture("enemy.png");
        goalTexture = new Texture("aim.png");

        playerPos = new Vector2(100, 100);

        float goalW = goalTexture.getWidth() * GOAL_SCALE;
        float goalH = goalTexture.getHeight() * GOAL_SCALE;
        float goalX = Gdx.graphics.getWidth() - goalW - 20;
        float goalY = 50;
        goalBounds = new Rectangle(goalX, goalY, goalW, goalH);

        enemies = new Array<>();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(goalTexture, goalBounds.x, goalBounds.y, goalBounds.width, goalBounds.height);
        game.batch.draw(playerTexture, playerPos.x, playerPos.y,
                playerTexture.getWidth() * PLAYER_SCALE, playerTexture.getHeight() * PLAYER_SCALE);

        for (Enemy e : enemies) {
            game.batch.draw(enemyTexture, e.position.x, e.position.y,
                    enemyTexture.getWidth() * ENEMY_SCALE, enemyTexture.getHeight() * ENEMY_SCALE);
        }

        game.batch.end();
    }

    private void update(float delta) {
        handleInput(delta);

        enemySpawnTimer += delta;
        if (enemySpawnTimer >= enemySpawnInterval) {
            enemies.add(new Enemy(enemyTexture.getWidth(), enemyTexture.getHeight(), playerPos));
            enemySpawnTimer = 0;
        }

        float playerW = playerTexture.getWidth() * PLAYER_SCALE;
        float playerH = playerTexture.getHeight() * PLAYER_SCALE;

        float playerCenterX = playerPos.x + playerW / 2f;
        float playerCenterY = playerPos.y + playerH / 2f;
        float playerRadius = playerW / 2f;

        Vector2 playerCenter = new Vector2(playerCenterX, playerCenterY);

        for (Enemy e : enemies) {
            e.update(delta, playerPos);

            float enemyW = enemyTexture.getWidth() * ENEMY_SCALE;
            float enemyH = enemyTexture.getHeight() * ENEMY_SCALE;
            float enemyCenterX = e.position.x + enemyW / 2f;
            float enemyCenterY = e.position.y + enemyH / 2f;
            float enemyRadius = enemyW / 2f;

            Vector2 enemyCenter = new Vector2(enemyCenterX, enemyCenterY);

            float distance = playerCenter.dst(enemyCenter);

            if (distance < playerRadius + enemyRadius * 0.8f) {
                game.setScreen(new GameOverScreen(game));
                return;
            }
        }

        Rectangle playerRect = new Rectangle(playerPos.x, playerPos.y, playerW, playerH);
        if (playerRect.overlaps(goalBounds)) {
            game.setScreen(new MiniGameScreen(game));
        }
    }

    private void handleInput(float delta) {
        float moveX = 0;
        float moveY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) moveY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveY -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveX -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveX += 1;

        float length = (float) Math.sqrt(moveX * moveX + moveY * moveY);
        if (length > 0) {
            moveX /= length;
            moveY /= length;
        }

        playerPos.x += moveX * PLAYER_SPEED * delta;
        playerPos.y += moveY * PLAYER_SPEED * delta;

        float maxX = Gdx.graphics.getWidth() - playerTexture.getWidth() * PLAYER_SCALE;
        float maxY = Gdx.graphics.getHeight() - playerTexture.getHeight() * PLAYER_SCALE;

        playerPos.x = MathUtils.clamp(playerPos.x, 0, maxX);
        playerPos.y = MathUtils.clamp(playerPos.y, 0, maxY);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void show() {}

    @Override
    public void dispose() {
        background.dispose();
        playerTexture.dispose();
        enemyTexture.dispose();
        goalTexture.dispose();
    }
}

