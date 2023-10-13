package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.Animator;
public class DecorationsManager {
    public static int decorationNumber = 21;
    public static Class<? extends Decoration> decorations[] = new Class[decorationNumber];
    static Animation<TextureRegion> texture[] = new Animation[decorationNumber];
    static String way[] = new String[decorationNumber];
    static Animator animator = new Animator();



    public static void init() {
        for(int i = 0; i < decorationNumber; i++) {
            way[i] = "Decorations/";
        }
        decorations[0] = Save.class;
        way[0] += "SAVE.png";
        texture[0] = animator.toAnimation( GlobalAssetManager.get(way[0], Texture.class), 16, 0, 0);


        decorations[1] = Rock.class;
        way[1] += "ROCK.png";
        texture[1] = animator.toAnimation( GlobalAssetManager.get(way[1], Texture.class), 8, 0, 0);

        decorations[2] = Oak.class;
        way[2] += "OAK.png";
        texture[2] = animator.toAnimation( GlobalAssetManager.get(way[2], Texture.class), 8, 0, 0);

        decorations[3] = OakBush.class;
        way[3] += "OAK_BUSH.png";
        texture[3] = animator.toAnimation(GlobalAssetManager.get(way[3], Texture.class), 8, 0, 0);

        decorations[4] = Pine.class;
        way[4] += "PINE.png";
        texture[4] = animator.toAnimation( GlobalAssetManager.get(way[4], Texture.class), 8, 0, 0);

        decorations[5] = PineBush.class;
        way[5] += "PINE_BUSH.png";
        texture[5] = animator.toAnimation( GlobalAssetManager.get(way[5], Texture.class), 8, 0, 0);

        decorations[6] = Maple.class;
        way[6] += "MAPLE.png";
        texture[6] = animator.toAnimation( GlobalAssetManager.get(way[6], Texture.class), 8, 0, 0);

        decorations[7] = MapleBush.class;
        way[7] += "MAPLE_BUSH.png";
        texture[7] = animator.toAnimation( GlobalAssetManager.get(way[7], Texture.class), 8, 0, 0);

        decorations[8] = Birch.class;
        way[8] += "BIRCH.png";
        texture[8] = animator.toAnimation( GlobalAssetManager.get(way[8], Texture.class), 8, 0, 0);

        decorations[9] = BirchBush.class;
        way[9] += "BIRCH_BUSH.png";
        texture[9] = animator.toAnimation( GlobalAssetManager.get(way[9], Texture.class), 8, 0, 0);

        decorations[10] = Willow.class;
        way[10] += "WILLOW.png";
        texture[10] = animator.toAnimation( GlobalAssetManager.get(way[10], Texture.class), 8, 0, 0);

        decorations[11] = OakStump.class;
        way[11] += "OAK_STUMP.png";
        texture[11] = animator.toAnimation( GlobalAssetManager.get(way[11], Texture.class), 1, 0, 0);

        decorations[12] = PineStump.class;
        way[12] += "PINE_STUMP.png";
        texture[12] = animator.toAnimation( GlobalAssetManager.get(way[12], Texture.class), 1, 0, 0);

        decorations[13] = MapleStump.class;
        way[13] += "MAPLE_STUMP.png";
        texture[13] = animator.toAnimation( GlobalAssetManager.get(way[13], Texture.class), 1, 0, 0);

        decorations[14] = BirchStump.class;
        way[14] += "BIRCH_STUMP.png";
        texture[14] = animator.toAnimation( GlobalAssetManager.get(way[14], Texture.class), 1, 0, 0);

        decorations[15] = Daisy.class;
        way[15] += "DAISY.png";
        texture[15] = animator.toAnimation( GlobalAssetManager.get(way[15], Texture.class), 8, 0, 0);

        decorations[16] = Reeds.class;
        way[16] += "REEDS.png";
        texture[16] = animator.toAnimation( GlobalAssetManager.get(way[16], Texture.class), 8, 0, 0);

        decorations[17] = Fern.class;
        way[17] += "FERN.png";
        texture[17] = animator.toAnimation( GlobalAssetManager.get(way[17], Texture.class), 8, 0, 0);

        decorations[18] = Grass.class;
        way[18] += "GRASS.png";
        texture[18] = animator.toAnimation( GlobalAssetManager.get(way[18], Texture.class), 8, 0, 0);

        decorations[19] = ChestClosed.class;
        way[19] += "CHEST_CLOSED.png";
        texture[19] = animator.toAnimation( GlobalAssetManager.get(way[19], Texture.class), 8, 0, 0);

        decorations[20] = ChestOpened.class;
        way[20] += "CHEST_OPENED.png";
        texture[20] = animator.toAnimation( GlobalAssetManager.get(way[20], Texture.class), 8, 0, 0);

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
