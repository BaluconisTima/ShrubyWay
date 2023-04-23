package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextDrawer {
    static public BitmapFont fontBlack = new BitmapFont(Gdx.files.internal("fonts/black.fnt"));
    static public BitmapFont fontWhite = new BitmapFont(Gdx.files.internal("fonts/white.fnt"));
    static  {
        fontBlack.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        fontBlack.getData().setScale(0.5f);
        fontWhite.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        fontWhite.getData().setScale(0.5f);
    }
    static public void drawBlack(Batch batch, String text, float x, float y, float scale) {
        fontBlack.getData().setScale(scale);
        fontBlack.draw(batch, text, x, y);
        fontBlack.getData().setScale(0.5f);
    }
    static public void drawWhite(Batch batch, String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale);
        fontWhite.draw(batch, text, x, y);
        fontWhite.getData().setScale(0.5f);
    }

    static public void drawWithShadow(Batch batch, String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale);
        fontBlack.getData().setScale(scale);
        for(float i = -4 * scale; i <= 4 * scale; i+= scale) {
            for(float j = -4 * scale; j <= 4 * scale; j+= scale) {
                fontBlack.draw(batch, text, Math.round(x + i), Math.round(y + j));
            }
        }
        fontWhite.draw(batch, text, Math.round(x), Math.round(y));
        fontWhite.getData().setScale(0.5f);
        fontBlack.getData().setScale(0.5f);
    }
    static public void drawWithShadow(Batch batch, String text, float x, float y, float scale, float alpha) {
        fontWhite.getData().setScale(scale);
        fontWhite.setColor(1, 1, 1, alpha);
        fontBlack.getData().setScale(scale);
        fontBlack.setColor(1, 1, 1, 1f);
        for(float i = -2; i <= 2; i+= scale) {
            for(float j = -2; j <= 2; j+= scale) {
                fontBlack.draw(batch, text, Math.round(x + i), Math.round(y + j));
            }
        }
        fontWhite.draw(batch, text, Math.round(x), Math.round(y));
        fontWhite.getData().setScale(0.5f);
        fontWhite.setColor(1, 1, 1, 1f);
        fontBlack.getData().setScale(0.5f);
        fontBlack.setColor(1, 1, 1, 1f);
    }

}
