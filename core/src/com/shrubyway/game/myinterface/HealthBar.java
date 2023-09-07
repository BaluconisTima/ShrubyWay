package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.Health;

public class HealthBar {
    static Texture base, bar;

    static {
        base = GlobalAssetManager.get("interface/heartBase.png", Texture.class);
        bar = GlobalAssetManager.get("interface/heart.png", Texture.class);
    }
    static TextureRegion halfbar = new TextureRegion(bar, bar.getWidth() / 2, 0, bar.getWidth() / 2,
            bar.getHeight());
    static public void render(Health health) {
        float x = health.getHealth() / health.getMaxHealth() * 20;
        Vector2 topRight = GlobalBatch.topRightCorner();

       for(int i = 0; i < 10; i++) {
           GlobalBatch.render(base, topRight.x - 77 * (i + 1) - 10, topRight.y - 100);
          if(x > i * 2) GlobalBatch.render(bar, topRight.x - 77 * (i + 1) - 10, topRight.y - 100);
           else if(x > i * 2 + 1) {
              GlobalBatch.render(halfbar, topRight.x - 77 * (i + 1) - 10 + 38, topRight.y - 100);
          }
       }
    }
}
