package com.shrubyway.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ItemManager {
    public static int itemNumber = 5;
    public static TextureRegion itemTexture[] = new TextureRegion[itemNumber];
    public static String itemName[] = new String[itemNumber];
    public static String itemDescription[] = new String[itemNumber];
    public static float throwingDamage[] = new float[itemNumber];
    public static ItemActing itemActing[] = new ItemActing[itemNumber];

    public static void init() {
        for (int i = 0; i < itemNumber; i++) {
            itemTexture[i] = new TextureRegion(new Texture("ITEMS/" + i + ".png"));
        }
        itemName[0] = "Stone";
        itemDescription[0] = "A small stone.";
        throwingDamage[0] = 5;
        itemActing[0] = null;

        itemName[1] = "Stick";
        itemDescription[1] = "A well-thrown stick.";
        throwingDamage[1] = 3;
        itemActing[1] = null;

        itemName[2] = "Pine cone";
        itemDescription[2] = "How easy it is to find,\njust as easy to lose.";
        throwingDamage[2] = 1;
        itemActing[2] = null;

        itemName[3] = "Bush Leaf";
        itemDescription[3] = "Many might consider \n" +
                "this cannibalism. [FOOD]";
        throwingDamage[3] = 0;
        itemActing[3] = new Food(1f, 2);

        itemName[4] = "Harmonica";
        itemDescription[4] = "That old harmonica makes \n" +
                "you feel strangely sad. [MUSIC]";
        throwingDamage[4] = 0;
        itemActing[4] = new Harmonica();
    }


    public static Item newItem(int id) {
        return new Item(id);
    }
}
