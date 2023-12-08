package com.shrubyway.game;

import com.badlogic.gdx.math.Vector2;

public class CameraEffects {
    static Vector2 addPositionExplosion = new Vector2(0, 0);
    static float explosionPower = 0;
    static public void newExplosion(float power) {
          explosionPower += power;
    }
    static private boolean save = false, savePhase = false;
    static private float saveEffect = 1;
    static public void save() {
        savePhase = true;
    }

    static public Vector2 direction = new Vector2(0, 0);
    static public void updateExp(float delta) {
        addPositionExplosion.scl((float)Math.pow(0.6f, delta * 60));
        explosionPower *= Math.pow(0.7f, delta * 60);
        direction.set(0, explosionPower);
        direction.rotate((float) (Math.random() * 360));
        addPositionExplosion.add(direction);
    }

    static public void updateSave() {

        if(savePhase) {
            if(saveEffect >= 0.01f) {
                saveEffect *= 0.8f;
            } else {
                savePhase = false;
            }
        } else {
            saveEffect *= 1.3f;
            if(saveEffect >= 1) {
                saveEffect = 1;
            }
        }
    }

    static public float getSaveEffect() {
        return saveEffect;
    }

    static public void update(float delta) {
         updateExp(delta);
         updateSave();
    }

    static public Vector2 getAddPositionExplosion() {
        return addPositionExplosion;
    }

    static public void dispose() {
        addPositionExplosion = null;
    }

}
