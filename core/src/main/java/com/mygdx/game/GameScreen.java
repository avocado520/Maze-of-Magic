package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private final MyGdxGame game;

    private OrthographicCamera camera;

    private Texture background;
    private Texture playerTexture;
    private Texture enemyTexture;
    private Texture goalTexture;
    private Texture starTexture;

    private Vector2 playerPos;
    private Rectangle goalBounds;

    private static final float PLAYER_SPEED = 200f;
    private static final float PLAYER_SCALE = 0.2f;
    private static final float ENEMY_SCALE = 0.2f;
    private static final float GOAL_SCALE = 0.4f;
    private static final float STAR_SCALE = 0.1f;

    private Array<Enemy> enemies;
    private Array<Star> stars;
    private float enemySpawnTimer;
    private float enemySpawnInterval = 2f;

    private int worldWidth;
    private int worldHeight;

    public GameScreen(MyGdxGame game) {
        this.game = game;

        background = new Texture("background2.png");
        playerTexture = new Texture("player.png");
        enemyTexture = new Texture("enemy.png");
        goalTexture = new Texture("aim.png");
        starTexture = new Texture("star.png");

        worldWidth = background.getWidth() * 2;
        worldHeight = background.getHeight() * 2;

        playerPos = new Vector2(100, 100);

        float goalW = goalTexture.getWidth() * GOAL_SCALE;
        float goalH = goalTexture.getHeight() * GOAL_SCALE;
        goalBounds = new Rectangle(worldWidth - goalW - 100, 100, goalW, goalH);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        enemies = new Array<>();
        stars = new Array<>();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0, worldWidth, worldHeight);
        game.batch.draw(goalTexture, goalBounds.x, goalBounds.y, goalBounds.width, goalBounds.height);
        game.batch.draw(playerTexture, playerPos.x, playerPos.y,
                playerTexture.getWidth() * PLAYER_SCALE, playerTexture.getHeight() * PLAYER_SCALE);

        for (Enemy e : enemies) {
            game.batch.draw(enemyTexture, e.position.x, e.position.y,
                    enemyTexture.getWidth() * ENEMY_SCALE, enemyTexture.getHeight() * ENEMY_SCALE);
        }

        for (Star s : stars) {
            game.batch.draw(starTexture, s.position.x, s.position.y,
                    starTexture.getWidth() * STAR_SCALE, starTexture.getHeight() * STAR_SCALE);
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
        Vector2 playerCenter = new Vector2(playerPos.x + playerW / 2f, playerPos.y + playerH / 2f);

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            e.update(delta, playerPos);

            float enemyW = enemyTexture.getWidth() * ENEMY_SCALE;
            float enemyH = enemyTexture.getHeight() * ENEMY_SCALE;
            Vector2 enemyCenter = new Vector2(e.position.x + enemyW / 2f, e.position.y + enemyH / 2f);
            float distance = playerCenter.dst(enemyCenter);

            if (distance < playerW / 2 + enemyW / 2 * 0.8f) {
                game.setScreen(new GameOverScreen(game));
                return;
            }
        }

        for (Star s : stars) s.update(delta);

        for (int i = enemies.size - 1; i >= 0; i--) {
            Rectangle enemyBounds = enemies.get(i).getBounds();
            for (int j = stars.size - 1; j >= 0; j--) {
                if (stars.get(j).getBounds().overlaps(enemyBounds)) {
                    enemies.removeIndex(i);
                    stars.removeIndex(j);
                    break;
                }
            }
        }

        for (int i = stars.size - 1; i >= 0; i--) {
            Vector2 pos = stars.get(i).position;
            if (pos.x < 0 || pos.x > worldWidth || pos.y < 0 || pos.y > worldHeight) {
                stars.removeIndex(i);
            }
        }

        Rectangle playerRect = new Rectangle(playerPos.x, playerPos.y, playerW, playerH);
        if (playerRect.overlaps(goalBounds)) {
            game.setScreen(new MiniGameScreen(game));
        }

        float camX = MathUtils.clamp(playerPos.x, camera.viewportWidth / 2f, worldWidth - camera.viewportWidth / 2f);
        float camY = MathUtils.clamp(playerPos.y, camera.viewportHeight / 2f, worldHeight - camera.viewportHeight / 2f);
        camera.position.set(camX, camY, 0);
    }

    private void handleInput(float delta) {
        float moveX = 0, moveY = 0;

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

        float maxX = worldWidth - playerTexture.getWidth() * PLAYER_SCALE;
        float maxY = worldHeight - playerTexture.getHeight() * PLAYER_SCALE;
        playerPos.x = MathUtils.clamp(playerPos.x, 0, maxX);
        playerPos.y = MathUtils.clamp(playerPos.y, 0, maxY);

        if (Gdx.input.justTouched()) {
            Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(click);

            Vector2 start = new Vector2(
                playerPos.x + playerTexture.getWidth() * PLAYER_SCALE / 2f,
                playerPos.y + playerTexture.getHeight() * PLAYER_SCALE / 2f
            );

            Vector2 target = new Vector2(click.x, click.y);
            stars.add(new Star(start, target, starTexture));
        }
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
        starTexture.dispose();
    }
}
