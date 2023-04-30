package com.shrubyway.game.visibleobject;

import com.shrubyway.game.shapes.Rectangle;

public class InteractiveObject extends VisibleObject {
    protected Rectangle collisionBox = null;
    protected Rectangle hitBox = null;
    protected Rectangle attackBox = null;

    protected static Rectangle badBox = new Rectangle(0,0,-1,-1);

    public Rectangle collisionBox() {
        return collisionBox;
    }

    public Rectangle hitBox() {
        return hitBox;
    }

    public Rectangle attackBox() {
        return attackBox;
    }
}
