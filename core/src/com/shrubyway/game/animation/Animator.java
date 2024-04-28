package com.shrubyway.game.animation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.ShrubyWay;

public class Animator {
    public Animator(){

    }
     static public Animation<TextureRegion> toAnimation(Texture texture, int n, int szN, int szM) {
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
    static public Animation<TextureRegion> toAnimation(Texture texture, int n, int szN, int szM, float frameDuration) {
        TextureRegion[] animationFrames = new TextureRegion[n];
        int textureWidth = texture.getWidth() / n, textureHeight = texture.getHeight();
        int index = 0;
        for (int i = 0; i < n; i++) {
            animationFrames[index++] =
                    new TextureRegion(texture, i * (textureWidth), 0,
                            (textureWidth) - szN, textureHeight - szM);
        }
        return new Animation<>(frameDuration, animationFrames);
    }

    static public Animation<TextureRegion> toAnimation(String path, int n) {
        TextureRegion[] animationFrames = new TextureRegion[n];
        for(int i = 0; i < n; i++) {
            String name = path + "frame" + String.format("%04d", i + 1) + ".png";
            Texture texture = ShrubyWay.assetManager.get(name, Texture.class);
            animationFrames[i] = new TextureRegion(texture);
        }
        return new Animation<>(1/24f, animationFrames);
    }
}
