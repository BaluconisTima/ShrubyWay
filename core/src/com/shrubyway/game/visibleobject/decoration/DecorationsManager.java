package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.Animator;
public class DecorationsManager {
    public static int decorationNumber = 4;
    public static Class<? extends Decoration> decorations[] = new Class[decorationNumber];
    static Animation<TextureRegion> texture[] = new Animation[decorationNumber];
    static String way[] = new String[decorationNumber];
    static Animator animator = new Animator();



    public static void init() {
        for(int i = 0; i < decorationNumber; i++) {
            way[i] = "Decorations/";
        }
        decorations[0] = Bush.class;
        way[0] += "BUSH.png";
        texture[0] = animator.toAnimation(GlobalAssetManager.get(way[0], Texture.class), 8, 0, 0);

        decorations[1] = Pine.class;
        way[1] += "PINE.png";
        texture[1] = animator.toAnimation( GlobalAssetManager.get(way[1], Texture.class), 8, 0, 0);

        decorations[2] = Rock.class;
        way[2] += "ROCK.png";
        texture[2] = animator.toAnimation( GlobalAssetManager.get(way[2], Texture.class), 8, 0, 0);

        decorations[3] = Willow.class;
        way[3] += "WILLOW.png";
        texture[3] = animator.toAnimation( GlobalAssetManager.get(way[3], Texture.class), 8, 0, 0);
    }

    public static Decoration newOf(int i){
        try {
            return decorations[i].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
