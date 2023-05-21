package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        float x = health.getHealth() / health.getMaxHealth() * 10;

       for(int i = 0; i < 10; i++) {
           GlobalBatch.render(base, 1920 - 77 * (i + 1) - 10, 1080 - 100);
          if(i + 1 <= x) GlobalBatch.render(bar, 1920 - 77 * (i + 1) - 10, 1080 - 100);
           else if(i + 0.5 <= x) {
              GlobalBatch.render(halfbar, 1920 - 77 * (i + 1) - 10 + 38, 1080 - 100);
          }
       }
    }
}
