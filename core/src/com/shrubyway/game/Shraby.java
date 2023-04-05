package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
public class Shraby {
    private final Vector2 position = new Vector2();
    private byte FaceDirection = 0;  // 0 - DOWN, 1 - UP, 2 - LEFT, 3 - RIGHT
    private boolean IsMoving = false;
    private byte lastFaceDirection = 0;
    private boolean lastIsMoving = false;

    private final int Speed = 7;
    Animation<TextureRegion> CurrentAnimation;
    float AnimationStateTime;

    private Texture AnimationList = new Texture(Gdx.files.internal("SHRABY/AFK/DOWN.png"));
    Animator animator = new Animator();
    TextureRegion currentFrame;
    public Shraby(float x, float y) {
      position.set(x, y);
        AnimationStateTime = 0f;
        CurrentAnimation = animator.toAnimation(AnimationList, 30);
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
               AnimationList = new Texture(Gdx.files.internal(s));
               AnimationStateTime = 0f;
               CurrentAnimation = animator.toAnimation(AnimationList, 30);

           }
           AnimationStateTime += Gdx.graphics.getDeltaTime();
           currentFrame = CurrentAnimation.getKeyFrame(AnimationStateTime, true);
           batch.draw(currentFrame,
                   position.x - currentFrame.getRegionWidth() / 2,
                   position.y - currentFrame.getRegionHeight() / 2);
    }
    public void moveTo(Vector2 direction) {
        if(direction.x == 0 && direction.y == 0) IsMoving = false;
        else IsMoving = true;
        if(direction.y < 0) FaceDirection = 0;
        else if(direction.y > 0) FaceDirection = 1;
        else if(direction.x < 0) FaceDirection = 2;
        else if(direction.x > 0) FaceDirection = 3;
        position.add(direction.scl(Speed));
    }
    public Vector2 Position() {
        return position;
    }

    public void dispose() {
      //  currentFrame.dispose();
    }

}