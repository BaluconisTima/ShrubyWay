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
    static public void updateExp() {
        addPositionExplosion.scl(0.6f);
        explosionPower *= 0.7f;
        Vector2 direction = new Vector2(0, explosionPower);
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

    static public void update() {
         updateExp();
         updateSave();
    }

    static public Vector2 getAddPositionExplosion() {
        return addPositionExplosion;
    }

}
