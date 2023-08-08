package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.InteractiveObject;

abstract public class Decoration extends InteractiveObject {
    public int decorationI = 0, decorationJ = 0;
    int id;
    protected float lastInteraction = -100f;

    public void change(float x, float y, int i, int j) {
        position.set(x, y);
        decorationI = i;
        decorationJ = j;
    }

    public void setCollisionBox() {
    }

    public void setHitbox() {
    }

    @Override
    public Rectangle collisionBox() {
        setCollisionBox();
        return collisionBox;
    }

    @Override
    public Rectangle hitBox() {
        setHitbox();
        return hitBox;
    }

    @Override public void render(){
        GlobalBatch.render(DecorationsManager.texture[id].getKeyFrame(AnimationGlobalTime.time()
                        - lastInteraction), Math.round(position.x),
                Math.round(position.y));
        collisionBox().render();
        hitBox().render();
    };

    public void interact() {
        lastInteraction = AnimationGlobalTime.time();
    }



}
