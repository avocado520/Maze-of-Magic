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

/**
 * Main game screen where player avoids enemies and reaches the magic circle.
 */
public class GameScreen implements Screen {
    private final MyGdxGame game;

    private Texture background;
    private Texture playerTexture;
    private Texture enemyTexture;
    private Texture goalTexture;

    private Vector2 playerPos;
    private Rectangle playerBounds;
    private Rectangle goalBounds;

    private static final float PLAYER_SPEED = 200f;
    private static final float ENEMY_SPEED = 100f;

    private Array<Enemy> enemies;
    private float enemySpawnTimer;
    private float enemySpawnInterval = 2f; // seconds

    /**
     * Constructs the main game screen.
     * @param game Main game reference
     */
    public GameScreen(MyGdxGame game) {
        this.game = game;

        background = new Texture("background.png");
        playerTexture = new Texture("player.png");
        enemyTexture = new Texture("enemy.png");
        goalTexture = new Texture("magic.png");

        playerPos = new Vector2(100, 100);
        playerBounds = new Rectangle(playerPos.x, playerPos.y, playerTexture.getWidth(), playerTexture.getHeight());

        // Place goal at lower right corner
        float goalX = Gdx.graphics.getWidth() - 120;
        float goalY = 50;
        goalBounds = new Rectangle(goalX, goalY, goalTexture.getWidth(), goalTexture.getHeight());

        enemies = new Array<>();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(goalTexture, goalBounds.x, goalBounds.y);
        game.batch.draw(playerTexture, playerPos.x, playerPos.y);

        for (Enemy e : enemies) {
            game.batch.draw(enemyTexture, e.position.x, e.position.y);
        }
        game.batch.end();
    }

    /**
     * Handles player input, enemy logic, and collision.
     */
    private void update(float delta) {
        handleInput(delta);

        // Spawn new enemies
        enemySpawnTimer += delta;
        if (enemySpawnTimer >= enemySpawnInterval) {
            enemies.add(new Enemy(enemyTexture.getWidth(), enemyTexture.getHeight()));
            enemySpawnTimer = 0;
        }

        // Update enemies and check collision
        Rectangle playerRect = new Rectangle(playerPos.x, playerPos.y, playerTexture.getWidth(), playerTexture.getHeight());
        for (Enemy e : enemies) {
            e.update(delta, playerPos);
            if (playerRect.overlaps(e.getBounds())) {
                System.out.println("Game Over");
                game.setScreen(new GameOverScreen(game));
            }
        }

        // Check if player reached goal
        if (playerRect.overlaps(goalBounds)) {
            System.out.println("Reached goal. MiniGame unlocked!");
            game.setScreen(new MiniGameScreen(game));
        }
    }

    /**
     * Handles WASD/arrow key movement, clamped to screen.
     */
    private void handleInput(float delta) {
        float moveX = 0;
        float moveY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) moveY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveY -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveX -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveX += 1;

        float length = (float)Math.sqrt(moveX * moveX + moveY * moveY);
        if (length > 0) {
            moveX /= length;
            moveY /= length;
        }

        playerPos.x += moveX * PLAYER_SPEED * delta;
        playerPos.y += moveY * PLAYER_SPEED * delta;

        playerPos.x = MathUtils.clamp(playerPos.x, 0, Gdx.graphics.getWidth() - playerTexture.getWidth());
        playerPos.y = MathUtils.clamp(playerPos.y, 0, Gdx.graphics.getHeight() - playerTexture.getHeight());
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
