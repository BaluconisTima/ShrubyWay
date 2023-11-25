package com.shrubyway.game.visibleobject;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.shapes.Rectangle;

public class InteractiveObject extends VisibleObject {

    protected Rectangle collisionBox = null;
    protected Rectangle hitBox = null;
    protected Rectangle attackBox = null;
    protected float damage = 0;

    public int damageLevel = 0;
    public int throwLevel = 0;

    public float damage() {
        float dam = (int)damage;
        float damLevel = damageLevel;
        damLevel /= 7;
        while(Math.random() < damLevel) {
            dam *= 1.5;
            damLevel /= 3;
        }
        return dam;
    }

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

    @Override public void dispose() {
        super.dispose();
        if(collisionBox != null) collisionBox.dispose();
        if(hitBox != null) hitBox.dispose();
        if(attackBox != null) attackBox.dispose();
        collisionBox = null;
        hitBox = null;
        attackBox = null;
    }
}
