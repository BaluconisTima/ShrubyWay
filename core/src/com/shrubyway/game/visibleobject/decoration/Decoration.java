package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.animation.Animator;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.VisibleObject;

abstract public class Decoration extends VisibleObject {
    public int decorationI = 0, decorationJ = 0;
    public char decorationType;
    static Animator animator = new Animator();
    protected float lastInteraction = -100f;

    public void change(float x, float y, int i, int j) {
        position.set(x, y);
        decorationI = i;
        decorationJ = j;
    }
    public void setCollisionBox() {
    }

    public void setHitbox() {
    }

    public Rectangle collisionBox() {
        setCollisionBox();
        return collisionBox;
    }
    public Rectangle hitBox() {
        setHitbox();
        return hitBox;
    }

    public void interact() {
        lastInteraction = AnimationGlobalTime.x;
    }
     public Decoration newTemp() {
        return null;
    }


}
