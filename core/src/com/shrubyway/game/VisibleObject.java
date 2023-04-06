package com.shrubyway.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class VisibleObject implements Comparable<VisibleObject> {
    final Vector2 position = new Vector2();


    public void Render(Batch batch){
    };

    public Vector2 positionBottom() {
        return position;
    }

    @Override public int compareTo(VisibleObject o) {
        Float y1 = -(positionBottom().y), y2 = -o.positionBottom().y;
        return (y1.compareTo(y2));
    }
}
