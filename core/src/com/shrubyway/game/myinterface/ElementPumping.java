package com.shrubyway.game.myinterface;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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

import java.text.DecimalFormat;

public class ElementPumping {
     static Texture ElementBase, Elements, FireElement, WaterElement, EarthElement, AirElement;
     static float progress = 0, displayProgress = 0;

     public static float localExp = 0;
     static Diamond fire, water, earth, air;

    static float scale = GlobalBatch.scale;

    static Sound levelUp = ShrubyWay.assetManager.get("sounds/EFFECTS/levelUp.ogg", Sound.class);
    static long lastSound = 0;
    static Vector2 corner;




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

            corner = GlobalBatch.bottomRightCorner();

         float centerX = corner.x + 10 - ElementBase.getWidth() / 2.0f;
            float centerY = corner.y + -10 + ElementBase.getHeight() / 2.0f;
            fire = new Diamond(centerX * scale, (centerY + 100)  * scale, 75 * scale);
            water = new Diamond(centerX * scale, (centerY - 100) * scale, 75 * scale);
            earth = new Diamond((centerX - 100) * scale, centerY * scale, 75 * scale);
            air = new Diamond((centerX + 100) * scale, centerY * scale, 75 * scale);

     }

     public static int getNextLevelCost() {
         return 750;
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
         progress = (localExp  * 1.0f / getNextLevelCost()) * 100;
         progress = Math.min(progress, 100);
         progress = Math.max(progress, 0);
         displayProgress += (progress - displayProgress) / 5;
     }

     public static boolean newLevel() {
         if(localExp >= getNextLevelCost()) {
             localExp -= getNextLevelCost();
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

        static public float getDamageMultiplier(int level) {
            level = Math.max(1, level);
            int damLevel = level;
            float damMod = 0;
            float k = 1;
            while(k <= damLevel) {
                damMod++;
                damLevel -= k;
                k *= 1.61803398875;
            }
            damMod += ((float)damLevel) / k;
            return damMod;
        }

        static public float getAttackCooldownMultiplier(int level) {
            level = Math.max(1, level);
            float x = (float)Math.pow(level, 0.9f) / 2;
            return (float)Math.pow((0.95), x);
        }

        static public float getDefenseMultiplier(int level) {
         level = Math.max(1, level);
         float x = (float)Math.pow(level, 0.8f) / 2;
         return (float)Math.pow((0.914), x);
        }

       static public float getThrowCooldownMultiplier(int level) {
           level = Math.max(1, level);
           float x = (float)Math.pow(level, 0.9f) / 2;
           return (float)Math.pow((0.95), x);
       }

       static public float getThrowSpeedMutiplier(int level) {
          /* level = Math.max(1, level);
           int damLevel = level;
           float damMod = 0;
           float k = 1;
           while(k <= damLevel) {
               damMod++;
               damLevel -= k;
               k *= 1.4;
           }
           damMod += ((float)damLevel) / k;
           return damMod; */
           return 1;
       }

       static public float getThrowDamageAddition(int level) {
           level = Math.max(1, level);
           int damLevel = level;
           float damMod = 0;
           float k = 1;
           while(k <= damLevel) {
               damMod += 0.2f;
               damLevel -= k;
               k *= 1.8;
           }
           damMod += 0.2f * ((float)damLevel) / k;
           return damMod;
       }

       static public float getPassiveHealTimeMultiplier(int level) {
           level = Math.max(1, level);
           float x = (float)Math.pow(level, 0.6f) / 2;
           return (float)Math.pow((0.95), x);
       }

       static public float getEatingSpeedMultiplier(int level) {
           level = Math.max(1, level);
           float x = (float)Math.pow(level, 0.8f) / 2;
           return (float)Math.pow((0.93), x);
       }

       static public float getEatingHealAddition(int level) {
           level = Math.max(1, level);
           int damLevel = level;
           float damMod = 0;
           float k = 1;
           while(k <= damLevel) {
               damMod += 0.2f;
               damLevel -= k;
               k *= 1.8;
           }
           damMod += 0.2f * ((float)damLevel) / k;
           return damMod;
       }


     public void render(Vector2 mouseOnScreenPosition) {

         Game.player.health.defenseLevel = earthLevel;
         Game.player.damageLevel = fireLevel;
         Game.player.throwLevel = airLevel;


         progressUpdate();
        // if(Event.happened("Pumping_Opened"))
         GlobalBatch.render(ElementBase, corner.x + 10 - ElementBase.getWidth(), corner.y-10);

         if(progress == 100) {
            // levelUp.resume();
           //  levelUp.loop();
             GlobalBatch.batch.setColor(1, 1, 1, (float) (Math.sin(AnimationGlobalTime.time() * 7) + 1) / 2);
             if(Event.happened("Pumping_Opened"))
             GlobalBatch.render(ShrubyWay.assetManager.get("interface/blink.png", Texture.class),
                     10 + corner.x - ElementBase.getWidth(), corner.y -10);
             GlobalBatch.batch.setColor(1, 1, 1, 1);
         } else {
            // levelUp.stop();
         }
         if(!Event.happened("Pumping_Opened"))
            GlobalBatch.render(Elements, corner.x + 10 - ElementBase.getWidth(), corner.y + -13);
         if(Event.happened("Fire_opened")) GlobalBatch.render(FireElement, corner.x + 10 - ElementBase.getWidth(), corner.y -10);
         if(Event.happened("Water_opened")) GlobalBatch.render(WaterElement, corner.x + 10 - ElementBase.getWidth(), corner.y -10);
         if(Event.happened("Earth_opened")) GlobalBatch.render(EarthElement, corner.x + 10 - ElementBase.getWidth(), corner.y -10);
         if(Event.happened("Air_opened")) GlobalBatch.render(AirElement, corner.x + 10 - ElementBase.getWidth(), corner.y -10);



         if(localExp >= getNextLevelCost()) {
             if (water.contains(mouseOnScreenPosition) && Event.happened("Water_opened")) {
                 GlobalBatch.render(ShrubyWay.assetManager.get("interface/water.png", Texture.class),
                         corner.x + 10 - ElementBase.getWidth(), corner.y-10);
             }

             if (fire.contains(mouseOnScreenPosition) && Event.happened("Fire_opened")) {
                 GlobalBatch.render(ShrubyWay.assetManager.get("interface/fire.png", Texture.class),
                         corner.x + 10 - ElementBase.getWidth(), corner.y-10);
             }
             if (earth.contains(mouseOnScreenPosition) && Event.happened("Earth_opened")) {
                 GlobalBatch.render(ShrubyWay.assetManager.get("interface/earth.png", Texture.class),
                         corner.x + 10 - ElementBase.getWidth(), corner.y-10);
             }
             if (air.contains(mouseOnScreenPosition) && Event.happened("Air_opened")) {
                 GlobalBatch.render(ShrubyWay.assetManager.get("interface/air.png", Texture.class),
                         corner.x + 10 - ElementBase.getWidth(), corner.y-10);
             }
         }

         float centerX = corner.x + 10 - ElementBase.getWidth() / 2.0f;
         float centerY = corner.y - 10 + ElementBase.getHeight() / 2.0f;


         if (Event.happened("Fire_opened"))
             TextDrawer.drawCenterOrange(String.valueOf(fireLevel), centerX - 3, centerY + 105, 1f);

         if (Event.happened("Air_opened"))
             TextDrawer.drawCenterLightBlue(String.valueOf(airLevel), centerX + 100, centerY + 5, 1f);

         if (Event.happened("Water_opened"))
             TextDrawer.drawCenterBlue(String.valueOf(waterLevel), centerX - 3, centerY - 100, 1f);

         if (Event.happened("Earth_opened"))
             TextDrawer.drawCenterGray(String.valueOf(earthLevel), centerX - 107, centerY + 5, 1f);

         if(Event.happened("Pumping_Opened"))
         GlobalBatch.render(ShrubyWay.assetManager.get("interface/darkness.png", Texture.class),
                 corner.x + 10 - ElementBase.getWidth(), corner.y-10);

         if(Event.happened("Pumping_Opened")) {
             Texture texture = ShrubyWay.assetManager.get("interface/Light.png", Texture.class);


             int x = 160 + (int) ((100 - displayProgress) * 1.1);
             TextureRegion region = new TextureRegion(texture, 0, x,
                     texture.getWidth(), texture.getHeight() - x);
             GlobalBatch.render(region, corner.x + 10 - ElementBase.getWidth(), corner.y-10);
         }


             DecimalFormat decimalFormat = new DecimalFormat("#.##");
             if (Event.happened("Pumping_Opened")) {
                 if (air.contains(mouseOnScreenPosition) && Event.happened("Air_opened")) {
                     String x1 = decimalFormat.format(ElementPumping.getThrowCooldownMultiplier(ElementPumping.airLevel)
                             * Game.player.throwCooldown) + " s.",
                             x2 = decimalFormat.format(ElementPumping.getThrowCooldownMultiplier(ElementPumping.airLevel + 1)
                                     * Game.player.throwCooldown) + " s.",
                             y1 = decimalFormat.format(ElementPumping.getThrowSpeedMutiplier(ElementPumping.airLevel)),
                             y2 = decimalFormat.format(ElementPumping.getThrowSpeedMutiplier(ElementPumping.airLevel + 1)),
                             z1 = decimalFormat.format(ElementPumping.getThrowDamageAddition(ElementPumping.airLevel)),
                             z2 = decimalFormat.format(ElementPumping.getThrowDamageAddition(ElementPumping.airLevel + 1));

                     String text = "Throw Cooldown: " + x1 + " -> " + x2 + " \n" +
                             "Speed multiplier: " + y1 + " -> " + y2 + "\n" +
                             "Bonus damage: " + z1 + " -> " + z2 + "\n";
                     TextDrawer.drawWithShadowColor(text, mouseOnScreenPosition.x * scale - 400, mouseOnScreenPosition.y * scale + 100, 0.4f,
                             new Color((float) 0.9, (float) 0.9, 1, 1));
                 }
                 if (water.contains(mouseOnScreenPosition) && Event.happened("Water_opened")) {
                     String x1 = decimalFormat.format(ElementPumping.getPassiveHealTimeMultiplier(ElementPumping.waterLevel)
                             * Game.RegenCooldown) + " s.",
                             x2 = decimalFormat.format(ElementPumping.getPassiveHealTimeMultiplier(ElementPumping.waterLevel + 1)
                                     * Game.RegenCooldown) + " s.",
                             y1 = decimalFormat.format(ElementPumping.getEatingSpeedMultiplier(ElementPumping.waterLevel)),
                             y2 = decimalFormat.format(ElementPumping.getEatingSpeedMultiplier(ElementPumping.waterLevel + 1)),
                             z1 = decimalFormat.format(ElementPumping.getEatingHealAddition(ElementPumping.waterLevel)),
                             z2 = decimalFormat.format(ElementPumping.getEatingHealAddition(ElementPumping.waterLevel + 1));

                     String text =
                             "Passive Regeneration Time: "
                                     + x1 + " -> " + x2 + " \n" +
                                     "Eating Speed multiplier: " + y1 + " -> " + y2 + "\n" +
                                     "Bonus regeneration: " + z1 + " -> " + z2 + "\n";
                     TextDrawer.drawWithShadowColor(text, mouseOnScreenPosition.x * scale - 500, mouseOnScreenPosition.y * scale + 100, 0.4f,
                             new Color(0, (float) 0.81, 1, 1));
                 }
                 if (earth.contains(mouseOnScreenPosition) && Event.happened("Earth_opened")) {
                     String x1 = decimalFormat.format((1 - ElementPumping.getDefenseMultiplier(ElementPumping.earthLevel)) * 100) + "% ",
                             x2 = decimalFormat.format((1 - ElementPumping.getDefenseMultiplier(ElementPumping.earthLevel + 1)) * 100) + "% ";


                     String text = "Damage mitigation: " + x1 + " -> " + x2 + "\n";
                     TextDrawer.drawWithShadowColor(text, mouseOnScreenPosition.x * scale - 400, mouseOnScreenPosition.y * scale + 50, 0.4f,
                             new Color((float) 0.5, (float) 0.5, (float) 0.5, 1));
                 }
                 if (fire.contains(mouseOnScreenPosition) && Event.happened("Fire_opened")) {
                     String x1 = decimalFormat.format(ElementPumping.getDamageMultiplier(ElementPumping.fireLevel) * Game.player.damage),
                             x2 = decimalFormat.format(ElementPumping.getDamageMultiplier(ElementPumping.fireLevel + 1) * Game.player.damage),
                             y1 = decimalFormat.format(ElementPumping.getAttackCooldownMultiplier(ElementPumping.fireLevel) * Game.player.attackCooldown) + " s.",
                             y2 = decimalFormat.format(ElementPumping.getAttackCooldownMultiplier(ElementPumping.fireLevel + 1) * Game.player.attackCooldown) + " s.";

                     String text = "Melee damage: " + x1 + " -> " + x2 + "\n" +
                             "Attack cooldown: " + y1 + " -> " + y2 + "\n";
                     TextDrawer.drawWithShadowColor(text, mouseOnScreenPosition.x * scale - 400, mouseOnScreenPosition.y * scale + 100, 0.4f,
                             new Color(1, (float) 0.5, (float) 0, 1));
                 }
             }



     }



}
