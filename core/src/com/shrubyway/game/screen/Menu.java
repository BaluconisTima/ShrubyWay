package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.sound.SoundSettings;


public class Menu extends Screen {
    static Texture Background = new Texture("interface/SWbackgound.png");
    static Texture logo = new Texture("interface/SWlogo.png");

    static Texture Shruby = new Texture("interface/Shruby.png");

    static Texture menuLight = new Texture("interface/lightMenu.png");
    static public Boolean goToGame = false;

    static Vector2 topLeftCorner = GlobalBatch.topLeftCorner(),
            bottomRightCorner = GlobalBatch.bottomRightCorner(),
            topRightCorner = GlobalBatch.topRightCorner(),
            bottomLeftCorner = GlobalBatch.bottomLeftCorner();


    static float centerX = GlobalBatch.centerX(),
            centerY = GlobalBatch.centerY();

    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/Click.ogg"));

    static Button playButton = new Button(new Texture("interface/play.png"),
            new Texture("interface/playSel.png"), centerX - 272, 45) {
    },
    settingsButton = new Button(new Texture("interface/settings.png"),
                    new Texture("interface/settingsSel.png"), centerX - 680, 65) {
    },
    resetButton = new Button(new Texture("interface/reset.png"),
            new Texture("interface/resetSel.png"), centerX + 680 - 347, 65) {
    },
    exitButton = new Button(new Texture("interface/exit.png"),
            new Texture("interface/exitSel.png"), topLeftCorner.x + 30, topLeftCorner.y - 150) {
    },
    achivementsButton = new Button(new Texture("interface/ach.png"),
            new Texture("interface/achSel.png"), topRightCorner.x - 170, topRightCorner.y - 145) {
    };


    public Menu() {
     //   SoundSettings.changeMusic("music/Menu.mp3");
    }



    @Override public void updateScreen() {
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
            if (playButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                goToGame = true;
            }
            if (settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
            }
            if (exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                Gdx.app.exit();
            }
            if (achivementsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
            }
            if (resetButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
            }
        }

    }


    @Override public void renderScreen() {
        ScreenUtils.clear(1, 1, 1, 1);
        GlobalBatch.render(Background, 0, 0);
        GlobalBatch.render(logo, centerX - 500, topLeftCorner.y - 187);
        long speed = 700;
        long y = System.currentTimeMillis() % (int)(6.28 * speed);
        GlobalBatch.render(menuLight, centerX - menuLight.getWidth() / 2, -700f + (float)Math.sin((float)y / speed) * 100f);
        GlobalBatch.render(Shruby, 0, 10);

        if(playButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            playButton.renderSellected();
        else playButton.render();

        if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            settingsButton.renderSellected();
        else settingsButton.render();

        if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            exitButton.renderSellected();
        else exitButton.render();

        if(achivementsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            achivementsButton.renderSellected();
        else achivementsButton.render();

        if(resetButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            resetButton.renderSellected();
        else resetButton.render();

    }
}
