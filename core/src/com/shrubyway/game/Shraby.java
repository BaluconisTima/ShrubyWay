package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.lang.Math;
import java.util.List;
import java.util.TreeSet;


public class Shraby extends VisibleObject {

    private byte FaceDirection = 0;  // 0 - DOWN, 1 - UP, 2 - LEFT, 3 - RIGHT
    private boolean IsMoving = false;
    private byte lastFaceDirection = 0;
    private boolean lastIsMoving = false;
    private boolean inLiquid = false;
    private String last_animation;

    private final float Speed = 10f;
    boolean isRunning = false;
    Animation<TextureRegion> CurrentAnimation;
    Animation<TextureRegion> CurrentAnimation_inLiquid;

    private Texture AnimationList = new Texture(Gdx.files.internal("SHRABY/AFK/DOWN.png"));
    Animator animator = new Animator();
    float RegionWidth, RegionHeight;

    public Shraby(float x, float y) {
      position.set(x, y);

        CurrentAnimation = animator.toAnimation(AnimationList, 30, 0,0);
        CurrentAnimation_inLiquid = animator.toAnimation(AnimationList, 30, 0, 50);
        RegionWidth = AnimationList.getWidth() / 30;
        RegionHeight = AnimationList.getHeight() / 30;
    }

    public Rectangle collisionbox = new Rectangle(0,0,0,0);
    @Override public Rectangle collisionBox() {
        collisionbox.change(position.x - RegionWidth / 2 + 55,
                position.y - RegionHeight / 2 + 5,
                RegionWidth - 115, 15);
        return collisionbox;
    }

    public void animationChange() {
        if(FaceDirection != lastFaceDirection || IsMoving != lastIsMoving) {
            lastFaceDirection = FaceDirection;
            lastIsMoving = IsMoving;
            String s = "SHRABY/";
            if(IsMoving) s += "WALK/";
            else s += "AFK/";
            switch(FaceDirection) {
                case 0: s += "DOWN"; break;
                case 1: s += "UP"; break;
                case 2: s += "LEFT"; break;
                case 3: s += "RIGHT"; break;
            }
            s += ".png";
            if(!s.equals(last_animation)) {
                last_animation = s;
                AnimationList = new Texture(Gdx.files.internal(s));
                CurrentAnimation = animator.toAnimation(AnimationList, 30, 0,0);
                CurrentAnimation_inLiquid = animator.toAnimation(AnimationList, 30, 0, 50);
            }
        }
    }
    @Override public void Render(Batch batch) {
           if(isRunning) {
               CurrentAnimation.setFrameDuration(1/36f);
               CurrentAnimation_inLiquid.setFrameDuration(1/36f);
           } else {
               CurrentAnimation.setFrameDuration(1/24f);
               CurrentAnimation_inLiquid.setFrameDuration(1/24f);
           }
           animationChange();
           TextureRegion temp =
                   new TextureRegion(CurrentAnimation.getKeyFrame(AnimationGlobalTime.x, true));

           if(inLiquid) {
               temp = CurrentAnimation_inLiquid.getKeyFrame(AnimationGlobalTime.x, true);
           }
           batch.draw(temp,
                   Math.round(position.x - RegionWidth / 2),
                   Math.round(position.y - RegionHeight / 2));
           collisionBox().render(batch);
    }
    public void Running(boolean running) {
        isRunning = running;
    }

    private boolean checkCollisions(Vector2 direction, TreeSet<VisibleObject> objects) {
        Rectangle temp = collisionBox();
        for(VisibleObject object : objects) {
            if(Math.abs(object.position.x - position.x) > 300) continue;
            if(Math.abs(object.position.y - position.y) > 300) continue;
            if(object.collisionBox().overlaps(temp)) {
                return true;
            }
        }
        return false;
    }
    public void TryMoveTo(Vector2 direction, TreeSet<VisibleObject> objects) {
        float tempSpeed = 0; tempSpeed += (Speed);
        if(inLiquid) tempSpeed *= 0.85;
        if(isRunning) tempSpeed *= 1.5;
        Vector2 tempDirection = new Vector2(direction);
        position.add(tempDirection.scl(tempSpeed));
        if(checkCollisions(direction, objects)) {
            position.sub(tempDirection);
        }
        if(checkCollisions(direction, objects)) {
            position.add(tempDirection);
        }
        ChangeAnimationsFor(direction);
    }
    public void ChangeAnimationsFor(Vector2 direction) {
        if(direction.x == 0 && direction.y == 0) IsMoving = false;
        else IsMoving = true;
        if(direction.y < 0) FaceDirection = 0;
        else if(direction.y > 0) FaceDirection = 1;
        else if(direction.x < 0) FaceDirection = 2;
        else if(direction.x > 0) FaceDirection = 3;
    }
    public Vector2 Position() {
        return position;
    }
    @Override public Vector2 positionBottom() {
        return new Vector2(position.x,
                position.y - RegionHeight/2);
    }

    public void LiquidStatus(boolean isLiquid) {
        inLiquid = isLiquid;
    }
    public void changePosition(Vector2 positionNew) {
        position.set(positionNew);
    }


}