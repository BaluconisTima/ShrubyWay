package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Wind;
import com.shrubyway.game.visibleobject.entity.Entity;

public class Leafer extends Mob {
    float mood = 1;
    float checkTime = 0;
    Entity targetEntity = null;
    Vector2 targetPosition = null;
    Vector2 tempLen = new Vector2();

    @Override
    public void attack(Vector2 direction) {
        if(AnimationGlobalTime.time() - lastAttackTime < attackCooldown) return;
        lastAttackTime = AnimationGlobalTime.time();
        Vector2 directionTemp =
                new Vector2(direction.x - positionCenter().x, direction.y - positionCenter().y);
        changeAnimationsFor(directionTemp, 2);
        animationTime = AnimationGlobalTime.time();
        allowedMotion = false;
        action = 2;
        Game.objectsList.add(new Wind(directionTemp, positionCenter(), this, 1f, 100));
    }

    public void Leafer_idiot_ai(float windDistance, float scareDistance, float delta, Vector2 playerPosition) {
        checkTime -= delta;
        lastThrowTime -= delta;

        if(checkTime < 0) {
            checkTime = 0.6f;
            if(targetEntity == null && targetPosition == null) {
                if(Math.random() > mood) {
                    if(Math.random() < 0.7) {
                        targetEntity = Game.player;
                    } else {
                        int count = 0;
                        for(VisibleObject object: Game.objectsList.getList()) {
                            if(object instanceof Mob) {
                                count++;
                            }
                        }

                        for(VisibleObject object: Game.objectsList.getList()) {
                            if(object instanceof Mob) {
                                if(Math.random() < 1f / count) {
                                    targetEntity = (Entity) object;
                                    break;
                                }
                                count--;
                            }
                        }
                    }
                } else {
                   // walk around
                    targetPosition = new Vector2(positionLegs().x + (float) (Math.random() * 800 - 400),
                            positionLegs().y + (float) (Math.random() * 800 - 400));
                }
            }

        }
        tempLen.set(playerPosition.x - positionLegs().x, playerPosition.y - positionLegs().y);
        if(tempLen.len() < scareDistance && health.getHealth() < health.getMaxHealth() * 0.3 + 1) {
            if(targetEntity != Game.player) {
                targetEntity = null;
            }
            float alpha = (float) (Math.random() * 0.5 * Math.PI - Math.PI * 0.25f);
            tempDirection.set(playerPosition.x - positionCenter().x, playerPosition.y - positionCenter().y);
            tempDirection.scl(1/tempDirection.len());
            tempDirection.rotateRad(alpha);
            tempDirection.scl(scareDistance);
            target.set(positionCenter().x - tempDirection.x, positionCenter().y - tempDirection.y);
            tryMoveAi(delta);
            return;
        }
        if(targetEntity != null) {
            if(targetEntity.dead()) {
                targetEntity = null;
                checkTime = 0.2f;
                target.set(positionLegs());
                tryMoveAi(delta);
                return;
            }
            tempLen.set(targetEntity.positionLegs().x - positionLegs().x, targetEntity.positionLegs().y - positionLegs().y);
            if(tempLen.len() > windDistance) {
                target.set(targetEntity.positionLegs());
                tryMoveAi(delta);
                return;
            } else {
                    attack(targetEntity.positionLegs());
                    mood *= 0.9;
                    mood = Math.max(0.01f, mood);
                    if(Math.random() > mood) {
                        targetEntity = null;
                        checkTime = 0.2f;
                }
                target.set(positionLegs());
                tryMoveAi(delta);
                return;
            }
        }
        if(targetPosition != null) {
            mood /= 0.98;
            mood = Math.min(0.99f, mood);
            if(Math.random() < mood) {
                targetPosition = null;
                checkTime = 0.2f;
                return;
            }
            tempLen.set(targetPosition.x - positionLegs().x, targetPosition.y - positionLegs().y);
            if(tempLen.len() < 150) {
                mood /= 0.9;
                mood = Math.min(0.99f, mood);
                targetPosition = null;
                checkTime = 0.5f;
            } else {
                target.set(targetPosition);
                tryMoveAi(delta);
            }
            return;
        }
        target.set(positionLegs());
        tryMoveAi(delta);
    }
}
