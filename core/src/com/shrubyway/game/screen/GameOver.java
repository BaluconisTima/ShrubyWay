package com.shrubyway.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.sound.SoundSettings;

public class GameOver extends Screen {
    static Texture Background = new Texture("interface/gameOverScreen.png");
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/Click.ogg"));

    static Button tryAgain = new Button(new Texture("interface/SwTryAgainButton2.png"),
            new Texture("interface/SwTryAgainButton.png"), 250, 300) {
    },
            exitButton = new Button(new Texture("interface/SWmainMenu.png.png"),
                    new Texture("interface/SWmainMenuSel.png"), 1050, 300) {
            };

    public Boolean tryingAgain = false, exit = false;

    public GameOver() {
     //   SoundSettings.changeMusic("music/Menu.mp3");
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
        GlobalBatch.render(Background, 0, 0);

        if(tryAgain.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            tryAgain.renderSellected();
        else tryAgain.render();

        if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            exitButton.renderSellected();
        else exitButton.render();
    }
}
