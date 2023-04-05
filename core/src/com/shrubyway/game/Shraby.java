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
    private byte lastFaceDirection = 0;
    private final int Speed = 10;
    Animation<TextureRegion> CurrentAnimation;
    float AnimationStateTime;
    private Texture AnimationList = new Texture(Gdx.files.internal("SHRABY_AFK_DOWN.png"));
    TextureRegion currentFrame;
    public Shraby(float x, float y) {
      position.set(x, y);
      Animator animator = new Animator();
      CurrentAnimation = animator.toAnimation(AnimationList, 1, 30);
      AnimationStateTime = 0f;


    }

    public void render(Batch batch) {
           if(FaceDirection != lastFaceDirection) {
               lastFaceDirection = FaceDirection;
               String s = "SHRABY_AFK_";
               switch(FaceDirection) {
                   case 0: s += "DOWN"; break;
                   case 1: s += "UP"; break;
                   case 2: s += "LEFT"; break;
                   case 3: s += "RIGHT"; break;
               }
               s += ".png";
               AnimationList = new Texture(Gdx.files.internal(s));
               Animator animator = new Animator();
               CurrentAnimation = animator.toAnimation(AnimationList, 1, 30);
               AnimationStateTime = 0f;
           }
           AnimationStateTime += Gdx.graphics.getDeltaTime();
           currentFrame = CurrentAnimation.getKeyFrame(AnimationStateTime, true);
           batch.draw(currentFrame,
                   position.x - currentFrame.getRegionWidth() / 2,
                   position.y - currentFrame.getRegionHeight() / 2);
    }
    public void moveTo(Vector2 direction) {
        if(direction.y < 0) FaceDirection = 0;
        else if(direction.y > 0) FaceDirection = 1;
        else if(direction.x < 0) FaceDirection = 2;
        else if(direction.x > 0) FaceDirection = 3;
        position.add(direction.scl(Speed));
    }

    public void dispose() {
      //  currentFrame.dispose();
    }

}