package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundAtPosition;
import com.badlogic.gdx.audio.Sound;

public class Dummy extends Decoration {
    Health health = new Health(10000000);
    {
        id = 21;
    }
    public int hitCounter = 0, throwCounter = 0;
    @Override public void hit(float damage, Vector2 hitPosition) {
        super.hit(damage, hitPosition);
        GlobalSoundManager.addSound(new SoundAtPosition(ShrubyWay.assetManager.get("sounds/EFFECTS/Dummy.ogg",
                Sound.class), position));
        health.getDamage(damage, hitPosition);
        health.heal(damage);
    }

    @Override
    public void hitWithMelee(float damage, Vector2 hitPosition) {
        super.hitWithMelee(damage, hitPosition);
        hitCounter++;
    }

    @Override
    public void hitWithProjectile(float damage, Vector2 hitPosition) {
        super.hitWithProjectile(damage, hitPosition);
        throwCounter++;
    }

    @Override
    public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 60,
                position.y + 30, 120, 75);
    }
    @Override
    public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 10,
                    position.y, 20, 10);
        else collisionBox.change(position.x + halfTextureWidth - 10,
                position.y, 20, 10);
    }

    {
        id = 21;
    }
}
