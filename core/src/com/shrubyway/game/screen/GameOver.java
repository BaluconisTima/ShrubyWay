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

public class GameOver extends Screen {
    static Texture Background = new Texture("interface/gameOverScreen.png");
    static Batch batch;
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/Click.ogg"));

    static Button tryAgain = new Button(new Texture("interface/SwTryAgainButton2.png"),
            new Texture("interface/SwTryAgainButton.png"), 250, 300) {
    },
            exitButton = new Button(new Texture("interface/SWmainMenu.png.png"),
                    new Texture("interface/SWmainMenuSel.png"), 1050, 300) {
            };

    public Boolean tryingAgain = false, exit = false;

    public GameOver() {
        Background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        loadingStatus.set(100);
    }

    @Override public void updateScreen() {
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
            if(tryAgain.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                tryingAgain = true;
            }
            if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                exit = true;
            }
        }
    }

    @Override public void renderScreen() {

        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        batch.draw(Background, 0, 0);

        if(tryAgain.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            tryAgain.renderSellected(batch);
        else tryAgain.render(batch);

        if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            exitButton.renderSellected(batch);
        else exitButton.render(batch);


        batch.end();
    }
}
