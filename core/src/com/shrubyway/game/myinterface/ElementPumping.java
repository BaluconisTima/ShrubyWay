package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.GlobalBatch;

public class ElementPumping {
     static Texture ElementBase, Elements;
     static float progress = 0, displayProgress = 0;

     static float localExp = 0;



     private static Integer[] nextLevelCost = new Integer[200];
     static public void init() {
            ElementBase = GlobalAssetManager.get("Interface/ElementBase.png", Texture.class);
            Elements = GlobalAssetManager.get("Interface/Elements.png", Texture.class);
            for (int i = 0; i < 200; i++) {
                nextLevelCost[i] = 200 + i * 50;
            }
            nextLevelCost[199] = (1 << 31) - 1;
     }


     static int fireLevel = 1, waterLevel = 1, earthLevel = 1, airLevel = 1;


     static void progressUpdate() {
         progress = (localExp  * 1.0f / nextLevelCost[fireLevel + waterLevel + earthLevel + airLevel - 4]) * 100;
         progress = Math.min(progress, 100);
         progress = Math.max(progress, 0);
         displayProgress += (progress - displayProgress) / 5;
     }
     static public void addExp(int exp) {
         localExp += exp;
         progressUpdate();
     }

     static public void addFireExp() {
         fireLevel++;
         progressUpdate();
     }

        static public void addWaterExp() {
            waterLevel++;
            progressUpdate();
        }

        static public void addEarthExp() {
            earthLevel++;
            progressUpdate();
        }

        static public void addAirExp() {
            airLevel++;
            progressUpdate();
        }

        static public void delFireExp() {
            fireLevel--;
            progressUpdate();
        }

        static public void delWaterExp() {
            waterLevel--;
            progressUpdate();
        }

        static public void delEarthExp() {
            earthLevel--;
            progressUpdate();
        }

        static public void delAirExp() {
            airLevel--;
            progressUpdate();
        }


     public void render() {
         progressUpdate();
         GlobalBatch.render(ElementBase, 1930 - ElementBase.getWidth(), -10);
         GlobalBatch.render(Elements, 1930 - ElementBase.getWidth(), -10);
         Texture texture =  GlobalAssetManager.get("Interface/Light.png", Texture.class);


         int x = 160 + (int)((100 - displayProgress) * 1.1);
         TextureRegion region = new TextureRegion(texture, 0, x,
                 texture.getWidth(), texture.getHeight() - x);
         GlobalBatch.render(region, 1930 - ElementBase.getWidth(), -10);

         float centerX = 1930 - ElementBase.getWidth() / 2.0f;
         float centerY = -10 + ElementBase.getHeight() / 2.0f;

         TextDrawer.drawCenterOrange(String.valueOf(fireLevel), centerX - 3, centerY + 105, 1f);
         TextDrawer.drawCenterLightBlue(String.valueOf(airLevel), centerX + 100, centerY + 5, 1f);
         TextDrawer.drawCenterBlue(String.valueOf(waterLevel), centerX - 3, centerY - 100, 1f);
         TextDrawer.drawCenterGray(String.valueOf(earthLevel), centerX - 100, centerY + 5, 1f);

     }



}
