package com.shrubyway.game.visibleobject;

import com.badlogic.gdx.math.Vector2;

public abstract class VisibleObject implements Comparable<VisibleObject> {

    public Vector2 position = new Vector2();
    public static long ids;
    long id = ids++;
    public void render(){};
    public Vector2 position() {
        return position;
    }

    @Override public int compareTo(VisibleObject o) {
        Integer y1 = Math.round(-(position().y));
        Integer y2 = Math.round(-o.position().y);
        if(y1.equals(y2)) {
                return (int) (id - o.id);
        }
        return (y1.compareTo(y2));
    }

    public void dispose() {
        position = null;
    }
}
