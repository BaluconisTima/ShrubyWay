package com.shrubyway.game;

import com.badlogic.gdx.math.Vector2;

public class CameraEffects {
    static Vector2 addPositionExplosion = new Vector2(0, 0);
    static float explosionPower = 0;
    static public void newExplosion(float power) {
          explosionPower += power;
    }

    static public void update() {
        addPositionExplosion.scl(0.6f);
        explosionPower *= 0.7f;
        Vector2 direction = new Vector2(0, explosionPower);
        direction.rotate((float) (Math.random() * 360));
        addPositionExplosion.add(direction);
    }

    static public Vector2 getAddPositionExplosion() {
        return addPositionExplosion;
    }

}
