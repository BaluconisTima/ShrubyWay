package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.TreeSet;

public class Entity extends VisibleObject{
    protected Rectangle hitBox;
    protected int Health;

    protected boolean onFire = false;
    protected Rectangle collisionBox = new Rectangle(0,0,-1,-1);
    protected byte FaceDirection = 0;  // 0 - DOWN, 1 - UP, 2 - LEFT, 3 - RIGHT
    protected boolean IsMoving = false;
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

    public float getSpeed() {
        float tempSpeed = 0; tempSpeed += (Speed);
        if(inLiquid) tempSpeed *= 0.85;
        if(isRunning) tempSpeed *= 1.25;
        return tempSpeed;
    }
    public void ChangeAnimationsFor(Vector2 direction) {
        if(direction.x == 0 && direction.y == 0) IsMoving = false;
        else IsMoving = true;
        if(Math.abs(direction.x) > Math.abs(direction.y)) {
            if(direction.x < 0) FaceDirection = 2;
            else if(direction.x > 0) FaceDirection = 3;
        } else {
            if(direction.y < 0) FaceDirection = 0;
            else if(direction.y > 0) FaceDirection = 1;
        }
    }
    public void Running(boolean running) {
        isRunning = running;
    }
    public void TryMoveTo(Vector2 direction, TreeSet<VisibleObject> objects){
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
        if(!IsMoving) return false;
        if((TimeUtils.nanoTime() - lastStepTime) / 1000000000.0f  >= StepCooldown / (getSpeed() / 10)) {
            lastStepTime = TimeUtils.nanoTime();
            return true;
        }
        return false;
    }

}
