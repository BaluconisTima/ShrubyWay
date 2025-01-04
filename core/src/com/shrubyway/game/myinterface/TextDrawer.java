package com.shrubyway.game.myinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.shrubyway.game.GlobalBatch;
import com.badlogic.gdx.graphics.Color;
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
        fontBlack.getData().setScale(scale * GlobalBatch.getScale());
        fontBlack.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale(), y * GlobalBatch.getScale());
        fontBlack.getData().setScale(0.5f);
    }
    static public void drawWhite(String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale * GlobalBatch.getScale());
        fontWhite.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale(), y * GlobalBatch.getScale());
        fontWhite.getData().setScale(0.5f);
    }

    static public void drawCenterWhite(String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale * GlobalBatch.getScale());
        GlyphLayout layout = new GlyphLayout(fontWhite, text);
        fontWhite.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale() - layout.width / 2,
                y * GlobalBatch.getScale() + layout.height / 2);

        fontWhite.getData().setScale(0.5f);
    }



    static public void drawCenterWhite(String text, float x, float y, float scale, float alpha) {
        fontWhite.getData().setScale(scale * GlobalBatch.getScale());
        GlyphLayout layout = new GlyphLayout(fontWhite, text);
        fontWhite.setColor(1, 1, 1, alpha);
        fontWhite.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale() - layout.width / 2,
                y * GlobalBatch.getScale() + layout.height / 2);
        fontWhite.setColor(1, 1, 1, 1);
        fontWhite.getData().setScale(0.5f);
    }

    static public void drawCenterOrange(String text, float x, float y, float scale) {
        fontOrange.getData().setScale(scale * GlobalBatch.getScale());
        GlyphLayout layout = new GlyphLayout(fontOrange, text);
        fontOrange.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale() - layout.width / 2,
                y * GlobalBatch.getScale() + layout.height / 2);

        fontOrange.getData().setScale(0.5f);
    }

    static public void drawCenterBlue(String text, float x, float y, float scale) {
        fontBlue.getData().setScale(scale * GlobalBatch.getScale());
        GlyphLayout layout = new GlyphLayout(fontBlue, text);
        fontBlue.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale() - layout.width / 2,
                y * GlobalBatch.getScale() + layout.height / 2);

        fontBlue.getData().setScale(0.5f);
    }

    static public void drawCenterGray(String text, float x, float y, float scale) {
        fontGray.getData().setScale(scale * GlobalBatch.getScale());
        GlyphLayout layout = new GlyphLayout(fontGray, text);
        fontGray.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale() - layout.width / 2,
                y * GlobalBatch.getScale() + layout.height / 2);

        fontGray.getData().setScale(0.5f);
    }

    static public void drawCenterLightBlue(String text, float x, float y, float scale) {
        fontLightBlue.getData().setScale(scale * GlobalBatch.getScale());
        GlyphLayout layout = new GlyphLayout(fontLightBlue, text);
        fontLightBlue.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale() - layout.width / 2,
                y * GlobalBatch.getScale() + layout.height / 2);

        fontLightBlue.getData().setScale(0.5f);
    }

    static public void drawCenterBlack(String text, float x, float y, float scale) {
        fontBlack.getData().setScale(scale * GlobalBatch.getScale());
        GlyphLayout layout = new GlyphLayout(fontBlack, text);
        fontBlack.draw(GlobalBatch.batch, text, x * GlobalBatch.getScale() - layout.width / 2,
                y * GlobalBatch.getScale() + layout.height / 2);
        fontBlack.getData().setScale(0.5f);
    }

    static public void drawWithShadow(String text, float x, float y, float scale) {
        fontWhite.getData().setScale(scale * GlobalBatch.getScale());
        fontBlack.getData().setScale(scale * GlobalBatch.getScale());
        for(float i = -4 * scale; i <= 4 * scale; i+= scale) {
            for(float j = -4 * scale; j <= 4 * scale; j+= scale) {
                fontBlack.draw(GlobalBatch.batch, text, Math.round(x + i) * GlobalBatch.getScale(), Math.round(y + j) * GlobalBatch.getScale());
            }
        }
        fontWhite.draw(GlobalBatch.batch, text, Math.round(x) * GlobalBatch.getScale(), Math.round(y) * GlobalBatch.getScale());
        fontWhite.getData().setScale(0.5f);
        fontBlack.getData().setScale(0.5f);
    }

    static public void drawWithShadowColor(String text, float x, float y, float scale, Color c) {
        fontWhite.getData().setScale(scale * GlobalBatch.getScale());
        fontBlack.getData().setScale(scale * GlobalBatch.getScale());
        for(float i = -4 * scale; i <= 4 * scale; i+= scale) {
            for(float j = -4 * scale; j <= 4 * scale; j+= scale) {
                fontBlack.draw(GlobalBatch.batch, text, Math.round(x + i) * GlobalBatch.getScale(), Math.round(y + j) * GlobalBatch.getScale());
            }
        }
        fontWhite.setColor(c);
        fontWhite.draw(GlobalBatch.batch, text, Math.round(x) * GlobalBatch.getScale(), Math.round(y) * GlobalBatch.getScale());
        fontWhite.setColor(1, 1, 1, 1);
        fontWhite.getData().setScale(0.5f);
        fontBlack.getData().setScale(0.5f);
    }

    static public void drawColor(String text, float x, float y, float scale, Color c) {
        fontWhite.getData().setScale(scale * GlobalBatch.getScale());
        fontWhite.setColor(c);
        fontWhite.draw(GlobalBatch.batch, text, Math.round(x) * GlobalBatch.getScale(), Math.round(y) * GlobalBatch.getScale());
        fontWhite.setColor(1, 1, 1, 1);
        fontWhite.getData().setScale(0.5f);
        fontBlack.getData().setScale(0.5f);
    }

    static public void drawWithShadow(String text, float x, float y, float scale, float alpha) {
        fontWhite.getData().setScale(scale * GlobalBatch.getScale());
        fontWhite.setColor(1, 1, 1, alpha);
        fontBlack.getData().setScale(scale * GlobalBatch.getScale());
        fontBlack.setColor(1, 1, 1, 1f);
        for(float i = -2; i <= 2; i+= scale) {
            for(float j = -2; j <= 2; j+= scale) {
                fontBlack.draw(GlobalBatch.batch, text,
                        Math.round(x + i) * GlobalBatch.getScale(), Math.round(y + j) * GlobalBatch.getScale());
            }
        }
        fontWhite.draw(GlobalBatch.batch, text, Math.round(x) * GlobalBatch.getScale(), Math.round(y) * GlobalBatch.getScale());
        fontWhite.getData().setScale(0.5f);
        fontWhite.setColor(1, 1, 1, 1f);
        fontBlack.getData().setScale(0.5f);
        fontBlack.setColor(1, 1, 1, 1f);
    }

}
