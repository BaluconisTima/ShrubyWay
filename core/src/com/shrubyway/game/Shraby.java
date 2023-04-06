package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.lang.Math;

public class Shraby {
    private final Vector2 position = new Vector2();

    private final boolean DebugMode = true;
    private byte FaceDirection = 0;  // 0 - DOWN, 1 - UP, 2 - LEFT, 3 - RIGHT
    private boolean IsMoving = false;
    private byte lastFaceDirection = 0;
    private boolean lastIsMoving = false;
    private boolean inLiquid = false;
    private String last_animation;

    private final int Speed = 10;
    Animation<TextureRegion> CurrentAnimation;
    Animation<TextureRegion> CurrentAnimation_inLiquid;
    float AnimationStateTime;

    private Texture AnimationList = new Texture(Gdx.files.internal("SHRABY/AFK/DOWN.png"));
    Animator animator = new Animator();
    float frameY;
    TextureRegion currentFrame;
    public Shraby(float x, float y) {
      position.set(x, y);

        CurrentAnimation = animator.toAnimation(AnimationList, 30, 0,0);
        CurrentAnimation_inLiquid = animator.toAnimation(AnimationList, 30, 0, 50);
        AnimationStateTime = 0f;
        frameY = 0;
    }

    public void render(Batch batch) {
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
               if(s != last_animation) {
                   last_animation = s;
                   AnimationList = new Texture(Gdx.files.internal(s));
                   CurrentAnimation =
                           animator.toAnimation(AnimationList, 30, 0, 0);
                   CurrentAnimation_inLiquid =
                           animator.toAnimation(AnimationList, 30, 0, 50);
                   AnimationStateTime = 0f;
                   frameY = currentFrame.getRegionHeight();
               }
           }
           AnimationStateTime += Gdx.graphics.getDeltaTime();
           currentFrame = CurrentAnimation.getKeyFrame(AnimationStateTime, true);
           TextureRegion temp = new TextureRegion(currentFrame);

           if(inLiquid) {
               temp = CurrentAnimation_inLiquid.getKeyFrame(AnimationStateTime, true);
           }
           batch.draw(temp,
                   Math.round(position.x - currentFrame.getRegionWidth() / 2),
                   Math.round(position.y - currentFrame.getRegionHeight() / 2));
    }
    public void moveTo(Vector2 direction) {
        if(direction.x == 0 && direction.y == 0) IsMoving = false;
        else IsMoving = true;
        if(direction.y < 0) FaceDirection = 0;
        else if(direction.y > 0) FaceDirection = 1;
        else if(direction.x < 0) FaceDirection = 2;
        else if(direction.x > 0) FaceDirection = 3;
        int temp = 0; temp += (Speed);
        if(inLiquid) temp *= 0.7;
        position.add(direction.scl(temp));
    }
    public Vector2 Position() {
        return position;
    }
    public Vector2 BottomPosition() {
        return new Vector2(position.x,
                position.y - frameY/2);
    }

    public void LiquidStatus(boolean isLiquid) {
        inLiquid = isLiquid;
    }
    public void changePosition(Vector2 positionNew) {
        position.set(positionNew);
    }
    public void dispose() {
      //  currentFrame.dispose();
    }


}