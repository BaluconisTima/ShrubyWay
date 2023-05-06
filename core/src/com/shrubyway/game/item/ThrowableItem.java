package com.shrubyway.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;

import javax.swing.text.Position;
import java.io.Serializable;

public class ThrowableItem extends Bullet {
    int id;
    Sound sound;
    Boolean rotation;
    long RotationSound;

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
            sound = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/rotation.ogg"));
            RotationSound = sound.play();
    }

    @Override public Rectangle attackBox() {
        if(attackBox == null) {
            attackBox = new Rectangle(0,0,0,0);
        }
        attackBox.change(position.x -32, position.y - 32, 64, 64);
        return attackBox;
    }
    Vector2 temp = new Vector2(0,0);

    public void processBullet(Vector2 playerPossition) {
           super.processBullet(playerPossition);
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
        super.die();
    }

    @Override public void render(Batch batch) {
        batch.draw(ItemManager.itemTexture[id],
                Math.round(position.x -
                        ItemManager.itemTexture[id].getRegionWidth() / 2),
                Math.round(position.y -
                        ItemManager.itemTexture[id].getRegionHeight() / 2) ,
                Math.round(ItemManager.itemTexture[id].getRegionWidth() * 0.5f),
                Math.round(ItemManager.itemTexture[id].getRegionHeight() * 0.5f),
                ItemManager.itemTexture[id].getRegionWidth(),
                ItemManager.itemTexture[id].getRegionHeight(),
                0.5f,
                0.5f,
                (AnimationGlobalTime.time() - throwingTime) *  1500);

    }

    @Override public Vector2 positionCenter() {
        return position;
    }
}
