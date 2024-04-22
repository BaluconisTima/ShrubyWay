package com.shrubyway.game.visibleobject.effect;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.CameraEffects;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundAtPosition;

public class FruitExplosion extends Explosion {

    public FruitExplosion(float x, float y) {

        super(x - 400, y - 400, 25, 700);
        float explosionPower = 350;
        Vector2 playerPosition = Game.player.positionCenter();

        explosionPower *= Math.max(0, (1 - playerPosition.dst(position) / 1700));


        CameraEffects.newExplosion(explosionPower);
        GlobalSoundManager.addSound(new SoundAtPosition(ShrubyWay.assetManager.get("sounds/EFFECTS/FruitExplosion.ogg",
                Sound.class),
                new Vector2(x, y)));
        animation = animator.toAnimation(ShrubyWay.assetManager.get("effects/fruitExplosion.png", Texture.class),
                12, 0, 0);

    }
}
