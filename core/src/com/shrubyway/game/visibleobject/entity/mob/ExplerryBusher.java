package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundAtPosition;
import com.shrubyway.game.visibleobject.entity.EntityManager;

public class ExplerryBusher extends Mob{
    @Override public void ai(Vector2 playerPosition, float delta) {
        longRangeAi(600, 500, playerPosition, null, delta);
    }

    public ExplerryBusher(float x, float y) {
        entityID = 4;
        id = 3;
        health = new Health(15);
        speed = 6f;
        allowedMotion = true;
        attackCooldown = 0f;
        throwCooldown = 4f;
        action = 0;
        damage = 0f;
        regionWidth =  EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        position.set(x - regionWidth / 2, y);
        target.set(x - regionWidth / 2, y);
    }
    @Override
    public void throwItem(Vector2 shootPosition, Item item, boolean rotating) {
        if(!canThrow()) return;
        GlobalSoundManager.addSound(new SoundAtPosition(EntityManager.soundDamage[entityID], positionCenter()));
        lastThrowTime = AnimationGlobalTime.time();
        Game.objectsList.add(MobsManager.newOf(2, positionCenter().x, positionCenter().y));
    }

    @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 150,
                position.y + 20,
                70, 140);
        return hitBox;
    }
    @Override public Rectangle attackBox() {
        return null;
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(position.x + 128,
                position.y + 5,
                105, 30);
        return collisionBox;
    }

    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }

}
