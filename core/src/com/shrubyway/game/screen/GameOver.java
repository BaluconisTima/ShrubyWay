package com.shrubyway.game.screen;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.sound.SoundSettings;

public class GameOver extends Screen {
    static Texture Background, sign;
    Sound sound;
    static Button tryAgain, exitButton;

    public GameOver() {
        SoundSettings.changeMusic(null);

        if(Background == null) Background = ShrubyWay.assetManager.get("interface/SWbackgound.png", Texture.class);
        if(sign == null) sign = ShrubyWay.assetManager.get("interface/gameOver.png", Texture.class);
        if(sound == null) sound = ShrubyWay.assetManager.get("sounds/EFFECTS/Click.ogg", Sound.class);
        if(tryAgain == null) tryAgain = new Button(ShrubyWay.assetManager.get("interface/SwTryAgainButton2.png", Texture.class),
                ShrubyWay.assetManager.get("interface/SwTryAgainButton.png", Texture.class), GlobalBatch.bottomLeftCorner().x + 300, 300) {
        };
        if(exitButton == null) exitButton = new Button(ShrubyWay.assetManager.get("interface/SWmainMenu.png.png", Texture.class),
                ShrubyWay.assetManager.get("interface/SWmainMenuSel.png", Texture.class), GlobalBatch.bottomRightCorner().x - 300 -
                ShrubyWay.assetManager.get("interface/SWmainMenu.png.png", Texture.class).getWidth(), 300) {
        };
    }

    private void menu() {
        ShrubyWay.screen = new Menu();
    }

    private void tryAgain() {
        ShrubyWay.screen = new Game();
        ShrubyWay.screen.updateScreen();
    }

    @Override public void updateScreen() {
        if(lastScaleX != GlobalBatch.scaleX || lastScaleY != GlobalBatch.scaleY) {
            lastScaleX = GlobalBatch.scaleX;
            lastScaleY = GlobalBatch.scaleY;
            tryAgain.set(GlobalBatch.bottomLeftCorner().x + 300, 300);
            exitButton.set(GlobalBatch.bottomRightCorner().x - 300 -
                    ShrubyWay.assetManager.get("interface/SWmainMenu.png.png", Texture.class).getWidth(), 300);
        }
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
            if(tryAgain.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                tryAgain();
            }
            if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                sound.play(SoundSettings.soundVolume);
                menu();
            }
        }
    }

    float lastScaleX = 1, lastScaleY = 1;

    @Override public void renderScreen() {
        ScreenUtils.clear(1, 1, 1, 1);
        GlobalBatch.render(Background, 0, 0, GlobalBatch.topRightCorner().x, GlobalBatch.topRightCorner().y);
        GlobalBatch.render(sign, GlobalBatch.centerX() - sign.getWidth() / 2, GlobalBatch.centerY());

        if(tryAgain.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            tryAgain.renderSellected();
        else tryAgain.render();

        if(exitButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()))
            exitButton.renderSellected();
        else exitButton.render();
    }
}
