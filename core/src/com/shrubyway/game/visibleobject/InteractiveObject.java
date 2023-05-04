package com.shrubyway.game.visibleobject;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.shapes.Rectangle;

public class InteractiveObject extends VisibleObject {

    protected Rectangle collisionBox = null;
    protected Rectangle hitBox = null;
    protected Rectangle attackBox = null;
    public float damage = 0;
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

    Vector2 temp = new Vector2(0,0);
    public Vector2 positionCenter() {
        temp.set(position.x, position.y);
        return temp;
    }
}
