package com.shrubyway.game.item;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;

public class ThrowableItem extends Bullet {
    int id;
    Sound sound;
    Boolean rotation;
    long RotationSound;

    float damageAd = 1, speedScale = 1;

    public ThrowableItem(Vector2 startPosition, Vector2 finishPosition, Item item,
                         VisibleObject thrower, boolean rotation) {
           this.rotation = rotation;
            whoThrow = thrower;
            throwingTime = AnimationGlobalTime.time();
            id = item.id;
            position.set(startPosition);
            speed = 20f;
            damage = ItemManager.throwingDamage[id];
            direction =
                    new Vector2(finishPosition.x - position.x, finishPosition.y - position.y);
            direction.nor();
            direction.scl(speed);
            sound = ShrubyWay.assetManager.get("sounds/EFFECTS/rotation.ogg", Sound.class);
            RotationSound = sound.play(SoundSettings.soundVolume);
    }
    public ThrowableItem(Vector2 startPosition, Vector2 finishPosition, Item item,
                         VisibleObject thrower, boolean rotation, float damageAd, float speedScale) {
        this.rotation = rotation;
        this.damageAd = damageAd;
        this.speedScale = speedScale;
        whoThrow = thrower;
        throwingTime = AnimationGlobalTime.time();
        id = item.id;
        position.set(startPosition);
        speed = 20f * speedScale;
        damage = ItemManager.throwingDamage[id] + damageAd;
        direction =
                new Vector2(finishPosition.x - position.x, finishPosition.y - position.y);
        direction.nor();
        sound = ShrubyWay.assetManager.get("sounds/EFFECTS/rotation.ogg", Sound.class);
        RotationSound = sound.play(SoundSettings.soundVolume);
    }

    @Override public Rectangle attackBox() {
        if(attackBox == null) {
            attackBox = new Rectangle(0,0,0,0);
        }
        attackBox.change(position.x -32, position.y - 32, 64, 64);
        return attackBox;
    }
    Vector2 temp = new Vector2(0,0);

    public void processBullet(Vector2 playerPossition, float delta) {
           super.processBullet(delta);
           if(rotation) {
               temp.set(playerPossition.x, playerPossition.y);
               temp.sub(position.x, position.y);
               float temp1 = temp.len();
               temp1 = temp1 / MapSettings.soundDistance;
               temp1 = Math.min(temp1, 1);
           sound.setVolume(RotationSound, (1 - temp1) * SoundSettings.soundVolume);
           }
    }


        @Override public void die() {
        sound.setVolume(RotationSound, 0);
        VisibleObject deathObject = ItemManager.getItemDeath(id, positionCenter().x, positionCenter().y);
        if(deathObject != null) {
            Game.objectsList.add(deathObject);
        }
        super.die();
    }

    @Override public void render() {

        GlobalBatch.render(ItemManager.itemTexture[id], position.x -
                        ItemManager.itemTexture[id].getRegionWidth() * 0.25f,
                position.y - ItemManager.itemTexture[id].getRegionHeight() * 0.25f,
                ItemManager.itemTexture[id].getRegionWidth() * 0.5f,
                ItemManager.itemTexture[id].getRegionHeight() * 0.5f,
                (AnimationGlobalTime.time() - throwingTime) *  1500);

        attackBox().render();

    }

    @Override public Vector2 positionCenter() {
        return position;
    }
}
