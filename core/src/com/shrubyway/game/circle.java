package com.shrubyway.game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;


public class circle {
    public final Vector2 centerPosition = new Vector2();
    public float Radius;
    private final ShapeRenderer shapeRenderer;
    public circle(float x, float y, float radius) {
        centerPosition.set(x, y);
        Radius = radius;
        shapeRenderer = new ShapeRenderer();
    }

    public boolean overlaps(circle x) {
        return (centerPosition.dst2(x.centerPosition) <= x.Radius + Radius);
    }
    public boolean overlaps(Rectangle x) {
        Vector2 closestPoint = new Vector2();
        closestPoint.x = Math.max(x.topLeftCorner.x, Math.min(centerPosition.x, x.bottomRightCorner.x));
        closestPoint.y = Math.max(x.topLeftCorner.y, Math.min(centerPosition.y, x.bottomRightCorner.y));
        return (centerPosition.dst2(closestPoint) < Radius * Radius);
    }

    public void render() {
       /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(centerPosition.x, centerPosition.y, Radius);
        shapeRenderer.end(); */
    }

}
