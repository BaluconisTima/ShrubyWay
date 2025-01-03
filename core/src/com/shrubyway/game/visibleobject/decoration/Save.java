package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;

public class Save extends Decoration{
    {
        id = 0;
    }
    @Override public void change(float x, float y, int i, int j) {
        halfTextureWidth = DecorationsManager.texture[id].getKeyFrame(0f).getRegionWidth() / 2f;
        DecorationsManager.texture[id].setPlayMode(Animation.PlayMode.LOOP);
        position.set(x - halfTextureWidth + com.shrubyway.game.map.MapSettings.TYLESIZE/2, y + 30);
        decorationI = i;
        decorationJ = j;
    }
    @Override
    public void render() {
        GlobalBatch.render(DecorationsManager.texture[id].getKeyFrame(AnimationGlobalTime.time()
                        - lastHitTime), Math.round(position.x),
                Math.round(position.y - 115));
        collisionBox().render();
        hitBox().render();
        if(pointWithArrow) {
            GlobalBatch.render(arrow.getKeyFrame(AnimationGlobalTime.time(), true), Math.round(position.x + halfTextureWidth - 45),
                    Math.round(position.y + 150));
        }
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 60,
                    position.y + 20, -1, -1);
        else collisionBox.change(position.x + halfTextureWidth - 60, position.y + 20, -1, -1);
    }
    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,-1,-1);
        hitBox.change(position.x + halfTextureWidth - 30,
                position.y + 30, -1, -1);
    }

    @Override  public void setInteractionBox() {
        if(interactionBox == null) interactionBox = new Rectangle(0,0,-1,-1);
        interactionBox.change(position.x + halfTextureWidth - 30,
                position.y + 30, 60, 60);
    }

    @Override public void interact() {
        Game.saveGame(true);
    }
}
