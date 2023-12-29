package com.shrubyway.game.myinterface;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Diamond;
import com.shrubyway.game.sound.SoundSettings;

public class ElementPumping {
     static Texture ElementBase, Elements, FireElement, WaterElement, EarthElement, AirElement;
     static float progress = 0, displayProgress = 0;

     public static float localExp = 0;
     static Diamond fire, water, earth, air;

    static float scale = GlobalBatch.scale;

    static Sound levelUp = ShrubyWay.assetManager.get("sounds/EFFECTS/levelUp.ogg", Sound.class);
    static long lastSound = 0;




    private static Integer[] nextLevelCost = new Integer[200];
     static public void init() {
          // lastSound = levelUp.play(SoundSettings.soundVolume);
           //levelUp.loop();
          // levelUp.stop();
            fireLevel = 1;
            waterLevel = 1;
            earthLevel = 1;
            airLevel = 1;
            localExp = 0;
            progress = 0;

            ElementBase = ShrubyWay.assetManager.get("interface/ElementBase.png", Texture.class);
            Elements = ShrubyWay.assetManager.get("interface/Elements.png", Texture.class);
            FireElement = ShrubyWay.assetManager.get("interface/elementFire.png", Texture.class);
            WaterElement = ShrubyWay.assetManager.get("interface/elementWater.png", Texture.class);
            EarthElement = ShrubyWay.assetManager.get("interface/elementEarth.png", Texture.class);
            AirElement = ShrubyWay.assetManager.get("interface/elementWind.png", Texture.class);

            float centerX = 1930 - ElementBase.getWidth() / 2.0f;
            float centerY = -10 + ElementBase.getHeight() / 2.0f;
            fire = new Diamond(centerX * scale, (centerY + 100)  * scale, 80 * scale);
            water = new Diamond(centerX * scale, (centerY - 100) * scale, 80 * scale);
            earth = new Diamond((centerX - 100) * scale, centerY * scale, 80 * scale);
            air = new Diamond((centerX + 100) * scale, centerY * scale, 80 * scale);

            for (int i = 0; i < 200; i++) {
                nextLevelCost[i] = 200 + i * 50;
            }
            nextLevelCost[199] = (1 << 31) - 1;
     }

     public static int getLVL() {
         return (fireLevel + waterLevel + earthLevel + airLevel - 4);
     }


     public boolean leftClick(Vector2 position) {
            if(!Event.happened("Pumping_Opened")) return false;
            if (fire.contains(position)) {
                addFireExp();
                return true;
            }
            if (water.contains(position)) {
                addWaterExp();
                return true;
            }
            if (earth.contains(position)) {
                addEarthExp();
                return true;
            }
            if (air.contains(position)) {
                addAirExp();
                return true;
            }
            return false;
     }


     public static Integer fireLevel = 1, waterLevel = 1, earthLevel = 1, airLevel = 1;


     static void progressUpdate() {
         progress = (localExp  * 1.0f / nextLevelCost[fireLevel + waterLevel + earthLevel + airLevel - 4]) * 100;
         progress = Math.min(progress, 100);
         progress = Math.max(progress, 0);
         displayProgress += (progress - displayProgress) / 5;
     }

     public static boolean newLevel() {
         if(waterLevel + fireLevel + earthLevel + airLevel - 4 == 200) {
             return false;
         }
         if(localExp >= nextLevelCost[fireLevel + waterLevel + earthLevel + airLevel - 4]) {
             localExp -= nextLevelCost[fireLevel + waterLevel + earthLevel + airLevel - 4];
             return true;
         }
         return false;
     }

     static public void setLocalExp(int exp) {
         localExp = exp;
         displayProgress = 0;
         progressUpdate();
     }

     static public void addExp(int exp) {
         localExp += exp;
         progressUpdate();
     }

     static private void addFireExp() {
         if(!newLevel()) return;
         ShrubyWay.assetManager.get("sounds/EFFECTS/fire.ogg", Sound.class).play(SoundSettings.soundVolume);
         fireLevel++;
         progressUpdate();
     }

        static private void addWaterExp() {
            if(!newLevel()) return;
            ShrubyWay.assetManager.get("sounds/EFFECTS/water.ogg", Sound.class).play(SoundSettings.soundVolume);
            waterLevel++;
            progressUpdate();
        }

        static private void addEarthExp() {
            if(!newLevel()) return;
            ShrubyWay.assetManager.get("sounds/EFFECTS/earth.ogg", Sound.class).play(SoundSettings.soundVolume);
            earthLevel++;
            progressUpdate();
        }

        static private void addAirExp() {
            if(!newLevel()) return;
            ShrubyWay.assetManager.get("sounds/EFFECTS/air.ogg", Sound.class).play(SoundSettings.soundVolume);
            airLevel++;
            progressUpdate();
        }


     public void render(Vector2 mouseOnScreenPosition) {

         Game.player.health.defenseLevel = earthLevel;
         Game.player.damageLevel = fireLevel;
         Game.player.throwLevel = airLevel;


         progressUpdate();
        // if(Event.happened("Pumping_Opened"))
         GlobalBatch.render(ElementBase, 1930 - ElementBase.getWidth(), -10);

         if(progress == 100) {
            // levelUp.resume();
           //  levelUp.loop();
             GlobalBatch.batch.setColor(1, 1, 1, (float) (Math.sin(AnimationGlobalTime.time() * 7) + 1) / 2);
             if(Event.happened("Pumping_Opened"))
             GlobalBatch.render(ShrubyWay.assetManager.get("interface/blink.png", Texture.class),
                     1930 - ElementBase.getWidth(), -10);
             GlobalBatch.batch.setColor(1, 1, 1, 1);
         } else {
            // levelUp.stop();
         }
         if(!Event.happened("Pumping_Opened"))
            GlobalBatch.render(Elements, 1930 - ElementBase.getWidth(), -13);
         if(Event.happened("Fire_opened")) GlobalBatch.render(FireElement, 1930 - ElementBase.getWidth(), -10);
         if(Event.happened("Water_opened")) GlobalBatch.render(WaterElement, 1930 - ElementBase.getWidth(), -10);
         if(Event.happened("Earth_opened")) GlobalBatch.render(EarthElement, 1930 - ElementBase.getWidth(), -10);
         if(Event.happened("Air_opened")) GlobalBatch.render(AirElement, 1930 - ElementBase.getWidth(), -10);




         if(water.contains(mouseOnScreenPosition) && Event.happened("Water_opened")) {
             GlobalBatch.render(ShrubyWay.assetManager.get("interface/water.png", Texture.class),
                     1930 - ElementBase.getWidth(), -10);
         }

         if(fire.contains(mouseOnScreenPosition) && Event.happened("Fire_opened")) {
                GlobalBatch.render(ShrubyWay.assetManager.get("interface/fire.png", Texture.class),
                        1930 - ElementBase.getWidth(), -10);
         }
         if(earth.contains(mouseOnScreenPosition) && Event.happened("Earth_opened")) {
                GlobalBatch.render(ShrubyWay.assetManager.get("interface/earth.png", Texture.class),
                        1930 - ElementBase.getWidth(), -10);
         }
         if(air.contains(mouseOnScreenPosition) && Event.happened("Air_opened")) {
                GlobalBatch.render(ShrubyWay.assetManager.get("interface/air.png", Texture.class),
                        1930 - ElementBase.getWidth(), -10);
         }

         if(Event.happened("Pumping_Opened"))
         GlobalBatch.render(ShrubyWay.assetManager.get("interface/darkness.png", Texture.class),
                 1930 - ElementBase.getWidth(), -10);

         if(Event.happened("Pumping_Opened")) {
             Texture texture = ShrubyWay.assetManager.get("interface/Light.png", Texture.class);


             int x = 160 + (int) ((100 - displayProgress) * 1.1);
             TextureRegion region = new TextureRegion(texture, 0, x,
                     texture.getWidth(), texture.getHeight() - x);
             GlobalBatch.render(region, 1930 - ElementBase.getWidth(), -10);
         }

         float centerX = 1930 - ElementBase.getWidth() / 2.0f;
         float centerY = -10 + ElementBase.getHeight() / 2.0f;

         if(Event.happened("Fire_opened"))
             TextDrawer.drawCenterOrange(String.valueOf(fireLevel), centerX - 3, centerY + 105, 1f);

         if(Event.happened("Air_opened"))
             TextDrawer.drawCenterLightBlue(String.valueOf(airLevel), centerX + 100, centerY + 5, 1f);

         if(Event.happened("Water_opened"))
             TextDrawer.drawCenterBlue(String.valueOf(waterLevel), centerX - 3, centerY - 100, 1f);

         if(Event.happened("Earth_opened"))
             TextDrawer.drawCenterGray(String.valueOf(earthLevel), centerX - 107, centerY + 5, 1f);

     }



}
