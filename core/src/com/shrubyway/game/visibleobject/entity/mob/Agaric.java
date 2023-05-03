package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.shrubyway.game.Health;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.animation.AnimationLoader;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.ObjectsList;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Arrays;

public class Agaric extends Mob{

    static String actions[] = {"AFK", "WALK", "ATTACK", "DEATH"};
    static protected boolean looping[] =
            new boolean[]{true, true, false, false};
    static protected ArrayList<ArrayList<Animation<TextureRegion>[]>> animations;

    static protected ArrayList<String>[] actionTypes = new ArrayList[]{
            new ArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
            new ArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
            new ArrayList<>(Arrays.asList("DOWN", "UP", "LEFT", "RIGHT")),
            new ArrayList<>(Arrays.asList("1"))};

    static int frameCount[] = {30, 30, 14, 22};

    static {
        if(animations == null) animations =
                AnimationLoader.load("ENTITIES/AGARIC", actions, actionTypes, frameCount);
    }


    @Override public void ai(Vector2 playerPosition) {
        if(momentum.len() > 1) return;

       if(lastTargetUpdate < AnimationGlobalTime.x - targetUpdateInterval) {
           tempDirection.set(playerPosition.x - positionLegs().x, playerPosition.y - positionLegs().y);
           if(tempDirection.len() < 2000) {
                target.set(playerPosition.x, playerPosition.y);
                lastTargetUpdate = AnimationGlobalTime.x;
           } else {
               tempDirection.set(target.x - playerPosition.x, target.y - playerPosition.y);
               if(tempDirection.len() < 100) {
                   target.set(playerPosition.x, playerPosition.y);
                   lastTargetUpdate = AnimationGlobalTime.x;
               } else
               if(Math.random() < 0.02) {
                   target.set(positionLegs().x + (float) (Math.random() * 1000 - 500),
                           positionLegs().y + (float) (Math.random() * 1000 - 500));
                   lastTargetUpdate = AnimationGlobalTime.x;
               }
           }

       }

        if(target.x - positionLegs().x > 70) tempDirection.x = 1;
        else if(target.x - positionLegs().x < -70) tempDirection.x = -1;
        else tempDirection.x = 0;

        if(target.y - positionLegs().y > 70) tempDirection.y = 1;
        else if(target.y - positionLegs().y < -70) tempDirection.y = -1;
        else tempDirection.y = 0;


        tempDirection2.set(tempDirection.x, tempDirection.y);

        if(!tryMoveTo(tempDirection)) {
                 if(AnimationGlobalTime.x - lastTargetUpdate < targetUpdateInterval) {
                     tempDirection.nor();
                     if(Math.abs(tempDirection.x) < 70) {
                         tempDirection.set(1, 0);
                         target.set(target.x + 150, target.y);
                     }

                     if (!tryMoveTo(tempDirection)) {

                         if(Math.abs(tempDirection.y) < 70) {
                             tempDirection.set(0, 1);
                             target.set(target.x, target.y + 150);
                         }


                         if (!tryMoveTo(tempDirection)) {
                             tempDirection.set(0, 0);
                             tryMoveTo(tempDirection);
                         }
                     }
                 }
        }

        tempDirection.set(playerPosition.x - positionLegs().x,
                playerPosition.y - positionLegs().y);

        if(tempDirection.len() < 150) {
            attack();
        }
    }

    public Agaric(float x, float y) {
        health = new Health(10);
        speed = 8f;
        allowedMotion = true;
        attackCooldown = 1f;
        action = 0;
        damage = 1f;
        regionWidth = (animations.get(0).get(0)[0].getKeyFrame(AnimationGlobalTime.x)).getRegionWidth();
        regionHeight = animations.get(0).get(0)[0].getKeyFrame(AnimationGlobalTime.x).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
    }
    @Override public void die() {
        if(action == 3) return;
        animationTime = 0f;
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



    @Override public void render(Batch batch) {

        if(health.timeAfterHit() < 0.2f) {
            batch.setColor(1, health.timeAfterHit() * 5f, health.timeAfterHit() * 5f, 1);
        }
        animations.get(action).get(faceDirection)[inLiquid ? 1: 0].setFrameDuration(1f/(24f / 8 * getSpeed()));
        batch.draw(animations.get(action).get(faceDirection)[inLiquid ? 1: 0].getKeyFrame(animationTime, looping[action]),
                Math.round(position.x), Math.round(position.y + 10) - (inLiquid ? -5 : 83));
        collisionBox().render(batch);
        batch.setColor(1, 1, 1, 1);
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

        if(action == 3 && animations.get(action).get(faceDirection)[inLiquid ? 1: 0].isAnimationFinished(animationTime)) {
            ObjectsList.del(this);
        }


        if(!allowedMotion) {
            if(action != 3 &&
                    animations.get(action).get(faceDirection)[inLiquid ? 1: 0].isAnimationFinished(animationTime)) {
                allowedMotion = true;
                action = 0;
                animationTime = 0;
            }
        }
    }






}