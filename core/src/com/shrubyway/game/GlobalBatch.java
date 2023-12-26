package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GlobalBatch {
    public static SpriteBatch batch;
    static public float scale = 1f;
    static void changeScale(int width, int height) {
       scale = (float) width / 1920;
        scale = Math.min(scale, (float) height / 1080);
    }
    public static void create() {
        batch = new SpriteBatch();
        batch.enableBlending();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    public static void dispose() {
        batch.dispose();
        scale = 1f;
    }
    public static void begin() {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
    }
    public static void end() {
        batch.end();
    }



    public static void setProjectionMatrix(OrthographicCamera camera) {
        camera.position.scl(scale);
        camera.position.set(Math.round(camera.position.x), Math.round(camera.position.y), Math.round(camera.position.z));
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        camera.position.scl(1 / scale);
        camera.position.set(Math.round(camera.position.x), Math.round(camera.position.y), Math.round(camera.position.z));
        camera.update();
    }
    public static void render(Texture texture, float x, float y) {
        batch.draw(texture, Math.round(x * scale), Math.round(y * scale),
                Math.round(texture.getWidth() * scale), Math.round(texture.getHeight() * scale));
    }

    public static void render(TextureRegion texture, float x, float y, float w, float h,
                              float angle) {
        batch.draw(texture, Math.round(x * scale), Math.round(y * scale),
                Math.round(w / 2 * scale), Math.round(h / 2 * scale),
                Math.round(w * scale), Math.round(h * scale),
                1, 1, angle);
    }

    public static void render(Texture texture, float x, float y, float width, float height,
                              float angle) {
        batch.draw(texture, Math.round(x * scale), Math.round(y * scale),
                Math.round(width / 2 * scale), Math.round(height / 2 * scale),
                Math.round(width * scale), Math.round(height * scale),
                1, 1, angle, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    public static void render(TextureRegion texture, float x, float y, float w, float h,
                              float angle, float scl) {
        batch.draw(texture, Math.round(x * scale * scl), Math.round(y * scale * scl),
                Math.round(w / 2 * scale * scl), Math.round(h / 2 * scale * scl),
                Math.round(w * scale * scl), Math.round(h * scale * scl),
                1, 1, angle);
    }
    public static void render(Texture texture, float x, float y, float scl) {
        batch.draw(texture, Math.round(x * scale * scl), Math.round(y * scale * scl),
                Math.round(texture.getWidth() * scale * scl), Math.round(texture.getHeight() * scale * scl));
    }

    public static void render(Texture texture, float x, float y, float width, float height) {
        batch.draw(texture, Math.round(x * scale), Math.round(y * scale),
                Math.round(width * scale), Math.round(height * scale));
    }

    public static void render(TextureRegion texture, float x, float y) {
        batch.draw(texture, Math.round(x * scale), Math.round(y * scale),
                Math.round(texture.getRegionWidth() * scale), Math.round(texture.getRegionHeight() * scale));
    }
    public static void render(TextureRegion texture, float x, float y, float scl) {
        batch.draw(texture, Math.round(x * scale * scl), Math.round(y * scale * scl),
                Math.round(texture.getRegionWidth() * scale * scl), Math.round(texture.getRegionHeight() * scale * scl));
    }

    public static void render(TextureRegion texture, float x, float y, float width, float height) {
        batch.draw(texture, Math.round(x * scale), Math.round(y * scale),
                Math.round(width * scale), Math.round(height * scale));
    }


    static public Vector2 topLeftCorner() {
        return new Vector2(0 , Gdx.graphics.getHeight() / scale);
    }
    static public Vector2 topRightCorner() {
        return new Vector2(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);
    }
    static public Vector2 bottomLeftCorner() {
        return new Vector2(0 , 0);
    }
    static public Vector2 bottomRightCorner() {
        return new Vector2(Gdx.graphics.getWidth() / scale, 0);
    }
    static public Vector2 center() {
        return new Vector2(Gdx.graphics.getWidth() / scale / 2, Gdx.graphics.getHeight() / scale / 2);
    }
    static public float centerX() {
        return Gdx.graphics.getWidth() / scale / 2;
    }
    static public float centerY() {
        return Gdx.graphics.getHeight() / scale / 2;
    }






}
