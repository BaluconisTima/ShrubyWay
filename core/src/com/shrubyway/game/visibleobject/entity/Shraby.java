package com.shrubyway.game.visibleobject.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;

import java.lang.Math;


public class Shraby extends Entity {

    static String actions[] = {"AFK", "WALK", "PORTAL", "ATTACK"};
    static String actionTypes[][] = {{"DOWN", "UP", "LEFT", "RIGHT"},
            {"DOWN", "UP", "LEFT", "RIGHT"},
            {"OUT", null, null, null},
            {"DOWN", "UP", "LEFT", "RIGHT"}};
    static int frameCount[] = {30, 30, 34, 14};
    public Shraby(float x, float y) {
        speed = 10f;
        canMove = false;
        action = 2;
        shootCooldown = 0;
        if(animations == null) animations =
                animationLoader.load("ENTITIES/SHRABY", actions, actionTypes, frameCount);
        position.set(x, y);
        regionWidth = (animations[0][0][0].getKeyFrame(AnimationGlobalTime.x)).getRegionWidth();
        regionHeight = animations[0][0][0].getKeyFrame(AnimationGlobalTime.x).getRegionHeight();
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
        if(action == 3) {
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
                    attackBox.change(position.x + 30,
                            position.y,
                            100, 210);
                    break;
                case 3:
                    attackBox.change(position.x + 240,
                            position.y,
                            100, 210);
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
    TextureRegion temp;
    @Override public void render(Batch batch) {
        animationTime += Gdx.graphics.getDeltaTime();
        if(!canMove) {
            if(animations[action][faceDirection][inLiquid ? 1: 0].isAnimationFinished(animationTime)) {
                canMove = true;
                action = 0;
                animationTime = 0;
            }
        }
        animations[action][faceDirection][inLiquid ? 1 : 0].setFrameDuration(1f/(2.4f * getSpeed()));
        temp =
                   animations[action][faceDirection][inLiquid ? 1 : 0].getKeyFrame(animationTime, true);
           batch.draw(temp,
                   Math.round(position.x), Math.round(position.y) - (inLiquid ? -5 : 83));

         //  collisionBox().render(batch);
         //  hitBox().render(batch);
        // attackBox().render(batch);
    }


    @Override public Vector2 position() {
        return position;
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }







}