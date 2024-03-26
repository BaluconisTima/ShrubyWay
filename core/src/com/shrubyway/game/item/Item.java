package com.shrubyway.game.item;

public class Item implements java.io.Serializable {
    public int id;
    static ItemManager itemManager;

    public Item(int id) {
        this.id = id;
    }
}
