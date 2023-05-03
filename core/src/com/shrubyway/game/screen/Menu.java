package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class Menu extends Screen {
    static Texture Background = new Texture("SWmenuBackground.png");

    public Menu() {
        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

    }

    static Batch batch = new SpriteBatch();

    @Override public void updateScreen() {

    }


    @Override public void renderScreen() {
        batch.begin();
        batch.draw(Background, 0, 0);
        batch.end();
    }
}
