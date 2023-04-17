package com.shrubyway.game;

import com.badlogic.gdx.graphics.Texture;

public class Item extends VisibleObject{
    static Texture texture[] = new Texture[3];
    static String itemName[] = {"Stone", "Stick", "Pine cone"};
            int id = 0;
    public Item(int x, int y, int itemID) {
        position.x = x;
        position.y = y;
        id = itemID;
        if(texture[id] == null) {
            texture[id] = new Texture("Items/" + id + ".png");
        }
    }
}
