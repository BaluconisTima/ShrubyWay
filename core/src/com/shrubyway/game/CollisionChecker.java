package com.shrubyway.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shrubyway.game.shapes.Rectangle;

public class CollisionChecker {
    private World world;


    static public float scale = 0.01f;

    public void init() {
        world = new World(new Vector2(0, 0), true);
    }

    public void clear() {
        world.dispose();
        world = new World(new Vector2(0, 0), true);
    }

    public CollisionChecker() {
        init();
    }


    private Body addDymanic(Rectangle rectangle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        float width = rectangle.bottomRightCorner.x - rectangle.topLeftCorner.x,
                height = rectangle.bottomRightCorner.y - rectangle.topLeftCorner.y;
        bodyDef.position.set((rectangle.topLeftCorner.x + width/2) * scale, (rectangle.topLeftCorner.y + height/2) * scale);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2 * scale, height/2 * scale);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }

    private Body createStatic(Rectangle rectangle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        float width = rectangle.bottomRightCorner.x - rectangle.topLeftCorner.x,
                height = rectangle.bottomRightCorner.y - rectangle.topLeftCorner.y;
        bodyDef.position.set((rectangle.topLeftCorner.x + width/2) * scale, (rectangle.topLeftCorner.y + height/2) * scale);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2 * scale, height/2 * scale);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }


    public Body add(Rectangle rectangle, Vector2 movement) {
        Body body = addDymanic(rectangle);
        body.setLinearVelocity(movement);
        return body;
    }

    public Body addStatic(Rectangle rectangle) {
        Body body = createStatic(rectangle);
        return body;
    }

    public void process(float delta) {
        world.step(delta, 6, 2);
    }

    Vector2 temp = new Vector2();


    public Vector2 getMovement(Body body, float x, float y) {
        float deltaX = body.getPosition().x/scale - x;
        float deltaY = body.getPosition().y/scale - y;
        temp.set(deltaX, deltaY);
        return temp;
    }


    public void setLinearVelocity(Body body, Vector2 movement) {
        movement.scl(scale);
        body.setLinearVelocity(movement);
    }

    Vector2 change(Body body, Rectangle before) {
        float n = (before.bottomRightCorner.x - before.topLeftCorner.x) / 2,
                m = (before.bottomRightCorner.y - before.topLeftCorner.y) / 2;
        Vector2 result = new Vector2();
        result.x = (body.getPosition().x/ scale - before.topLeftCorner.x)  - n;
        result.y = (body.getPosition().y/ scale - before.topLeftCorner.y)  - m;
        return result;
    }

    public void dispose() {
        world.dispose();
    }
}
