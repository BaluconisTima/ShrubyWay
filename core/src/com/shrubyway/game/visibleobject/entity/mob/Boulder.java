package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.entity.EntityManager;

public class Boulder extends Mob {
    @Override public void ai(Vector2 playerPosition, float delta) {
        closeAi(140, playerPosition, delta);
    }

    public Boulder(float x, float y) {
        entityID = 8;
        id = 7;
        health = new Health(20);
        speed = 1.5f;
        allowedMotion = true;
        attackCooldown = 3.5f;
        action = 0;
        damage = 8f;
        regionWidth = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
        waterOverlayScale = 1.6f;
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
        collisionBox.change(position.x + regionWidth / 2 - 60,
                position.y + 5,
                120, 30);
        return collisionBox;
    }

    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }


}
