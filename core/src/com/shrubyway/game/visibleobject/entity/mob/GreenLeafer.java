package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.entity.EntityManager;

public class GreenLeafer extends Leafer {
    @Override public void ai(Vector2 playerPosition, float delta) {
        Leafer_idiot_ai(450, 250, delta, playerPosition);
    }

    public GreenLeafer(float x, float y) {
        entityID = 7;
        id = 6;
        health = new Health(6);
        speed = 6.3f;
        allowedMotion = true;
        attackCooldown = 2.4f;
        power = 0.8f;
        action = 0;
        damage = 0f;
        regionWidth = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
    }
    @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 135,
                position.y + 40,
                90, 100);
        return hitBox;
    }
    @Override public Rectangle attackBox() {
        return null;
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(position.x + 140,
                position.y + 5,
                80, 20);
        return collisionBox;
    }

    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }
}
