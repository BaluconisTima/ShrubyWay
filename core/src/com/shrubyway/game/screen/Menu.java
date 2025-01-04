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
import com.shrubyway.game.layout.Layout;
import com.shrubyway.game.layout.ResetLayout;
import com.shrubyway.game.layout.SettingsLayout;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.sound.SoundSettings;


public class Menu extends Screen {
    static Texture Background, logo, Shruby, menuLight;
    Sound sound;
    static private Layout layout;
    static Button playButton, settingsButton, resetButton, exitButton, achivementsButton;

    static float lastScale = -1;

    static Vector2 topLeftCorner = GlobalBatch.topLeftCorner(),
            bottomRightCorner = GlobalBatch.bottomRightCorner(),
            topRightCorner = GlobalBatch.topRightCorner(),
            bottomLeftCorner = GlobalBatch.bottomLeftCorner();


    static float centerX = GlobalBatch.centerX(),
            centerY = GlobalBatch.centerY();
    static Animation<TextureRegion> eyes;
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

    float localScaleX = -1, localScaleY = -1, localScale = -1;

    void updateCorners() {
        if(localScale != lastScale || (centerX == 0 || centerY == 0) || localScaleX != GlobalBatch.scaleX || localScaleY != GlobalBatch.scaleY) {
            topLeftCorner = GlobalBatch.topLeftCorner();
            bottomRightCorner = GlobalBatch.bottomRightCorner();
            topRightCorner = GlobalBatch.topRightCorner();
            bottomLeftCorner = GlobalBatch.bottomLeftCorner();
            centerX = GlobalBatch.centerX();
            centerY = GlobalBatch.centerY();
            for(int i = 0; i < eyesCount; i++) {
                eyesPositions[i].x = (float) ((bottomRightCorner.x - topLeftCorner.x) / 7 * (i % 8) + topLeftCorner.x + 70 + 100 * ((i / 8) % 2) + (Math.random() * 100 - 50));
                eyesPositions[i].y = (float) ((topLeftCorner.y - bottomRightCorner.y) / 7 * (i / 8) + bottomRightCorner.y + 550 + (Math.random() * 100 - 50));
            }
            lastScale = localScale;
            localScaleX = GlobalBatch.scaleX;
            localScaleY = GlobalBatch.scaleY;

            playButton.set(centerX - 272, 45);
            settingsButton.set(centerX - 680, 65);
            resetButton.set(centerX + 680 - 347, 65);
            exitButton.set(topLeftCorner.x + 30, topLeftCorner.y - 150);
            achivementsButton.set(topRightCorner.x - 170, topRightCorner.y - 145);
        }
    }



    public Menu() {
        SoundSettings.changeMusic("music/Menu.mp3");
        // loading from assetManager
        if(Background == null) Background = ShrubyWay.assetManager.get("interface/SWbackgound.png", Texture.class);
        if(logo == null) logo = ShrubyWay.assetManager.get("interface/SWlogo.png", Texture.class);
        if(Shruby == null) Shruby = ShrubyWay.assetManager.get("interface/Shruby.png", Texture.class);
        if(menuLight == null) menuLight = ShrubyWay.assetManager.get("interface/lightMenu.png", Texture.class);
        if(eyes == null) eyes = Animator.toAnimation(ShrubyWay.assetManager.get("interface/EYES.png", Texture.class),
                134, 0, 0, 1/24f);

        if(playButton == null) playButton = new Button(ShrubyWay.assetManager.get("interface/play.png", Texture.class),
                ShrubyWay.assetManager.get("interface/playSel.png", Texture.class), centerX - 272, 45) {
        };
        if(settingsButton == null) settingsButton = new Button(ShrubyWay.assetManager.get("interface/settings.png", Texture.class),
                ShrubyWay.assetManager.get("interface/settingsSel.png", Texture.class), centerX - 680, 65) {
        };
        if(resetButton == null) resetButton = new Button(ShrubyWay.assetManager.get("interface/reset.png", Texture.class),
                ShrubyWay.assetManager.get("interface/resetSel.png", Texture.class), centerX + 680 - 347, 65) {
        };
        if(exitButton == null) exitButton = new Button(ShrubyWay.assetManager.get("interface/exit.png", Texture.class),
                ShrubyWay.assetManager.get("interface/exitSel.png", Texture.class), topLeftCorner.x + 30, topLeftCorner.y - 150) {
        };

        if(achivementsButton == null) achivementsButton = new Button(ShrubyWay.assetManager.get("interface/ach.png", Texture.class),
                ShrubyWay.assetManager.get("interface/achSel.png", Texture.class), topRightCorner.x - 170, topRightCorner.y - 145) {
        };
        if(sound == null) sound = ShrubyWay.assetManager.get("sounds/EFFECTS/Click.ogg", Sound.class);
    }


    private void game() {
        ShrubyWay.screen = new Game();
        ShrubyWay.screen.updateScreen();
    }


    @Override public void updateScreen() {
        updateCorners();
        if(layout != null) {
            layout.update(ShrubyWay.inputProcessor.mousePosition());
            if(layout.isClosed()) layout = null;
            return;
        }
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
            if (playButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                game();

            }
            if (settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                layout = new SettingsLayout();
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
                layout = new ResetLayout();
            }
        }

    }


    @Override public void renderScreen() {
        ScreenUtils.clear(1, 1, 1, 1);
        GlobalBatch.render(Background, 0, 0, GlobalBatch.topRightCorner().x, GlobalBatch.topRightCorner().y);

        GlobalBatch.batch.setColor(1, 1, 1, 0.4f);
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
        GlobalBatch.render(Shruby, centerX - Shruby.getWidth()/2, 10);

        if(playButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null)
            playButton.renderSellected();
        else playButton.render();

        if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null)
            settingsButton.renderSellected();
        else settingsButton.render();

        if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null)
            exitButton.renderSellected();
        else exitButton.render();

        if(achivementsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null)
            achivementsButton.renderSellected();
        else achivementsButton.render();

        if(resetButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null)
            resetButton.renderSellected();
        else resetButton.render();


        if(layout != null) layout.render(ShrubyWay.inputProcessor.mousePosition());

    }

}
