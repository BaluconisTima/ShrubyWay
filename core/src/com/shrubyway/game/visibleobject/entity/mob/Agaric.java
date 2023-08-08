package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.Health;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.animation.AnimationLoader;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.ObjectsList;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class Agaric extends Mob{

    int id = 1;
    static Sound soundDeath, soundDamage;

    static {
        soundDeath =
                GlobalAssetManager.get("sounds/EFFECTS/AgaricDeath.ogg", Sound.class);
        soundDamage =
                GlobalAssetManager.get("sounds/EFFECTS/AgaricDamage.ogg", Sound.class);
    }


    @Override public void ai(Vector2 playerPosition) {
        closeAi(100, playerPosition);
    }

    @Override public void getDamage(float damage) {
        super.getDamage(damage);
        soundDamage.play(SoundSettings.soundVolume);
    }

    public Agaric(float x, float y) {
        id = 0;
        health = new Health(10);
        speed = 8f;
        allowedMotion = true;
        attackCooldown = 1f;
        action = 0;
        damage = 2f;
        regionWidth = animations.get(0).get(0)[0].getKeyFrame(0).getRegionWidth();
        regionHeight = animations.get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
    }
    @Override public void die() {
        if(action == 3) return;
        soundDeath.play(SoundSettings.soundVolume);
        MobsManager.makeDrop(id, positionLegs().x, positionLegs().y);
        animationTime = AnimationGlobalTime.time();
        allowedMotion = false;
        action = 3;
    }

    @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 150,
                position.y + 20,
                70, 140);
        return hitBox;
    }
    @Override public Rectangle attackBox() {
        if(attackBox == null) attackBox = new Rectangle(0,0,0,0);
        if(action == 2 && attacking) {
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



    @Override public void render() {
        if(health.timeAfterHit() < 0.2f) {
            GlobalBatch.batch.setColor(1, health.timeAfterHit() * 5f, health.timeAfterHit() * 5f, 1);
        }
        animations.get(action).get(faceDirection)[inLiquid ? 1: 0].setFrameDuration(1f/(24f / speed * getSpeed()));
        GlobalBatch.render(animations.get(action).get(faceDirection)[inLiquid ? 1: 0].
                        getKeyFrame(AnimationGlobalTime.time() - animationTime, looping[action]),
                Math.round(position.x), Math.round(position.y) - (inLiquid ? -5 : 83));
        collisionBox().render();
        GlobalBatch.batch.setColor(1, 1, 1, 1);
        super.render();
    }


    @Override public Vector2 position() {
        return position;
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }

    @Override public void update() {
        super.update();
        faceDirection = (byte)Math.min(faceDirection,
                (animations.get(action).size() - 1));

        if(action == 3 && animations.get(action).get(faceDirection)[inLiquid ? 1: 0].
                isAnimationFinished(AnimationGlobalTime.time() - animationTime)) {
            ObjectsList.del(this);


        }


        if(!allowedMotion) {
            if(action != 3 &&
                    animations.get(action).get(faceDirection)[inLiquid ? 1: 0].isAnimationFinished(
                            AnimationGlobalTime.time() - animationTime)) {
                allowedMotion = true;
                action = 0;
                animationTime = AnimationGlobalTime.time();
            }
        }
    }






}
