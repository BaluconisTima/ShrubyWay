package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.TimeUtils;

import java.lang.Math;


public class Shraby extends Entity {

    static String actions[] = {"AFK", "WALK", "PORTAL", "ATTACK"};
    static String actionTypes[][] = {{"DOWN", "UP", "LEFT", "RIGHT"},
            {"DOWN", "UP", "LEFT", "RIGHT"},
            {"OUT", null, null, null},
            {"DOWN", "UP", "LEFT", "RIGHT"}};
    static int FrameCount[] = {30, 30, 34, 14};


    public Shraby(float x, float y) {
        Speed = 10f;
        canMove = false;
        action = 2;
        if(animations == null) animations =
                animationLoader.Load("ENTITIES/SHRABY", actions, actionTypes, FrameCount);
        position.set(x, y);
        RegionWidth = (animations[0][0][0].getKeyFrame(AnimationGlobalTime.x)).getRegionWidth();
        RegionHeight = animations[0][0][0].getKeyFrame(AnimationGlobalTime.x).getRegionHeight();
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/EFFECTS/PortalOut.ogg"));
        sound.play(SoundSettings.soundVolume);
    }

    @Override public Rectangle collisionBox() {
        collisionBox.change(position.x + 138,
                position.y + 5,
                95, 15);
        return collisionBox;
    }
    TextureRegion temp;
    @Override public void Render(Batch batch) {
        animationTime += Gdx.graphics.getDeltaTime();
        if(!canMove) {
            if(animations[action][FaceDirection][inLiquid ? 1: 0].isAnimationFinished(animationTime)) {
                canMove = true;
                action = 0;
                animationTime = 0;
            }
        }
        animations[action][FaceDirection][inLiquid ? 1 : 0].setFrameDuration(1f/(2.4f * getSpeed()));
        temp =
                   animations[action][FaceDirection][inLiquid ? 1 : 0].getKeyFrame(animationTime, true);
           batch.draw(temp,
                   Math.round(position.x), Math.round(position.y) - (inLiquid ? -5 : 83));
           collisionBox().render(batch);
    }


    @Override public Vector2 position() {
        return position;
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + RegionWidth / 2, position.y + 118);
        return tempPosition;
    }







}