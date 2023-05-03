package com.shrubyway.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealthBar {
    static Texture base = new Texture("interface/heartBase.png");
    static Texture bar = new Texture("interface/heart.png");
    static TextureRegion halfbar = new TextureRegion(bar, 38, 0, 38, 70);
    static public void render(Batch batch, Health health) {
        float x = health.getHealth() / health.getMaxHealth() * 10;

       for(int i = 0; i < 10; i++) {
           batch.draw(base, 1920 - 77 * (i + 1) - 10, 1080 - 90);
          if(i + 1 <= x) batch.draw(bar, 1920 - 77 * (i + 1) - 10, 1080 - 90);
           else if(i + 0.5 <= x) {
              batch.draw(halfbar, 1920 - 77 * (i + 1) - 10 + 38, 1080 - 90);
          }
       }
    }
}
