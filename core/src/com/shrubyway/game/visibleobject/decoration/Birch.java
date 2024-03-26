package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundAtPosition;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Birch extends Decoration{

    {
        id = 8;
    }
    @Override public void hit(float damage, Vector2 hitPosition) {
        super.hit(damage, hitPosition);
        GlobalSoundManager.addSound(new SoundAtPosition(
                ShrubyWay.assetManager.get("sounds/EFFECTS/bush.ogg", Sound.class), position));
        if(Math.random() < 0.2) {
            Game.objectsList.add(new VisibleItem(ItemManager.newItem(3),
                    position().x + halfTextureWidth  + ((float) Math.random() * 200f - 100f),
                    position().y + 50, new Vector2(0, -0.2f)));
        }
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 28,
                    position.y + 15, 70, 15);

        collisionBox.change(position.x + halfTextureWidth - 24,
                position.y + 15, 50, 15);
    }
    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 15,
                position.y + 50, 40, 100);
    }
}
