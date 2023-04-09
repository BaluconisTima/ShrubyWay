package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.TimeUtils;

import java.lang.Math;


public class Shraby extends Entity {

    static protected  Animation<TextureRegion> animations[][][];
    public Shraby(float x, float y) {
        if(animations == null) animations = animationLoader.Load("ENTITIES/SHRABY",30);
        position.set(x, y);
        RegionWidth = (animations[0][0][0].getKeyFrame(AnimationGlobalTime.x)).getRegionWidth();
        RegionHeight = animations[0][0][0].getKeyFrame(AnimationGlobalTime.x).getRegionHeight();
    }

    @Override public Rectangle collisionBox() {
        collisionBox.change(position.x + 55,
                position.y + 5,
                RegionWidth - 115, 15);
        return collisionBox;
    }
    @Override public void Render(Batch batch) {
        animations[FaceDirection][IsMoving ? 1 : 0][inLiquid ? 1 : 0].setFrameDuration(1f/(2.4f * getSpeed()));
           TextureRegion temp =
                   animations[FaceDirection][IsMoving ? 1 : 0][inLiquid ? 1 : 0].getKeyFrame(AnimationGlobalTime.x, true);
           batch.draw(temp,
                   Math.round(position.x), Math.round(position.y));
           collisionBox().render(batch);
    }
    private float cooldown = 0.5f;
    private float lastUsedTime;
    public Bullet shoot(Vector2 mousePosition) {
        if((TimeUtils.nanoTime() - lastUsedTime) / 1000000000.0f > cooldown) {
            lastUsedTime = TimeUtils.nanoTime();
            Bullet bullet = new Bullet(positionCenter(), mousePosition);
            ChangeAnimationsFor(bullet.Direction);
            return bullet;
        }
        return null;
    }



    @Override public Vector2 position() {
        return position;
    }







}