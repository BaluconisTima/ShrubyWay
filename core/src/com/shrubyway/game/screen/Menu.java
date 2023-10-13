package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.Animator;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.saver.GameSaver;
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
    static Animator animator = new Animator();
    static Animation<TextureRegion> eyes = animator.toAnimation(new Texture("interface/EYES.png"),
            134, 0, 0, 1/24f);


    static int eyesCount = 32;
    static Vector2 eyesPositions[] = new Vector2[eyesCount];
    static float startTime[] = new float[eyesCount];

    static {
        for(int i = 0; i < eyesCount; i++) {
            startTime[i] = (float) (Math.random() * 5.583);
            eyesPositions[i] = new Vector2();
            eyesPositions[i].x = (float) ((bottomRightCorner.x - topLeftCorner.x) / 7 * (i % 8) + topLeftCorner.x + 70 + 100 * ((i / 8) % 2) + (Math.random() * 100 - 50));
            eyesPositions[i].y = (float) ((topLeftCorner.y - bottomRightCorner.y) / 7 * (i / 8) + bottomRightCorner.y + 550 + (Math.random() * 100 - 50));
        }
    }

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
                GameSaver.resetSave();
            }
        }

    }


    @Override public void renderScreen() {
        ScreenUtils.clear(1, 1, 1, 1);
        GlobalBatch.render(Background, 0, 0);

        GlobalBatch.batch.setColor(1, 1, 1, 0.3f);
        long speed2 = 1000;
        long y2 = System.currentTimeMillis() % (int)(5.583 * speed2);
        float time = (float)y2 / speed2;
        for(int i = 0; i < eyesCount; i++) {
            GlobalBatch.render(eyes.getKeyFrame(time + startTime[i]), eyesPositions[i].x, eyesPositions[i].y);
        }
        GlobalBatch.batch.setColor(1, 1, 1, 1);

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
