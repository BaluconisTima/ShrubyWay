package com.shrubyway.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
public class Animator {
    public Animator(){

    }
    public Animation<TextureRegion> toAnimation(Texture texture, int n) {
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        TextureRegion[] animation_frames = new TextureRegion[n];
        int index = 0;
        for (int i = 0; i < n; i++) {
                animation_frames[index++] = new TextureRegion(texture, i * (texture.getWidth() / n), 0,
                        (texture.getWidth() / n), texture.getHeight());

        }
        return new Animation<TextureRegion>(1/24f, animation_frames);
    }
}
