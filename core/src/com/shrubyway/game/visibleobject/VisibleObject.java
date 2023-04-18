package com.shrubyway.game.visibleobject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.shapes.Rectangle;

public abstract class VisibleObject implements Comparable<VisibleObject> {
    public final Vector2 position = new Vector2();
    static long ids;
    long id = ids++;
    protected Rectangle collisionBox = null;
    protected Rectangle hitBox = null;
    protected Rectangle attackBox = null;


    public void render(Batch batch){

    };
    public Rectangle collisionBox() {
        return collisionBox;
    }
    public Rectangle hitBox() {
        return hitBox;
    }
    public Rectangle attackBox() {
        return attackBox;
    }

    public Vector2 position() {
        return position;
    }

    @Override public int compareTo(VisibleObject o) {
        Integer y1 = Math.round(-(position().y));
        Integer y2 = Math.round(-o.position().y);
        Integer x1 = Math.round(-(position().x));
        Integer x2 = Math.round(-o.position().x);
        if(y1.equals(y2)) {
            if(x1.equals(x2)) {
                return (int) (id - o.id);
            }
            return (x1.compareTo(x2));
        }
        return (y1.compareTo(y2));
    }
}
