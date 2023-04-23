package com.shrubyway.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
public class ItemManager {
    public static int itemNumber = 3;
    public static Texture itemTexture[] = new Texture[itemNumber];
    public static String itemName[] = new String[itemNumber];
    public static String itemDescription[] = new String[itemNumber];
    public static float throwingDamage[] = new float[itemNumber];
    public static boolean Interacting[] = new boolean[itemNumber];

    public static void init() {
        for(int i = 0; i < itemNumber; i++) {
            itemTexture[i] = new Texture("Items/" + i +".png");
            itemTexture[i].setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        itemName[0] = "Stone";
        itemDescription[0] = "A small stone.";
        throwingDamage[0] = 5;
        Interacting[0] = false;

        itemName[1] = "Stick";
        itemDescription[1] = "A well-thrown stick.";
        throwingDamage[1] = 3;
        Interacting[1] = false;

        itemName[2] = "Pine cone";
        itemDescription[2] = "How easy it is to find,\njust as easy to lose.";
        throwingDamage[2] = 1;
        Interacting[2] = false;
    }
    public static Item newItem(int id) {
        return new Item(id);
    }
}
