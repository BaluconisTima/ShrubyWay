package com.shrubyway.game.visibleobject.bullet;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.entity.Entity;

public class Wind extends Bullet {
    float power = 0.5f;
    float radius = 100;
    Vector2 tempDirection = new Vector2(0,0);

    public Wind(Vector2 direction, Vector2 position, VisibleObject whoThrow, float power, float radius) {
        this.direction = new Vector2(direction);
        this.position = new Vector2(position);
        this.whoThrow = whoThrow;
        this.power = power;
        this.radius = radius;
        throwingTime = AnimationGlobalTime.time();
        speed = 15;
    }
    @Override
    public void processBullet(float delta) {
        if(power < 0.02f) {
           die();
        }
        direction.nor();
        direction.scl(power * delta * 600);
        for(VisibleObject object: Game.objectsList.getList()) {
            if (object instanceof Entity ent) {
                if (ent == whoThrow) continue;
                tempDirection.set(ent.positionCenter().x - position.x, ent.positionCenter().y - position.y);
                if (tempDirection.len() < radius) {
                    ent.addMomentum(direction);
                }
            }
            if (object instanceof Bullet bullet) {
                if (tempDirection.len() < radius) {
                    if (bullet instanceof Wind) continue;
                    bullet.direction.nor();
                    bullet.direction.scl(power * delta * 600);
                    bullet.direction.add(direction);
                    bullet.direction.nor();
                }
            }
        }
        power *= Math.pow(0.90f, delta * 60);
        radius /= Math.pow(0.93f, delta * 60);
        super.processBullet(delta);
    }

    @Override
    public void render() {
      ///  Rectangle tempRectangle = new Rectangle(position.x - radius/2,position.y - radius/2,
          //      radius,radius);
       // tempRectangle.render(1, 1, 1, power);
    }
}
