package com.shrubyway.game.myinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.map.MapSettings;

public class MiniMap {
    static public void render(int lvl, float px, float py) {
        px /= MapSettings.MAPSIZE; px *= 274;
        py /= MapSettings.MAPSIZE; py *= 274;

        GlobalBatch.render(GlobalAssetManager.get("interface/Map" + lvl + ".png", Texture.class),
                25, 25);
        GlobalBatch.render(GlobalAssetManager.get("interface/ShrubyIcon.png", Texture.class),
                25 + 29 + px, 25 + 45 + py);
        GlobalBatch.render(GlobalAssetManager.get("interface/miniMapOverlay.png", Texture.class),
                25, 25);
    }
}
