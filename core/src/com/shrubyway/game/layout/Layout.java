package com.shrubyway.game.layout;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.sound.SoundSettings;

public class Layout {
    private static Texture background, window;

    protected Sound sound = ShrubyWay.assetManager.get("sounds/EFFECTS/Click.ogg", Sound.class);
    private boolean isClosed = false;

    protected void close() {
        sound.play(SoundSettings.soundVolume);
        isClosed = true;
    }
    public Layout() {
        if(background == null) background = ShrubyWay.assetManager.get("interface/layout_shadow.png", Texture.class);
        if(window == null) window = ShrubyWay.assetManager.get("interface/layout_window.png", Texture.class);
    }
    public void render(Vector2 mousePos) {
        GlobalBatch.render(background, 0, 0);
        GlobalBatch.render(window, GlobalBatch.centerX() - window.getWidth()/2, GlobalBatch.centerY() - window.getHeight()/2);
    }

    public void update(Vector2 mousePos) {

    }

    public boolean isClosed() {
        return isClosed;
    }
}
