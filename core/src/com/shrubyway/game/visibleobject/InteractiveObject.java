package com.shrubyway.game.visibleobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.shrubyway.game.CollisionChecker;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;

public class InteractiveObject extends VisibleObject {

    protected Rectangle collisionBox = null;
    protected Body CollisionBody = null;

    protected Rectangle hitBox = null;
    protected Rectangle attackBox = null;
    public float damage = 0;

    public int damageLevel = 0;
    public int throwLevel = 0;

    public float damage() {
        if(damageLevel == 0) return damage;
        return damage * ElementPumping.getDamageMultiplier(damageLevel);
    }

    public Rectangle collisionBox() {
        return collisionBox;
    }


    private void correctBody() {
        CollisionBody.setTransform((collisionBox.topLeftCorner.x + collisionBox.bottomRightCorner.x) / 2 * CollisionChecker.scale,
                (collisionBox.topLeftCorner.y + collisionBox.bottomRightCorner.y)/2 * CollisionChecker.scale, 0);
    }

    public Body getCollisionBody() {
        if(CollisionBody == null) {
            createBody();
        }
        correctBody();
        return CollisionBody;
    }

    public Body getCollisionBodyToChange() {
        if(CollisionBody == null) {
            createBody();
        }
        return CollisionBody;
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

    public void hideBody() {
        if(CollisionBody == null) {
           return;
        }
       CollisionBody.setActive(false);
    }

    public void unhideBody() {
        if(CollisionBody == null) {
            return;
        }
        CollisionBody.setActive(true);
    }

    protected void createBody() {
        if(CollisionBody == null) {
            CollisionBody = Game.collisionChecker.addStatic(collisionBox());
        }
    }

    protected void deleteBody() {
        if(CollisionBody != null) {
            CollisionBody.getWorld().destroyBody(CollisionBody);
            CollisionBody = null;
        }
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
