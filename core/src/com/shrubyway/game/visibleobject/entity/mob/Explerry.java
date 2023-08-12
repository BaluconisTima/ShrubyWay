package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.effect.BerryExplosion;
import com.shrubyway.game.visibleobject.entity.EntityManager;

public class Explerry extends Mob{
    @Override public void ai(Vector2 playerPosition) {
        closeAi(100, playerPosition);
    }

    public Explerry(float x, float y) {
        entityID = 3;
        id = 2;
        health = new Health(1);
        speed = 10f;
        allowedMotion = true;
        attackCooldown = 1f;
        action = 0;
        damage = 2f;

        regionWidth = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
    }

    @Override public void attack(Vector2 direction) {
        die();
    }

    @Override public void update() {
        super.update();
        if(action == 3 && EntityManager.animations[entityID].get(action).get(faceDirection)[inLiquid ? 1: 0].
                isAnimationFinished(AnimationGlobalTime.time() - animationTime)) {
            Game.objectsList.add(new BerryExplosion(positionCenter().x, positionCenter().y));
        }
    }

    @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 150,
                position.y + 20,
                70, 70);
        return hitBox;
    }
    @Override public Rectangle attackBox() {
        if(attackBox == null) attackBox = new Rectangle(0,0,0,0);
        return attackBox;
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(position.x + 148,
                position.y + 5,
                70, 10);
        return collisionBox;
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }
}
