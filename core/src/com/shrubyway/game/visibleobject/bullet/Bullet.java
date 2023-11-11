package com.shrubyway.game.visibleobject.bullet;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.VisibleObject;


public abstract class Bullet extends InteractiveObject {
    public Vector2 direction;
    protected float speed;
    public VisibleObject whoThrow;
    public float throwingTime;

    public void tryMove(float delta) {
        Vector2 tempDirection = new Vector2(direction);
        tempDirection.scl(speed * delta * 3f);
        position.add(tempDirection);
    }

    public void die() {
        Game.objectsList.del(this);
    }

    public void processBullet(float delta) {
        this.tryMove(delta);
        if(this.throwingTime + 10f < AnimationGlobalTime.time()) {
            this.die();
        }
    }


}

