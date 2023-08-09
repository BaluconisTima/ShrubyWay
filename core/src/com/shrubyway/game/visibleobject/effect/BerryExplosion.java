package com.shrubyway.game.visibleobject.effect;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.CameraEffects;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundAtPosition;

public class BerryExplosion extends Explosion {

    public BerryExplosion(float x, float y, Vector2 playerPosition) {

        super(x - 240, y - 240, 8, 300);
        float explosionPower = 100;

        explosionPower *= Math.max(0, (1 - playerPosition.dst(position) / 1700));


        CameraEffects.newExplosion(explosionPower);
        GlobalSoundManager.addSound(new SoundAtPosition(GlobalAssetManager.get("sounds/EFFECTS/berryExplosion.wav",
                Sound.class),
                new Vector2(x, y)));
        animation = animator.toAnimation(GlobalAssetManager.get("effects/berryExplosion.png", Texture.class),
                13, 0, 0);

    }
}
