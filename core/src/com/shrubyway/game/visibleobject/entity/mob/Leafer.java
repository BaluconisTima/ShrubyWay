package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.entity.Entity;

public class Leafer extends Mob {
    float bored = 0;
    float funny = 1;
    float checkTime = 0;
    Entity targetEntity = null;
    Vector2 targetPosition = null;

    public void Leafer_idiot_ai(float windDistance, float scareDistance, float delta, Vector2 playerPosition) {
        checkTime -= delta;
        lastThrowTime -= delta;

        if(checkTime < 0) {
            checkTime = 0.6f;
            if(targetEntity == null && targetPosition == null) {
                if(Math.random() < bored) {
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
                    targetPosition = new Vector2(positionLegs().x + (float) (Math.random() * 600 - 300), positionLegs().y + (float) (Math.random() * 600 - 300));
                }
            }

        }
        if(playerPosition.dst2(positionLegs()) < scareDistance && health.getHealth() < health.getMaxHealth() * 0.3 + 1) {
            if(targetEntity != Game.player) {
                targetEntity = null;
                funny = 0;
                bored = 1;
            }
            float alpha = (float) (Math.random() * 0.5 * Math.PI - Math.PI * 0.25f);
            tempDirection.set(playerPosition.x - positionCenter().x, playerPosition.y - positionCenter().y);
            tempDirection.scl(1/tempDirection.len());
            tempDirection.rotateRad(alpha);
            tempDirection.scl(scareDistance);
            target.set(positionCenter().x - tempDirection.x, positionCenter().y - tempDirection.y);
            tryMoveTo(tempDirection);
            return;
        }
        if(targetEntity != null) {
            if(targetEntity.positionLegs().dst2(positionLegs()) > windDistance) {
                tryMoveTo(targetEntity.positionLegs());
                return;
            } else {
                if(canThrow()) {
                    throwItem(playerPosition, new Item(4), true);
                    funny *= 0.9;
                    bored = Math.min(1f, bored / 0.9f);

                    if(Math.random() > funny) {
                        targetEntity = null;
                        checkTime = 0.2f;
                    }
                }
                return;
            }
        }
        if(targetPosition != null) {
            if(targetPosition.dst2(positionLegs()) < 100) {
                funny = Math.min(1f, funny / 0.9f);
                bored *= 0.9f;
                targetPosition = null;
                checkTime = 0.5f;
            } else tryMoveTo(targetPosition);
        }
    }
}
