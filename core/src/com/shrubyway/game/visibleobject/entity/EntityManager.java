package com.shrubyway.game.visibleobject.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.AnimationLoader;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {
    static int entityCount = 5;
    static String actions[][] = new String[entityCount][];
    static boolean looping[][] = new boolean[entityCount][];
    static CopyOnWriteArrayList<String>[] actionTypes[] = new CopyOnWriteArrayList[entityCount][];
    static int frameCount[][] = new int[entityCount][];
    static public CopyOnWriteArrayList<CopyOnWriteArrayList<Animation<TextureRegion>[]>> animations[]
            = new CopyOnWriteArrayList[entityCount];

    static public Sound[] soundDeath = new Sound[entityCount];
    static public Sound[] soundDamage = new Sound[entityCount];

    public static void init() {
        actions[0] = new String[]{"AFK", "WALK", "ATTACK", "DEATH", "PORTAL", "HARMONICA", "EAT"};
        looping[0] = new boolean[]{true, true, false, false, false, true, true};
        actionTypes[0] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")),
                new CopyOnWriteArrayList<>(Arrays.asList("OUT")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")) };
        frameCount[0] = new int[]{30, 30, 14, 34, 34, 26, 16};
        animations[0] = AnimationLoader.load("ENTITIES/SHRUBY", actions[0], actionTypes[0], frameCount[0]);
        soundDeath[0] =
                GlobalAssetManager.get("sounds/EFFECTS/ShrabyDeath1.wav", Sound.class);
        soundDamage[0] = GlobalAssetManager.get("sounds/EFFECTS/ShrabyDamage.ogg", Sound.class);


        actions[1] = new String[]{"AFK", "WALK", "ATTACK", "DEATH"};
        looping[1] = new boolean[]{true, true, false, false};
        actionTypes[1] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")) };
        frameCount[1] = new int[]{30, 30, 14, 22};
        animations[1] = AnimationLoader.load("ENTITIES/AGARIC", actions[1], actionTypes[1], frameCount[1]);
        soundDeath[1] =
                GlobalAssetManager.get("sounds/EFFECTS/AgaricDeath.ogg", Sound.class);
        soundDamage[1] =
                GlobalAssetManager.get("sounds/EFFECTS/AgaricDamage.ogg", Sound.class);


        actions[2] = new String[]{"AFK", "WALK", "ATTACK", "DEATH"};
        looping[2] = new boolean[]{true, true, false, false};
        actionTypes[2] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")) };
        frameCount[2] = new int[]{25, 26, 11, 24};
        animations[2] = AnimationLoader.load("ENTITIES/CONEY", actions[2], actionTypes[2], frameCount[2]);
        soundDeath[2] =
                GlobalAssetManager.get("sounds/EFFECTS/ConeyDeath.ogg", Sound.class);
        soundDamage[2] =
                GlobalAssetManager.get("sounds/EFFECTS/ConeyDamage.ogg", Sound.class);

        actions[3] = new String[]{"AFK", "WALK", "DEATH", "DEATH"};
        looping[3] = new boolean[]{true, true, false, false};
        actionTypes[3] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")) };
        frameCount[3] = new int[]{30, 25, 23, 23};
        animations[3] = AnimationLoader.load("ENTITIES/EXPLERRY", actions[3], actionTypes[3], frameCount[3]);
        soundDeath[3] =
                GlobalAssetManager.get("sounds/EFFECTS/ExplerryDeath.wav", Sound.class);
        soundDamage[3] = GlobalAssetManager.get("sounds/EFFECTS/ExplerryDamage.ogg", Sound.class);

        actions[4] = new String[]{"AFK", "WALK", "ATTACK", "DEATH"};
        looping[4] = new boolean[]{true, true, false, false};
        actionTypes[4] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("UP")),
                new CopyOnWriteArrayList<>(Arrays.asList("1")) };
        frameCount[4] = new int[]{30, 30, 12, 22};
        animations[4] = AnimationLoader.load("ENTITIES/EXPLERRY BUSHER", actions[4], actionTypes[4], frameCount[4]);
        soundDeath[4] =
                GlobalAssetManager.get("sounds/EFFECTS/MobDeath.ogg", Sound.class);
        soundDamage[4] = GlobalAssetManager.get("sounds/EFFECTS/BerryBush.wav", Sound.class);
    }


}
