package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.InteractiveObject;

abstract public class Decoration extends InteractiveObject {
    public int decorationI = 0, decorationJ = 0;
    public int id;
    protected float lastHitTime = -100f;

    public void change(float x, float y, int i, int j) {
        halfTextureWidth = DecorationsManager.texture[id].getKeyFrame(0f).getRegionWidth() / 2f;
        DecorationsManager.texture[id].setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + MapSettings.TYLESIZE/2, y + 30);
        decorationI = i;
        decorationJ = j;
    }
    Rectangle interactionBox;

    public void setCollisionBox() {
    }

    public void setHitbox() {
    }

    public void setInteractionBox() {
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

    public Rectangle interactionBox() {
        setInteractionBox();
        return interactionBox;
    }

    @Override public void render(){
        GlobalBatch.render(DecorationsManager.texture[id].getKeyFrame(AnimationGlobalTime.time()
                        - lastHitTime), Math.round(position.x),
                Math.round(position.y));
        collisionBox().render();
        hitBox().render();
    };
    float halfTextureWidth;
    @Override public Vector2 positionCenter() {
        return new Vector2(position.x + halfTextureWidth, position.y);
    }

    public void hit() {
        lastHitTime = AnimationGlobalTime.time();
    }

    public void interact() {

    }



}
