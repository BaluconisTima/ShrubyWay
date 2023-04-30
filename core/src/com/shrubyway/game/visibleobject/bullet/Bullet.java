package com.shrubyway.game.visibleobject.bullet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.ObjectsList;
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
        ObjectsList.del(this);
    }

    public void processBullet(Vector2 playerPossition) {
        this.tryMove();
        if(this.throwingTime + 10f < AnimationGlobalTime.x) {
            this.die();
        }
    }


}

