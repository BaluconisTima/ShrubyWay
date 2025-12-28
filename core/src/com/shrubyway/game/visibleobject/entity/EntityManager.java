package com.shrubyway.game.visibleobject.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationLoader;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {
    static int entityCount = 10;
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
                ShrubyWay.assetManager.get("sounds/EFFECTS/ShrabyDeath1.wav", Sound.class);
        soundDamage[0] = ShrubyWay.assetManager.get("sounds/EFFECTS/ShrabyDamage.ogg", Sound.class);


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
                ShrubyWay.assetManager.get("sounds/EFFECTS/AgaricDeath.ogg", Sound.class);
        soundDamage[1] =
                ShrubyWay.assetManager.get("sounds/EFFECTS/AgaricDamage.ogg", Sound.class);


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
                ShrubyWay.assetManager.get("sounds/EFFECTS/ConeyDeath.ogg", Sound.class);
        soundDamage[2] =
                ShrubyWay.assetManager.get("sounds/EFFECTS/ConeyDamage.ogg", Sound.class);

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
                ShrubyWay.assetManager.get("sounds/EFFECTS/ExplerryDeath.wav", Sound.class);
        soundDamage[3] = ShrubyWay.assetManager.get("sounds/EFFECTS/ExplerryDamage.ogg", Sound.class);

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
                ShrubyWay.assetManager.get("sounds/EFFECTS/MobDeath.ogg", Sound.class);
        soundDamage[4] = ShrubyWay.assetManager.get("sounds/EFFECTS/BerryBush.wav", Sound.class);

        actions[5] = new String[]{"AFK", "WALK", "ATTACK", "DEATH"};
        looping[5] = new boolean[]{true, true, false, false};
        actionTypes[5] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN")) };
        frameCount[5] = new int[]{24, 33, 14, 24};
        animations[5] = AnimationLoader.load("ENTITIES/YELLOW LEAFER", actions[5], actionTypes[5], frameCount[5]);
        soundDeath[5] =
                ShrubyWay.assetManager.get("sounds/EFFECTS/MobDeath.ogg", Sound.class);
        soundDamage[5] = ShrubyWay.assetManager.get("sounds/EFFECTS/BerryBush.wav", Sound.class);

        actions[6] = new String[]{"AFK", "WALK", "ATTACK", "DEATH"};
        looping[6] = new boolean[]{true, true, false, false};
        actionTypes[6] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN")) };
        frameCount[6] = new int[]{24, 33, 14, 21};
        animations[6] = AnimationLoader.load("ENTITIES/RED LEAFER", actions[6], actionTypes[6], frameCount[6]);
        soundDeath[6] =
                ShrubyWay.assetManager.get("sounds/EFFECTS/MobDeath.ogg", Sound.class);
        soundDamage[6] = ShrubyWay.assetManager.get("sounds/EFFECTS/BerryBush.wav", Sound.class);

        actions[7] = new String[]{"AFK", "WALK", "ATTACK", "DEATH"};
        looping[7] = new boolean[]{true, true, false, false};
        actionTypes[7] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN")) };
        frameCount[7] = new int[]{24, 33, 14, 16};
        animations[7] = AnimationLoader.load("ENTITIES/GREEN LEAFER", actions[7], actionTypes[7], frameCount[7]);
        soundDeath[7] =
                ShrubyWay.assetManager.get("sounds/EFFECTS/MobDeath.ogg", Sound.class);
        soundDamage[7] = ShrubyWay.assetManager.get("sounds/EFFECTS/BerryBush.wav", Sound.class);

        actions[8] = new String[]{"AFK", "WALK", "ATTACK", "DEATH"};
        looping[8] = new boolean[]{true, true, false, false};
        actionTypes[8] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
                new CopyOnWriteArrayList<>(Arrays.asList("DOWN")) };
        frameCount[8] = new int[]{33, 32, 17, 18};
        animations[8] = AnimationLoader.load("ENTITIES/BOULDER", actions[8], actionTypes[8], frameCount[8]);
        soundDeath[8] =
                ShrubyWay.assetManager.get("sounds/EFFECTS/MobDeath.ogg", Sound.class);
        soundDamage[8] = ShrubyWay.assetManager.get("sounds/EFFECTS/Boulder.ogg", Sound.class);

        actions[9] = new String[]{"AFK", "APPEAR", "ATTACK", "DEATH", "RUN"};
        looping[9] = new boolean[]{true, false, false, false, false};
        actionTypes[9] = new CopyOnWriteArrayList[]{
                new CopyOnWriteArrayList<>(Collections.singletonList("DOWN")),
                new CopyOnWriteArrayList<>(Collections.singletonList("DOWN")),
                new CopyOnWriteArrayList<>(Collections.singletonList("DOWN")),
                new CopyOnWriteArrayList<>(Collections.singletonList("DOWN")),
                new CopyOnWriteArrayList<>(Collections.singletonList("DOWN"))};
        frameCount[9] = new int[]{30, 39, 42, 19, 12};
        animations[9] = AnimationLoader.load("ENTITIES/CHANTERELLEY", actions[9], actionTypes[9], frameCount[9]);
        soundDeath[9] =
                ShrubyWay.assetManager.get("sounds/EFFECTS/MobDeath.ogg", Sound.class);
        soundDamage[9] = ShrubyWay.assetManager.get("sounds/EFFECTS/AgaricDamage.ogg", Sound.class);
    }



}
