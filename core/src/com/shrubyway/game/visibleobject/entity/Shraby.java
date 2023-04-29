package com.shrubyway.game.visibleobject.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.shrubyway.game.Health;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Shraby extends Entity {

    static String actions[] = {"AFK", "WALK", "PORTAL", "ATTACK", "DEATH"};
    static protected boolean looping[] =
            new boolean[]{true, true, false, false, false};
    static protected ArrayList<ArrayList<Animation<TextureRegion>[]>> animations;

    static protected ArrayList<String>[] actionTypes = new ArrayList[]{
            new ArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
            new ArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
            new ArrayList<>(Arrays.asList("OUT")),
            new ArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
            new ArrayList<>(Arrays.asList("1"))};
    static int frameCount[] = {30, 30, 34, 14, 34};

    public Shraby(float x, float y) {
        health = new Health(20);
        speed = 10f;
        allowedMotion = false;
        action = 2;
        shootCooldown = 0;
        if(animations == null) animations =
                animationLoader.load("ENTITIES/SHRABY", actions, actionTypes, frameCount);
        position.set(x, y);
        regionWidth = (animations.get(0).get(0)[0].getKeyFrame(AnimationGlobalTime.x)).getRegionWidth();
        regionHeight = animations.get(0).get(0)[0].getKeyFrame(AnimationGlobalTime.x).getRegionHeight();

        Sound sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/EFFECTS/PortalOut.ogg"));
        sound.play(SoundSettings.soundVolume);
    }
   @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 150,
                position.y + 40,
                70, 140);
        return hitBox;
   }
    @Override public Rectangle attackBox() {
        if(attackBox == null) attackBox = new Rectangle(0,0,-1,-1);
        if(action == 3 && attacking) {
            switch (faceDirection) {
                case 0:
                    attackBox.change(position.x + 75,
                            position.y - 50,
                            210, 100);
                    break;
                case 1:
                    attackBox.change(position.x + 75,
                            position.y + 120,
                            210, 100);
                    break;
                case 2:
                    attackBox.change(position.x,
                            position.y + 55,
                            130, 70);
                    break;
                case 3:
                    attackBox.change(position.x + 240,
                            position.y + 55,
                            130, 70);
                    break;
            }
        } else {
            attackBox.change(0,
                    0,
                    -1, -1);
        }
        return attackBox;
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(position.x + 138,
                position.y + 5,
                95, 15);
        return collisionBox;
    }
    @Override public void die() {
        if(action == 4) return;
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/EFFECTS/ShrabyDeath1.wav"));
        sound.play(SoundSettings.soundVolume);
        animationTime = 0f;
        allowedMotion = false;
        action = 4;
    }
    TextureRegion temp;
    @Override public void render(Batch batch) {
        attacking = false;
        animationTime += Gdx.graphics.getDeltaTime();
        if(!allowedMotion) {
            if(action != 4 &&
                    animations.get(action).get(faceDirection)[inLiquid ? 1: 0].isAnimationFinished(animationTime)) {
                allowedMotion = true;
                action = 0;
                animationTime = 0;
            }
        }
        faceDirection = (byte)Math.min(faceDirection,
                (animations.get(action).size() - 1));
        animations.get(action).get(faceDirection)[inLiquid ? 1: 0].setFrameDuration(1f/(2.4f * getSpeed()));
        temp = animations.get(action).get(faceDirection)[inLiquid ? 1: 0].getKeyFrame(animationTime, looping[action]);
           batch.draw(temp,
                   Math.round(position.x), Math.round(position.y) - (inLiquid ? -5 : 83));

         //  collisionBox().render(batch);
         //  hitBox().render(batch);
         //attackBox().render(batch);
    }


    @Override public Vector2 position() {
        return position;
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }

    public Vector2 positionItemDrop() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 50);
        return tempPosition;
    }







}