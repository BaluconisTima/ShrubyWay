package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.shrubyway.game.AnimationLoader;
import com.shrubyway.game.Animator;
import com.shrubyway.game.Bullet;
import com.shrubyway.game.Rectangle;
import com.shrubyway.game.VisibleObject;

import java.util.TreeSet;

abstract class Entity extends VisibleObject {
    protected Rectangle hitBox;
    protected float health;

    protected boolean onFire = false;
    protected Rectangle collisionBox = new Rectangle(0,0,-1,-1);
    protected byte faceDirection = 0;
    static protected  Animation<TextureRegion> animations[][][];
    protected int action = 0;
    protected boolean inLiquid = false;
    protected float speed = 10f;
    protected boolean isRunning = false;
    protected Animator animator = new Animator();
    protected float regionWidth, regionHeight;
    AnimationLoader animationLoader = new AnimationLoader();
    Vector2 tempPosition = new Vector2(0,0);

    private float lastStepTime = 0f;
    private float stepCooldown = 0.3f;

    private char lastTile = '0';
    Boolean canMove = true;
    static Sound soundAttack = Gdx.audio.newSound(Gdx.files.internal("Sounds/EFFECTS/Swing.ogg"));

    public float getSpeed() {
        float tempSpeed = 0; tempSpeed += (speed);
        if(inLiquid) tempSpeed *= 0.75;
        if(action != 3) {if (isRunning) tempSpeed *= 1.25;}
        else tempSpeed *= 1.5;
        return tempSpeed;
    }
    public void changeAnimationsFor(Vector2 direction) {
        if(!canMove) return;
        if(direction.x == 0 && direction.y == 0) action = 0;
        else action = 1;
        if(Math.abs(direction.x) > Math.abs(direction.y)) {
            if(direction.x < 0) faceDirection = 2;
            else if(direction.x > 0) faceDirection = 3;
        } else {
            if(direction.y < 0) faceDirection = 0;
            else if(direction.y > 0) faceDirection = 1;
        }
    }
    public void running(boolean running) {
        if(!canMove) return;
        isRunning = running;
    }
    public void tryMoveTo(Vector2 direction, TreeSet<VisibleObject> objects){
        if(!canMove) return;
        float tempSpeed = getSpeed();
        Vector2 tempDirection = new Vector2(direction);
        position.add(tempDirection.scl(tempSpeed));
        if(checkCollisions(objects)) {
            position.sub(tempDirection);
        }
        if(checkCollisions(objects)) {
            position.add(tempDirection);
        }
        changeAnimationsFor(direction);
    };


    private float attackCooldown = 0.3f;
    static float animationTime = 0f;
    private float lastAttackTime;
    public void attack() {
        if(!canMove && action != 3) return;
        if((TimeUtils.nanoTime() - lastAttackTime) / 1000000000.0f > attackCooldown) {
            animationTime = 0f;
            canMove = false;
            action = 3;
            lastAttackTime = TimeUtils.nanoTime();
            soundAttack.play();
        }
    }
    protected float shootCooldown = 0.5f;
    private float lastShootTime;
    public Bullet shoot(Vector2 mousePosition) {
        if(!canMove) return null;
        if((TimeUtils.nanoTime() - lastShootTime) / 1000000000.0f > shootCooldown) {
            lastShootTime = TimeUtils.nanoTime();
            Bullet bullet = new Bullet(positionCenter(), mousePosition);
            changeAnimationsFor(bullet.direction);
            return bullet;
        }
        return null;
    }
    @Override public Rectangle collisionBox(){
        return collisionBox;
    }
    protected boolean checkCollisions(TreeSet<VisibleObject> objects){
        Rectangle temp = collisionBox();
        for(VisibleObject object : objects) {
            if(object == this) continue;
            if(Math.abs(object.position.x - position.x) > 300) continue;
            if(Math.abs(object.position.y - position.y) > 300) continue;
            if(object.collisionBox().overlaps(temp)) {
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
    public Vector2 positionCenter() {
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
        if((TimeUtils.nanoTime() - lastStepTime) / 1000000000.0f  >= stepCooldown / (getSpeed() / 10)) {
            lastStepTime = TimeUtils.nanoTime();
            return true;
        }
        return false;
    }

}
