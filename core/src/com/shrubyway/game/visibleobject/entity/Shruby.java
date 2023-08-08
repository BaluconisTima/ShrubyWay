package com.shrubyway.game.visibleobject.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.Health;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.ObjectsList;


public class Shruby extends Entity {
    int id = 0;
    public Shruby(float x, float y) {
        damage = 2f;
        health = new Health(20, 0.3f);
        speed = 10f;
        allowedMotion = false;
        action = 4;
        throwCooldown = 0.7f;
        position.set(x, y);
        regionWidth = (EntityManager.animations[id].get(0).get(0)[0].getKeyFrame(0)).getRegionWidth();
        regionHeight = EntityManager.animations[id].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        Sound sound = GlobalAssetManager.get("sounds/EFFECTS/PortalOut.ogg", Sound.class);
        sound.play(SoundSettings.soundVolume);
        animationTime = 0;
    }

   @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 150,
                position.y + 40,
                70, 140);
        return hitBox;
   }
    @Override public Rectangle attackBox() {
        if(attackBox == null)
            attackBox = new Rectangle(0,0,0,0);

        if(action == 2 && attacking) {
            switch (faceDirection) {
                case 0:
                    attackBox.change(position.x + 75,
                            position.y - 50,
                            210, 100);
                    break;
                case 1:
                    attackBox.change(position.x + 75,
                            position.y + 120,
                            210, 100);
                    break;
                case 2:
                    attackBox.change(position.x,
                            position.y + 55,
                            130, 70);
                    break;
                case 3:
                    attackBox.change(position.x + 240,
                            position.y + 55,
                            130, 70);
                    break;
            }
        } else {
            attackBox.change(0,
                    0,
                    -1, -1);
        }
        return attackBox;
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(position.x + 138,
                position.y + 5,
                95, 15);
        return collisionBox;
    }

    @Override
    public void getDamage(float damage, Vector2 hitPosition) {
        if(health.getHealth() > 0 && damage != 0) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/ShrabyDamage.ogg"));
            sound.play(SoundSettings.soundVolume);
        }
        super.getDamage(damage, hitPosition);
    }

    public boolean canAct() {
        return (allowedMotion);
    }

    @Override public void die() {
        if(action == 3) return;
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/ShrabyDeath1.wav"));
        sound.play(SoundSettings.soundVolume);
        animationTime = AnimationGlobalTime.time();
        allowedMotion = false;
        action = 3;
    }

    @Override public void update() {
        super.update();
        faceDirection = (byte)Math.min(faceDirection,
                (EntityManager.animations[id].get(action).size() - 1));

        if(action == 3 && EntityManager.animations[id].get(action).get(faceDirection)[inLiquid ? 1: 0].
                isAnimationFinished(AnimationGlobalTime.time() - animationTime)) {
            ObjectsList.del(this);
        }

        if(!allowedMotion) {
            if(action != 3 &&
                    EntityManager.animations[id].get(action).get(faceDirection)[inLiquid ? 1: 0].
                            isAnimationFinished(AnimationGlobalTime.time() - animationTime)) {
                allowedMotion = true;
                action = 0;
                animationTime = AnimationGlobalTime.time();
            }
        }
    }

    public void updateAnimation(int action) {
        if(!allowedMotion) return;
        if(this.action != action) {
            this.action = action;
            animationTime = AnimationGlobalTime.time();
        }
    }
    @Override public void render() {
        if(health.timeAfterHit() < 0.2f) {
            GlobalBatch.batch.setColor(1, health.timeAfterHit() * 5f, health.timeAfterHit() * 5f, 1);
        }
        EntityManager.animations[id].get(action).get(faceDirection)[inLiquid ? 1: 0].setFrameDuration(1f/(2.4f * getSpeed()));
        GlobalBatch.render(EntityManager.animations[id].get(action).get(faceDirection)[inLiquid ? 1: 0]
                           .getKeyFrame(AnimationGlobalTime.time() - animationTime, EntityManager.looping[id][action]),
                   Math.round(position.x), Math.round(position.y) - (inLiquid ? -5 : 83));
        GlobalBatch.batch.setColor(1, 1, 1, 1);
        super.render();
    }


    @Override public Vector2 position() {
        return position;
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }

    public Vector2 positionItemDrop() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 50);
        return tempPosition;
    }







}