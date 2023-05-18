package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.Button;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.sound.SoundSettings;


public class Menu extends Screen {
    static Texture Background = new Texture("interface/SWmenuBackground.png");
    static Texture logo = new Texture("interface/SWlogo.png");
    static public Boolean goToGame = false;
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/Click.ogg"));

    static Batch batch = new SpriteBatch();
    static Button playButton = new Button(new Texture("interface/SWplayButton.png"),
            new Texture("interface/SWplayButtonSellected.png"), 135, 90 + 159 * 2) {
    },
            settingsButton = new Button(new Texture("interface/SWsettingsButton.png"),
                    new Texture("interface/SWsettingsButtonSellected.png"), 135, 60 + 159) {
            },
            exitButton = new Button(new Texture("interface/SWexitButton.png"),
            new Texture("interface/SWexitButtonSellected.png"), 135, 30) {
    };


    public Menu() {
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    }



    @Override public void updateScreen() {
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
        if(playButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
            sound.play(SoundSettings.soundVolume);
            goToGame = true;
        }
        if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
            sound.play(SoundSettings.soundVolume);
        }
        if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
            sound.play(SoundSettings.soundVolume);
            Gdx.app.exit();
        }
        }

    }


    @Override public void renderScreen() {
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        batch.draw(Background, 0, 0);
        batch.draw(logo, 10, 500);

        if(playButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            playButton.renderSellected(batch);
        else playButton.render(batch);

        if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            settingsButton.renderSellected(batch);
        else settingsButton.render(batch);

        if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            exitButton.renderSellected(batch);
        else exitButton.render(batch);

        batch.end();
    }
}
