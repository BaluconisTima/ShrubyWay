package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.entity.EntityManager;

public class Chanterelley extends Mob {
    public Chanterelley(float x, float y) {
        entityID = 9;
        id = 8;
        health = new Health(10);
        attackCooldown = 0.3f;
        action = 1;
        damage = 1f;
        faceDirection = 0;
        fixedToGround = true;
        regionWidth = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
        waterOverlayScale = 1.6f;
        animationTime = AnimationGlobalTime.time();
    }

    @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + regionWidth / 2 - 70,
                position.y + 20,
                140, 120);
        return hitBox;
    }


    @Override public Rectangle attackBox() {
        if(attackBox == null) attackBox = new Rectangle(0,0,0,0);
        if(action == 2 && attacking) {
            attackBox.change(position.x + regionWidth / 2 - 70,
                    position.y + 20,
                    140, 120);
        } else {
            attackBox.change(0,
                    0,
                    -1, -1);
        }
        return attackBox;
    }

    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(0,
                position.y + 5,
                120, 30);
        return collisionBox;
    }

    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }

    float lastAttackTime = 0;

    @Override public void ai(Vector2 playerPosition, float delta) {
        if (action == 1 && EntityManager.animations[entityID].get(action).get(faceDirection)[inLiquid ? 1: 0].
                isAnimationFinished(AnimationGlobalTime.time() - animationTime)) { // switch to idle when appeared
            action = 0;
            animationTime = AnimationGlobalTime.time() + 0.5f;
            return;
        }
        if(action == 0) {
            // if close enough to target, attack
            float distanceToPlayer = playerPosition.dst(positionCenter());
            if(distanceToPlayer < 300 && lastAttackTime + 5 <= AnimationGlobalTime.time()) {
                action = 2;
                animationTime = AnimationGlobalTime.time();
                lastAttackTime = AnimationGlobalTime.time() + 0.2f;
                return;
            }
        }
        if(action == 2) {
            if(EntityManager.animations[entityID].get(action).get(faceDirection)[inLiquid ? 1: 0].
                    isAnimationFinished(AnimationGlobalTime.time() - animationTime)) {
                action = 0;
                animationTime = AnimationGlobalTime.time() + 0.5f;
                return;
            }
            if(AnimationGlobalTime.time() - lastAttackTime >= attackCooldown) {
                attacking =  true;
                lastAttackTime = AnimationGlobalTime.time();
            }
            return;
        }
        if(action == 4) { // run away TODO
            return;
        }
    }
}
