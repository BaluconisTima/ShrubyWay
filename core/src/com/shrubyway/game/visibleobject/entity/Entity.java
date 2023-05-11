package com.shrubyway.game.visibleobject.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.Health;

import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ThrowableItem;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.sound.SoundSettings;

import java.util.TreeSet;

abstract public class Entity extends InteractiveObject {



    public Health health;
    public byte faceDirection = 0;
    protected int action = 0;
    protected boolean inLiquid = false;
    protected Vector2 momentum = new Vector2(0, 0);
    protected float speed = 10f;
    protected boolean isRunning = false;
    protected float regionWidth, regionHeight;
    protected Vector2 tempPosition = new Vector2(0,0);

    protected float lastStepTime = 0f;
    protected float stepCooldown = 0.3f;

    protected char lastTile = '0';
    protected Boolean allowedMotion = false;
    static Sound soundAttack;

    static  {
        GlobalAssetManager.assetManager.load("sounds/EFFECTS/Swing.ogg", Sound.class);
        GlobalAssetManager.assetManager.finishLoading();
        soundAttack = GlobalAssetManager.assetManager.get("sounds/EFFECTS/Swing.ogg");
    }


    public float getSpeed() {
        float tempSpeed = 0; tempSpeed += (speed);
        if(inLiquid) tempSpeed *= 0.75;
        if(action != 2) {if (isRunning) tempSpeed *= 1.25;}
        else tempSpeed *= 1.5;
        return tempSpeed;
    }
    public void update() {
         tryMoveMomentum();
        attacking = false;
    }

    public void getDamage(float damage) {
        health.getDamage(damage);
        if(health.getHealth() <= 0) {
            die();
        }
    }
    protected Vector2 direction = new Vector2(0,0);
    public void getDamage(float damage, Vector2 hitPosition) {
        direction.set(positionCenter().x - hitPosition.x + (float)Math.random() * 30 - 15,
                positionCenter().y - hitPosition.y  + (float)Math.random() * 30 - 15);
        direction.nor();
        direction.scl(damage * 100f / health.getMaxHealth());
        getDamage(damage);

        momentum.add(direction);
    }


    protected void changeAnimationsFor(Vector2 direction, int actionLocal) {
        if(!allowedMotion) return;
        action = actionLocal;

        if(Math.abs(direction.x) > Math.abs(direction.y)) {
            if(direction.x < 0) faceDirection = 2;
            else if(direction.x > 0) faceDirection = 3;
        } else {
            if(direction.y < 0) faceDirection = 0;
            else if(direction.y > 0) faceDirection = 1;
        }
    }
    public void running(boolean running) {
        if(!allowedMotion) return;
        isRunning = running;
    }
    protected Vector2 tempDirection = new Vector2(0,0);
    protected Vector2 tempDirection2 = new Vector2(0,0);
    protected final int collisionsEps = 10;

    public void tryMoveMomentum() {
        if(action == 3) return;
        momentum.scl(0.85f);
        tempDirection.set(momentum);
        tempDirection.scl(1f/collisionsEps);
        for(int i = 0; i < collisionsEps; i++) {
            position.add(tempDirection);
            if (checkCollisions()) {
                position.sub(tempDirection);
            }
            if (checkCollisions()) {
                position.add(tempDirection);
            }
        }
    }


    public boolean tryMoveTo(Vector2 direction){
        if(!allowedMotion) return false;
        direction.nor();

        float tempSpeed = getSpeed();
        tempDirection.set(direction);
        tempDirection.scl(tempSpeed);
        tempDirection.scl(1f/collisionsEps);
        boolean moved = false;

        for(int i = 0; i < collisionsEps; i++) {
            position.add(tempDirection);
            if (checkCollisions()) {
                position.sub(tempDirection);
            } else moved = true;

            if (checkCollisions()) {
                position.add(tempDirection);
            }
        }
        changeAnimationsFor(direction, direction.len() < 0.1f ? 0 : 1);
        return moved;
    };

    public void addMomentum(Vector2 x) {
        momentum.add(x);
    }

    public void die() {
    }


    protected float attackCooldown = 0.3f;
    protected float animationTime = 0f;
    protected float lastAttackTime;

    public void attack() {
        if(!allowedMotion && action != 2) return;

        if((AnimationGlobalTime.time() - lastAttackTime) > attackCooldown) {
            attacking = true;
            animationTime = AnimationGlobalTime.time();
            allowedMotion = false;
            action = 2;
            lastAttackTime = AnimationGlobalTime.time();
            soundAttack.play(SoundSettings.soundVolume);
        }
    }
    protected float throwCooldown = 0.5f;
    private float lastThrowTime;
    protected boolean attacking = false;

    public boolean canThrow() {
        if(!allowedMotion) return false;
        return (AnimationGlobalTime.time() - lastThrowTime >= throwCooldown);
    }
    public void throwItem(Vector2 mousePosition, Item item, boolean rotating) {
        if(!canThrow()) return;
        lastThrowTime = AnimationGlobalTime.time();
        Bullet bullet = new ThrowableItem(positionCenter(), mousePosition, item, this, rotating);
        changeAnimationsFor(bullet.direction, 2);
        ObjectsList.add(bullet);
    }

    @Override public Rectangle collisionBox(){return collisionBox;}
    @Override public Rectangle hitBox() {return hitBox;}

    protected boolean checkCollisions(){
        Rectangle temp = collisionBox();
        for(VisibleObject object : ObjectsList.getList()) {
            if(!(object instanceof InteractiveObject)) continue;
            if(object == this) continue;
              if(Math.abs(object.position.x - position.x) > 300) continue;
            if(Math.abs(object.position.y - position.y) > 300) continue;
            if(((InteractiveObject)object).collisionBox() == null) continue;
            if(((InteractiveObject)object).collisionBox().overlaps(temp)) {
                return true;
            }
        }
        return false;
    };
    public void liquidStatus(boolean isLiquid) {
        inLiquid = isLiquid;
    }
    public void changePosition(Vector2 positionNew) {
        position.set(positionNew);
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + regionHeight / 2);
        return tempPosition;
    }
    public Vector2 positionLegs() {
        tempPosition.set(position.x + regionWidth / 2, position.y);
        return tempPosition;
    }
    public boolean makingStep(char tile) {
        if(tile != lastTile) lastStepTime = 0f;
        lastTile = tile;
        if(action != 1) return false;
        if((AnimationGlobalTime.time() - lastStepTime)  >= stepCooldown / (getSpeed() / 10)) {
            lastStepTime = AnimationGlobalTime.time();
            return true;
        }
        return false;
    }

}
