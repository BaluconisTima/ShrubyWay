package com.shrubyway.game.layout;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.saver.GameSaver;
import com.shrubyway.game.sound.SoundSettings;

public class ResetLayout extends Layout{

    static private Texture resetText;
    static Button resetButton, cancelButton;
    static Sound sound = ShrubyWay.assetManager.get("sounds/EFFECTS/save.ogg", Sound.class);
    public ResetLayout() {
        super();
        if(sound == null) sound = ShrubyWay.assetManager.get("sounds/EFFECTS/save.ogg", Sound.class);
        if (resetText == null) resetText = new Texture("interface/reset_text.png");
        if (resetButton == null)
            resetButton = new Button(ShrubyWay.assetManager.get("interface/reset1.png", Texture.class),
                    ShrubyWay.assetManager.get("interface/reset2.png", Texture.class),
                    GlobalBatch.centerX() - 650, GlobalBatch.centerY() - 300);
        if (cancelButton == null)
            cancelButton = new Button(ShrubyWay.assetManager.get("interface/cancel.png", Texture.class),
                    ShrubyWay.assetManager.get("interface/cancel2.png", Texture.class),
                    GlobalBatch.centerX() + 650 -
                            ShrubyWay.assetManager.get("interface/cancel.png", Texture.class).getWidth(),
                     GlobalBatch.centerY() - 300);
    }

    private void reset() {
        sound.play(SoundSettings.soundVolume);
        GameSaver.resetSave();
        close();
    }

    float localScaleX = -1, localScaleY = -1;

    @Override public void update(Vector2 mousePos) {
        if(localScaleX != GlobalBatch.scaleX || localScaleY != GlobalBatch.scaleY) {
            resetButton.set(GlobalBatch.centerX() - 650, GlobalBatch.centerY() - 300);
            cancelButton.set(GlobalBatch.centerX() + 650 -
                            ShrubyWay.assetManager.get("interface/cancel.png", Texture.class).getWidth(),
                    GlobalBatch.centerY() - 300);
            localScaleX = GlobalBatch.scaleX;
            localScaleY = GlobalBatch.scaleY;
        }
        super.update(mousePos);
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
            if(resetButton.rectangle.checkPoint(mousePos)) reset();
            if(cancelButton.rectangle.checkPoint(mousePos)) close();
        }
    }

    @Override public void render(Vector2 mousePos) {
        super.render(mousePos);
        GlobalBatch.render(resetText, GlobalBatch.centerX() - resetText.getWidth()/2,
                GlobalBatch.centerY() - resetText.getHeight()/2 + 200);

        if(resetButton.rectangle.checkPoint(mousePos)) resetButton.renderSellected();
        else resetButton.render();

        if(cancelButton.rectangle.checkPoint(mousePos)) cancelButton.renderSellected();
        else cancelButton.render();

    }
}
