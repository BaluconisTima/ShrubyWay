package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.visibleobject.entity.Entity;

public class Mob extends Entity {
    int id;
    protected Vector2 target = new Vector2(0,0);
    protected float lastTargetUpdate = 0;
    protected float targetUpdateInterval = 0.5f;

    public void ai(Vector2 playerPosition) {

    }
}
