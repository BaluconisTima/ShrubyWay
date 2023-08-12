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

    public void tryMove() {
        position.add(direction);
    }

    public void die() {
        Game.objectsList.del(this);
    }

    public void processBullet(Vector2 playerPossition) {
        this.tryMove();
        if(this.throwingTime + 10f < AnimationGlobalTime.time()) {
            this.die();
        }
    }


}

