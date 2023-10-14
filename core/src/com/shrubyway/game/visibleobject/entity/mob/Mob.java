package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.visibleobject.entity.Entity;

public abstract class Mob extends Entity {
    public int id;
    protected Vector2 target = new Vector2(0,0);
    protected float lastTargetUpdate = 0;
    protected float targetUpdateInterval = 0.5f;


    public void ai(Vector2 playerPosition, float delta) {

    }

    @Override public void update(float delta) {
         super.update(delta);
         if(action == 3) alpha *= Math.pow(0.95f, delta * 60);
    }

    @Override public void die() {
        if(action == 3) return;
        MobsManager.makeDrop(id, positionLegs().x, positionLegs().y);
        super.die();
        action = 3;
    }

    private void tryMoveAi(float delta) {
        if(target.x - positionLegs().x > 70) tempDirection.x = 1;
        else if(target.x - positionLegs().x < -70) tempDirection.x = -1;
        else tempDirection.x = 0;

        if(target.y - positionLegs().y > 70) tempDirection.y = 1;
        else if(target.y - positionLegs().y < -70) tempDirection.y = -1;
        else tempDirection.y = 0;


        tempDirection2.set(tempDirection.x, tempDirection.y);

        if(!tryMoveTo(tempDirection, delta)) {
            if(AnimationGlobalTime.time() - lastTargetUpdate < targetUpdateInterval) {
                tempDirection.nor();
                if(Math.abs(tempDirection.x) < 70) {
                    tempDirection.set(1, 0);
                    target.set(target.x + 150, target.y);
                }

                if (!tryMoveTo(tempDirection, delta)) {

                    if(Math.abs(tempDirection.y) < 70) {
                        tempDirection.set(0, 1);
                        target.set(target.x, target.y + 150);
                    }


                    if (!tryMoveTo(tempDirection, delta)) {
                        tempDirection.set(0, 0);
                        tryMoveTo(tempDirection, delta);
                    }
                }
            }
        }
    }

    protected void closeAi(float distance, Vector2 playerPosition, float delta) {
        if(momentum.len() > 1) return;

        if(lastTargetUpdate < AnimationGlobalTime.time() - targetUpdateInterval) {
            tempDirection.set(playerPosition.x - positionLegs().x, playerPosition.y - positionLegs().y);
            if(tempDirection.len() < 2000) {
                target.set(playerPosition.x, playerPosition.y);
                lastTargetUpdate = AnimationGlobalTime.time();
            } else {
                tempDirection.set(target.x - playerPosition.x, target.y - playerPosition.y);
                if(tempDirection.len() < 100) {
                    target.set(playerPosition.x, playerPosition.y);
                    lastTargetUpdate = AnimationGlobalTime.time();
                } else
                if(Math.random() < 0.02) {
                    target.set(positionLegs().x + (float) (Math.random() * 1000 - 500),
                            positionLegs().y + (float) (Math.random() * 1000 - 500));
                    lastTargetUpdate = AnimationGlobalTime.time();
                }
            }

        }
        tryMoveAi(delta);

        tempDirection.set(playerPosition.x - positionLegs().x,
                playerPosition.y - positionLegs().y);
        if(tempDirection.len() < distance) {
            attack(target);
        }
    }
    protected void longRangeAi(float shootDistance, float scareDistance, Vector2 playerPosition, Item bullet, float delta) {
           if(!allowedMotion) return;
           if(momentum.len() > 1) return;
           tempDirection.set(playerPosition.x - positionLegs().x, playerPosition.y - positionLegs().y);
           if(lastThrowTime + throwCooldown <= AnimationGlobalTime.time() && tempDirection.len() < shootDistance
                   && tempDirection.len() > scareDistance * 0.7 && canThrow()) {
               tempDirection.set(playerPosition.x - positionCenter().x, playerPosition.y - positionCenter().y);
               throwItem(playerPosition, bullet, true);
               tempDirection.nor();
               animationTime = AnimationGlobalTime.time();
               allowedMotion = false;
               action = 2;
               return;
           }

            tempDirection.set(playerPosition.x - target.x, playerPosition.y - target.y);
           tempDirection2.set(playerPosition.x - positionLegs().x, playerPosition.y - positionLegs().y);
            if(tempDirection.len() < scareDistance && tempDirection2.len() < scareDistance) {
                float alpha = (float) (Math.random() * 0.5 * Math.PI - Math.PI * 0.25f);
                tempDirection.set(playerPosition.x - positionCenter().x, playerPosition.y - positionCenter().y);
                tempDirection.scl(1/tempDirection.len());
                tempDirection.rotateRad(alpha);
                tempDirection.scl(scareDistance);
                target.set(positionCenter().x - tempDirection.x, positionCenter().y - tempDirection.y);
            } else {
                if(lastTargetUpdate < AnimationGlobalTime.time() - targetUpdateInterval) {
                    tempDirection.set(playerPosition.x - positionLegs().x, playerPosition.y - positionLegs().y);
                    tempDirection2.set(playerPosition.x - target.x, playerPosition.y - target.y);
                    if (tempDirection.len() > shootDistance) {
                        target.set(playerPosition.x, playerPosition.y);
                        lastTargetUpdate = AnimationGlobalTime.time();
                    }
                }
            }

        tempDirection.set(playerPosition.x - positionLegs().x, playerPosition.y - positionLegs().y);
        if(tempDirection.len() >= shootDistance || tempDirection.len() <= scareDistance) tryMoveAi(delta);
        else {
            tempDirection.set(0, 0);
            tryMoveTo(tempDirection, delta);
        }

    }
}
