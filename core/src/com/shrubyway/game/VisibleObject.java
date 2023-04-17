package com.shrubyway.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class VisibleObject implements Comparable<VisibleObject> {
    final Vector2 position = new Vector2();
    public void render(Batch batch){

    };
    public Rectangle collisionBox() {
        return new Rectangle(position.x, position.y, -1, -1);
    }

    public Vector2 position() {
        return position;
    }

    @Override public int compareTo(VisibleObject o) {
        Integer y1 = Math.round(-(position().y));
        Integer y2 = Math.round(-o.position().y);
        Integer x1 = Math.round(-(position().x));
        Integer x2 = Math.round(-o.position().x);
        if(y1.equals(y2)) return (x1.compareTo(x2));
        return (y1.compareTo(y2));
    }
}
