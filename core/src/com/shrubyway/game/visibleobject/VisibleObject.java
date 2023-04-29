package com.shrubyway.game.visibleobject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.shapes.Rectangle;

public abstract class VisibleObject implements Comparable<VisibleObject> {
    public final Vector2 position = new Vector2();
    public static long ids;
    long id = ids++;
    public void render(Batch batch){};
    public Vector2 position() {
        return position;
    }

    @Override public int compareTo(VisibleObject o) {
        Integer y1 = Math.round(-(position().y));
        Integer y2 = Math.round(-o.position().y);
        Integer x1 = Math.round(-(position().x));
        Integer x2 = Math.round(-o.position().x);
        if(y1.equals(y2)) {
                return (int) (id - o.id);
        }
        return (y1.compareTo(y2));
    }
}
