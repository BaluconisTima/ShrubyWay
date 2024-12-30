package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.entity.EntityManager;

public class RedLeafer extends Leafer {
    @Override public void ai(Vector2 playerPosition, float delta) {
        Leafer_idiot_ai(500, 350, delta, playerPosition);
    }

    public RedLeafer(float x, float y) {
        entityID = 6;
        id = 5;
        health = new Health(10);
        speed = 6.3f;
        allowedMotion = true;
        attackCooldown = 4f;
        power = 1.2f;
        action = 0;
        damage = 0f;
        regionWidth = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
    }
    @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 125,
                position.y + 20,
                120, 170);
        return hitBox;
    }
    @Override public Rectangle attackBox() {
        return null;
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(position.x + 100,
                position.y + 5,
                150, 30);
        return collisionBox;
    }

    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }
}
