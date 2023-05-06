package com.shrubyway.game.shapes;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;


public class Circle{
    public final Vector2 centerPosition = new Vector2();
    public float radius;
    private final ShapeRenderer shapeRenderer;

    public Circle(float x, float y, float radius) {
        centerPosition.set(x, y);
        this.radius = radius;
        shapeRenderer = new ShapeRenderer();
    }

    public boolean overlaps(Circle x) {
        return (centerPosition.dst2(x.centerPosition) <= x.radius + radius);
    }
    public boolean overlaps(Rectangle x) {
        Vector2 closestPoint = new Vector2();
        closestPoint.x = Math.max(x.topLeftCorner.x, Math.min(centerPosition.x, x.bottomRightCorner.x));
        closestPoint.y = Math.max(x.topLeftCorner.y, Math.min(centerPosition.y, x.bottomRightCorner.y));
        return (centerPosition.dst2(closestPoint) < radius * radius);
    }

    public void render() {
       /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(centerPosition.x, centerPosition.y, Radius);
        shapeRenderer.end(); */
    }

}
