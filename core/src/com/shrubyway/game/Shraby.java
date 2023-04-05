package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
public class Shraby {
    private final Vector2 position = new Vector2();
    private final byte FaceDirection = 0;  // 0 - DOWN, 1 - UP, 2 - LEFT, 3 - RIGHT
    private final int Speed = 10;
    Animation<TextureRegion> CurrentAnimation;
    float AnimationStateTime;
    private final Texture AFK_DOWN_texture = new Texture(Gdx.files.internal("Shraby_AFK_down.png"));
    TextureRegion currentFrame;
    public Shraby(float x, float y) {
      position.set(x, y);
      Animator animator = new Animator();
      CurrentAnimation = animator.toAnimation(AFK_DOWN_texture, 1, 30);
      //AnimationStateTime = 0f;


    }

    public void render(Batch batch) {
           AnimationStateTime += Gdx.graphics.getDeltaTime();
           currentFrame = CurrentAnimation.getKeyFrame(AnimationStateTime, true);
           batch.draw(currentFrame,
                   position.x - currentFrame.getRegionWidth() / 2,
                   position.y - currentFrame.getRegionHeight() / 2);
    }
    public void moveTo(Vector2 direction) {
        position.add(direction.scl(Speed));
    }

    public void dispose() {
      //  currentFrame.dispose();
    }

}