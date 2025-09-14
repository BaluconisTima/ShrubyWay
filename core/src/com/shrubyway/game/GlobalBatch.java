package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.animation.AnimationGlobalTime;

public class GlobalBatch {
    public static SpriteBatch batch;
    static private float scale = 1f;
    static public float scaleX, scaleY;
    public static ShaderProgram shader;

    static public int screenWidth = 1920, screenHeight = 1080;
    static void changeScale(int width, int height) {
       scaleX = (float) width / 1920;
       scaleY = (float) height / 1080;
       scale = Math.min(scaleX, scaleY);
       screenWidth = width;
       screenHeight = height;
    }

    static public float getScale() {
        return scale;
    }
    public static void create() {
        batch = new SpriteBatch();
        resetStandardBatchSettings();
    }

    public static void resetStandardBatchSettings() {
        batch.enableBlending();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1f, 1f, 1f, 1f);
    }


    public static void setUpColor(Color color, float strength) {
        batch.setColor((1f + color.r * strength) / (1f + strength),
                (1f + color.g * strength) / (1f + strength),
                (1f + color.b * strength) / (1f + strength),
                1.0f);
    }

    public static void dispose() {
        batch.dispose();
        scale = 1f;
    }

    public static void setShader(ShaderProgram _shader) {
        //shader = _shader;
    }

    public static void unsetShader() {
        shader = null;
        batch.setShader(null);
    }

    public static void begin() {
        ScreenUtils.clear(0, 0, 0, 1);
        if(shader != null && batch.getShader() != shader) {
            batch.setShader(shader);
        }
        if(shader != null)
        shader.setUniformf("u_time", AnimationGlobalTime.time());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                screenWidth, screenHeight));
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

    public static void render(TextureRegion textureRegion, float x, float y) {
        batch.draw(textureRegion, Math.round(x * scale), Math.round(y * scale),
                Math.round(textureRegion.getRegionWidth() * scale), Math.round(textureRegion.getRegionHeight() * scale));
    }
    public static void render(TextureRegion texture, float x, float y, float scl) {
        batch.draw(texture, Math.round(x * scale * scl), Math.round(y * scale * scl),
                Math.round(texture.getRegionWidth() * scale * scl), Math.round(texture.getRegionHeight() * scale * scl));
    }

    public static void render(TextureRegion texture, float x, float y, float width, float height) {
        batch.draw(texture, Math.round(x * scale), Math.round(y * scale),
                Math.round(width * scale), Math.round(height * scale));
    }


    static private Vector2 topLeftCorner = new Vector2(0 , 0),
                           topRightCorner = new Vector2(0, 0),
                            bottomLeftCorner = new Vector2(0 , 0),
                            bottomRightCorner = new Vector2(0, 0),
                            center = new Vector2(0, 0);

    static public Vector2 topLeftCorner() {
        topLeftCorner.set(0 , screenHeight / scale);
        return topLeftCorner;
    }
    static public Vector2 topRightCorner() {
        topRightCorner.set(screenWidth / scale, screenHeight / scale);
        return topRightCorner;
    }
    static public Vector2 bottomLeftCorner() {
        bottomLeftCorner.set(0 , 0);
        return bottomLeftCorner;
    }
    static public Vector2 bottomRightCorner() {
        bottomRightCorner.set(screenWidth / scale, 0);
        return bottomRightCorner;
    }
    static public Vector2 center() {
        center.set(screenHeight / scaleX / 2, screenWidth / scaleY / 2);
        return center;
    }
    static public float centerX() {
        return screenWidth / scale / 2;
    }
    static public float centerY() {
        return screenHeight / scale / 2;
    }






}
