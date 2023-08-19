package com.shrubyway.game.myinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.shrubyway.game.GlobalBatch;

public class TextDrawer {
    static public BitmapFont fontBlack = new BitmapFont(Gdx.files.internal("fonts/black.fnt"));
    static public BitmapFont fontWhite = new BitmapFont(Gdx.files.internal("fonts/white.fnt"));

    static public BitmapFont fontBlue = new BitmapFont(Gdx.files.internal("fonts/blue.fnt"));
    static public BitmapFont fontOrange = new BitmapFont(Gdx.files.internal("fonts/orange.fnt"));

    static public BitmapFont fontGray = new BitmapFont(Gdx.files.internal("fonts/gray.fnt"));

    static public BitmapFont fontLightBlue = new BitmapFont(Gdx.files.internal("fonts/light_blue.fnt"));

    static  {
        fontBlack.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        fontBlack.getData().setScale(0.5f);
        fontWhite.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        fontWhite.getData().setScale(0.5f);
    }
    static public void drawBlack(String text, float x, float y, float scale) {
        fontBlack.getData().setScale(scale * GlobalBatch.scale);
        fontBlack.draw(GlobalBatch.batch, text, x * GlobalBatch.scale, y * GlobalBatch.scale);
        fontBlack.getData().setScale(0.5f);
    }
    static public void drawWhite(String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale * GlobalBatch.scale);
        fontWhite.draw(GlobalBatch.batch, text, x * GlobalBatch.scale, y * GlobalBatch.scale);
        fontWhite.getData().setScale(0.5f);
    }

    static public void drawCenterWhite(String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale * GlobalBatch.scale);
        GlyphLayout layout = new GlyphLayout(fontWhite, text);
        fontWhite.draw(GlobalBatch.batch, text, x * GlobalBatch.scale - layout.width / 2,
                y * GlobalBatch.scale + layout.height / 2);

        fontWhite.getData().setScale(0.5f);
    }

    static public void drawCenterOrange(String text, float x, float y, float scale) {
        fontOrange.getData().setScale(scale * GlobalBatch.scale);
        GlyphLayout layout = new GlyphLayout(fontOrange, text);
        fontOrange.draw(GlobalBatch.batch, text, x * GlobalBatch.scale - layout.width / 2,
                y * GlobalBatch.scale + layout.height / 2);

        fontOrange.getData().setScale(0.5f);
    }

    static public void drawCenterBlue(String text, float x, float y, float scale) {
        fontBlue.getData().setScale(scale * GlobalBatch.scale);
        GlyphLayout layout = new GlyphLayout(fontBlue, text);
        fontBlue.draw(GlobalBatch.batch, text, x * GlobalBatch.scale - layout.width / 2,
                y * GlobalBatch.scale + layout.height / 2);

        fontBlue.getData().setScale(0.5f);
    }

    static public void drawCenterGray(String text, float x, float y, float scale) {
        fontGray.getData().setScale(scale * GlobalBatch.scale);
        GlyphLayout layout = new GlyphLayout(fontGray, text);
        fontGray.draw(GlobalBatch.batch, text, x * GlobalBatch.scale - layout.width / 2,
                y * GlobalBatch.scale + layout.height / 2);

        fontGray.getData().setScale(0.5f);
    }

    static public void drawCenterLightBlue(String text, float x, float y, float scale) {
        fontLightBlue.getData().setScale(scale * GlobalBatch.scale);
        GlyphLayout layout = new GlyphLayout(fontLightBlue, text);
        fontLightBlue.draw(GlobalBatch.batch, text, x * GlobalBatch.scale - layout.width / 2,
                y * GlobalBatch.scale + layout.height / 2);

        fontLightBlue.getData().setScale(0.5f);
    }

    static public void drawCenterBlack(String text, float x, float y, float scale) {
        fontBlack.getData().setScale(scale * GlobalBatch.scale);
        GlyphLayout layout = new GlyphLayout(fontBlack, text);
        fontBlack.draw(GlobalBatch.batch, text, x * GlobalBatch.scale - layout.width / 2,
                y * GlobalBatch.scale + layout.height / 2);
        fontBlack.getData().setScale(0.5f);
    }

    static public void drawWithShadow(String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale * GlobalBatch.scale);
        fontBlack.getData().setScale(scale * GlobalBatch.scale);
        for(float i = -4 * scale; i <= 4 * scale; i+= scale) {
            for(float j = -4 * scale; j <= 4 * scale; j+= scale) {
                fontBlack.draw(GlobalBatch.batch, text, Math.round(x + i) * GlobalBatch.scale, Math.round(y + j) * GlobalBatch.scale);
            }
        }
        fontWhite.draw(GlobalBatch.batch, text, Math.round(x) * GlobalBatch.scale, Math.round(y) * GlobalBatch.scale);
        fontWhite.getData().setScale(0.5f);
        fontBlack.getData().setScale(0.5f);
    }
    static public void drawWithShadow(String text, float x, float y, float scale, float alpha) {
        fontWhite.getData().setScale(scale * GlobalBatch.scale);
        fontWhite.setColor(1, 1, 1, alpha);
        fontBlack.getData().setScale(scale);
        fontBlack.setColor(1, 1, 1, 1f);
        for(float i = -2; i <= 2; i+= scale) {
            for(float j = -2; j <= 2; j+= scale) {
                fontBlack.draw(GlobalBatch.batch, text,
                        Math.round(x + i) * GlobalBatch.scale, Math.round(y + j) * GlobalBatch.scale);
            }
        }
        fontWhite.draw(GlobalBatch.batch, text, Math.round(x) * GlobalBatch.scale, Math.round(y) * GlobalBatch.scale);
        fontWhite.getData().setScale(0.5f);
        fontWhite.setColor(1, 1, 1, 1f);
        fontBlack.getData().setScale(0.5f);
        fontBlack.setColor(1, 1, 1, 1f);
    }

}
