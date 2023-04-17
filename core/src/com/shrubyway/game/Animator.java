package com.shrubyway.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
    public Animator(){

    }
     Animation<TextureRegion> toAnimation(Texture texture, int n, int szN, int szM) {
        TextureRegion[] animationFrames = new TextureRegion[n];
        int textureWidth = texture.getWidth() / n, textureHeight = texture.getHeight();
        int index = 0;
        for (int i = 0; i < n; i++) {
            animationFrames[index++] =
                        new TextureRegion(texture, i * (textureWidth), 0,
                        (textureWidth) - szN, textureHeight - szM);
        }
        return new Animation<>(1/24f, animationFrames);
    }
}
