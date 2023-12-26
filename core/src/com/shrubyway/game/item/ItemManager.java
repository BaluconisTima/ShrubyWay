package com.shrubyway.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.item.Food.AgaricAct;
import com.shrubyway.game.item.Food.Food;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.effect.BerryExplosion;
import com.shrubyway.game.visibleobject.effect.FruitExplosion;

import java.lang.reflect.Constructor;

public class ItemManager {
    public static int itemNumber = 14;
    public static TextureRegion itemTexture[] = new TextureRegion[itemNumber];
    public static String itemName[] = new String[itemNumber];
    public static String itemDescription[] = new String[itemNumber];
    public static float throwingDamage[] = new float[itemNumber];
    public static Class ItemDeath[] = new Class[itemNumber];
    public static ItemActing itemActing[] = new ItemActing[itemNumber];

    public static int tableN = 10, tableM = 10;

    public static VisibleObject getItemDeath(int id, float x, float y) {
            if(ItemDeath[id] == null) {
                return null;
            }
            try {
                Constructor<?> constructor = ItemDeath[id].getDeclaredConstructor(float.class, float.class);
                return (VisibleObject) (VisibleObject) constructor.newInstance(x, y);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }

    public static void init() {
        int size = ShrubyWay.assetManager.get("ITEMS/all.png", Texture.class).getWidth() / tableN;
        int size2 = ShrubyWay.assetManager.get("ITEMS/all.png", Texture.class).getHeight() / tableM;
        for (int i = 0; i < itemNumber; i++) {
            itemTexture[i] = new TextureRegion(ShrubyWay.assetManager.get("ITEMS/all.png", Texture.class),
                    size * (i % tableN), size2 * (i / tableN), size, size2);
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

        itemName[3] = "Green Leaf";
        itemDescription[3] = "Many might consider \n" +
                "this cannibalism.";
        throwingDamage[3] = 0;
        itemActing[3] = new Food(1f, 2);

        itemName[4] = "Yellow Leaf";
        itemDescription[4] = "Many might consider \n" +
                "this cannibalism.";
        throwingDamage[4] = 0;
        itemActing[4] = new Food(1.3f, 3);

        itemName[5] = "Red Leaf";
        itemDescription[5] = "Many might consider \n" +
                "this cannibalism.";
        throwingDamage[5] = 0;
        itemActing[5] = new Food(2f, 4);

        itemName[6] = "Fly Agaric";
        itemDescription[6] = "Causes hallucinations, addiction \n" +
                "and loss of control over darkness.";
        throwingDamage[6] = 0;
        itemActing[6] = new AgaricAct(0.2f, -1f, 20f);

        itemName[7] = "Harmonica";
        itemDescription[7] = "That old harmonica makes \n" +
                "you feel strangely sad. [MUSIC]";
        throwingDamage[7] = 0;
        itemActing[7] = new Harmonica();

        itemName[8] = "Explerry";
        itemDescription[8] = "Few people know that you can eat \n" +
                "an explerry in your lifetime, but only once.";
        ItemDeath[8] = BerryExplosion.class;
        throwingDamage[8] = 0.1f;

        itemActing[8] = new Food(10, -100f);

        itemName[9] = "Pine seed";
        itemDescription[9] = "A pine seed with a pointy end.";
        throwingDamage[9] = 4;
        itemActing[9] = null;

        itemName[10] = "Ancient Explruit";
        itemDescription[10] = "The hard rind of this fruit \n" +
                "should not give you too strong \na sense of security.";
        throwingDamage[10] = 1;
        ItemDeath[10] = FruitExplosion.class;
        itemActing[10] = new Food(10, -100f);

        itemName[11] = "Shard of the Artifact of Light";
        itemDescription[11] = "Only by assembling an artifact from \n" +
                              "them at the altar is it possible to \n" +
                               "manifest and defeat the darkness.";
        throwingDamage[11] = 0;
        itemActing[11] = null;

        itemName[12] = "Artifact of Light";
        itemDescription[12] = "You cannot even realize the power and strength \n" +
                " of the light of this artifact.";
        throwingDamage[12] = 0;
        itemActing[12] = null;

        itemName[13] = "Stale bread";
        itemDescription[13] = "Better not even think about how old it is.";
        throwingDamage[13] = 1.5f;
        itemActing[13] = new Food(1f, 5);
    }


    public static Item newItem(int id) {
        return new Item(id);
    }
}
