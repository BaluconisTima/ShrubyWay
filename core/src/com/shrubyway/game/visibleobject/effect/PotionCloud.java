package com.shrubyway.game.visibleobject.effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.animation.Animator;
import com.shrubyway.game.item.Potion.Potion;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.entity.Entity;

public class PotionCloud extends VisibleEffect {
    float creationTime;
    Potion potion;

    Vector2 positionCenter = new Vector2();
    Animation<TextureRegion> animation;
    static Animator animator = new Animator();
    public PotionCloud(float x, float y, Potion potion) {
        position.set(x-150, y-150);
        this.potion = potion;
        creationTime = AnimationGlobalTime.time();
        animation = animator.toAnimation(ShrubyWay.assetManager.get("effects/EffectCloud.png", Texture.class),
                10, 0, 0);
        this.interactive = true;
    }

    @Override public void apply(VisibleObject visibleObject) {
        if (applied) {
            return;
        }
        if (visibleObject instanceof Entity) {
            Entity entity = (Entity) visibleObject;
            positionCenter.set(this.position.x + animation.getKeyFrame(0).getRegionWidth() / 2,
                    this.position.y + animation.getKeyFrame(0).getRegionHeight() / 2);

            float distance = positionCenter.dst(entity.positionCenter());
            if(distance < 150)
                potion.ApplyEffect(entity);
        }

    }

    void destroy() {
        Game.objectsList.del(this);
    }

    @Override public void render() {
        if(animation.isAnimationFinished(AnimationGlobalTime.time() - creationTime)) {
            destroy();
            return;
        }
        GlobalBatch.batch.setColor(potion.color);
        GlobalBatch.render(animation.getKeyFrame(
                AnimationGlobalTime.time() - creationTime, false
        ), position.x, position.y);
        GlobalBatch.batch.setColor(1, 1, 1, 1);
    }
}
