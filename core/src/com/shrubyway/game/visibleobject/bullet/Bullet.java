package com.shrubyway.game.visibleobject.bullet;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.VisibleObject;


public abstract class Bullet extends InteractiveObject {
    public Vector2 direction;
    protected float speed = 1f;
    public VisibleObject whoThrow;
    public float throwingTime;
    Vector2 tmp = new Vector2(0,0);

    public void tryMove(float delta) {
        tmp.set(direction);
        tmp.nor();
        tmp.scl(speed * delta * 60f);
        position.add(tmp);
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

