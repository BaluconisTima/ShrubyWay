package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.TreeSet;

public class Entity extends VisibleObject{
    protected Rectangle hitBox;
    protected float Health;

    protected boolean onFire = false;
    protected Rectangle collisionBox = new Rectangle(0,0,-1,-1);
    protected byte FaceDirection = 0;
    static protected  Animation<TextureRegion> animations[][][];
    protected int action = 0;
    protected boolean inLiquid = false;
    protected float Speed = 10f;
    protected boolean isRunning = false;
    protected Animator animator = new Animator();
    protected float RegionWidth, RegionHeight;
    AnimationLoader animationLoader = new AnimationLoader();
    Vector2 tempPosition = new Vector2(0,0);

    private float lastStepTime = 0f;
    private float StepCooldown = 0.3f;

    private char lastTile = '0';
    Boolean canMove = true;
    static Sound SoundAttack = Gdx.audio.newSound(Gdx.files.internal("Sounds/EFFECTS/Swing.ogg"));

    public float getSpeed() {
        float tempSpeed = 0; tempSpeed += (Speed);
        if(inLiquid) tempSpeed *= 0.75;
        if(action != 3) {if (isRunning) tempSpeed *= 1.25;}
        else tempSpeed *= 1.5;
        return tempSpeed;
    }
    public void ChangeAnimationsFor(Vector2 direction) {
        if(!canMove) return;
        if(direction.x == 0 && direction.y == 0) action = 0;
        else action = 1;
        if(Math.abs(direction.x) > Math.abs(direction.y)) {
            if(direction.x < 0) FaceDirection = 2;
            else if(direction.x > 0) FaceDirection = 3;
        } else {
            if(direction.y < 0) FaceDirection = 0;
            else if(direction.y > 0) FaceDirection = 1;
        }
    }
    public void Running(boolean running) {
        if(!canMove) return;
        isRunning = running;
    }
    public void TryMoveTo(Vector2 direction, TreeSet<VisibleObject> objects){
        if(!canMove) return;
        objects.remove(this);
        float tempSpeed = getSpeed();
        Vector2 tempDirection = new Vector2(direction);
        position.add(tempDirection.scl(tempSpeed));
        if(checkCollisions(objects)) {
            position.sub(tempDirection);
        }
        if(checkCollisions(objects)) {
            position.add(tempDirection);
        }
        objects.add(this);
        ChangeAnimationsFor(direction);
    };


    private float AttackCooldown = 0.3f;
    static float animationTime = 0f;
    private float lastAttackTime;
    public void Attack() {
        if(!canMove && action != 3) return;
        if((TimeUtils.nanoTime() - lastAttackTime) / 1000000000.0f > AttackCooldown) {
            animationTime = 0f;
            canMove = false;
            action = 3;
            lastAttackTime = TimeUtils.nanoTime();
            SoundAttack.play();
        }
    }
    private float ShootCooldown = 0.5f;
    private float lastShootTime;
    public Bullet shoot(Vector2 mousePosition) {
        if(!canMove) return null;
        if((TimeUtils.nanoTime() - lastShootTime) / 1000000000.0f > ShootCooldown) {
            lastShootTime = TimeUtils.nanoTime();
            Bullet bullet = new Bullet(positionCenter(), mousePosition);
            ChangeAnimationsFor(bullet.Direction);
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
            if(Math.abs(object.position.x - position.x) > 300) continue;
            if(Math.abs(object.position.y - position.y) > 300) continue;
            if(object.collisionBox().overlaps(temp)) {
                return true;
            }
        }
        return false;
    };
    public void LiquidStatus(boolean isLiquid) {
        inLiquid = isLiquid;
    }
    public void changePosition(Vector2 positionNew) {
        position.set(positionNew);
    }
    public Vector2 positionCenter() {
        tempPosition.set(position.x + RegionWidth / 2, position.y + RegionHeight / 2);
        return tempPosition;
    }
    public Vector2 positionLegs() {
        tempPosition.set(position.x + RegionWidth / 2, position.y);
        return tempPosition;
    }


    public boolean makingStep(char tile) {
        if(tile != lastTile) lastStepTime = 0f;
        lastTile = tile;
        if(action != 1) return false;
        if((TimeUtils.nanoTime() - lastStepTime) / 1000000000.0f  >= StepCooldown / (getSpeed() / 10)) {
            lastStepTime = TimeUtils.nanoTime();
            return true;
        }
        return false;
    }

}
