package com.shrubyway.game.visibleobject.effect;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.animation.Animator;
import com.shrubyway.game.visibleobject.ObjectsList;

public abstract class Explosion extends Effect{
    float power, radius, creationTime;
    Vector2 positionCenter = new Vector2();
    public boolean damaged = false;
    Animation<TextureRegion> animation;
    static Animator animator = new Animator();
    public Explosion(float x, float y, float power, float radius) {
        creationTime = AnimationGlobalTime.time();
        position.set(x, y + 120);
        this.power = power;
        this.radius = radius;
    }


    public int getDamage(Vector2 position) {
        positionCenter.set(this.position.x + animation.getKeyFrame(0).getRegionWidth() / 2,
                this.position.y - 120 + animation.getKeyFrame(0).getRegionHeight() / 2);

        float distance = positionCenter.dst(position);
        return (int) (power * Math.max(0, (1 - distance / radius)));
    }

    public Vector2 getMomentum(Vector2 position) {
        positionCenter.set(this.position.x + animation.getKeyFrame(0).getRegionWidth() / 2,
                this.position.y - 120 + animation.getKeyFrame(0).getRegionHeight() / 2);

        float distance = positionCenter.dst(position);
        Vector2 direction = new Vector2((position.x - positionCenter.x), (position.y - positionCenter.y));
        direction.nor();
        float powerMomentum = (float) (power * Math.pow(Math.max(0, (1 - distance / 1.15 / radius)), 0.75));
        return new Vector2(direction.x * powerMomentum,
                direction.y * powerMomentum);
    }

    void destroy() {
        ObjectsList.del(this);
    }

    @Override public void render() {
        if(animation.isAnimationFinished(AnimationGlobalTime.time() - creationTime)) {
            destroy();
            return;
        }
        GlobalBatch.render(animation.getKeyFrame(
                AnimationGlobalTime.time() - creationTime, false
        ), position.x, position.y - 120);
    }
}
