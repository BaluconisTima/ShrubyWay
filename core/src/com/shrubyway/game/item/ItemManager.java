package com.shrubyway.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.item.Food.AgaricAct;
import com.shrubyway.game.item.Food.Food;
import com.shrubyway.game.item.Food.FriedMushroomsAct;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.effect.BerryExplosion;
import com.shrubyway.game.visibleobject.effect.FruitExplosion;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ItemManager {
    public static int itemNumber = 32;
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
        throwingDamage[13] = 0;
        itemActing[13] = new Food(1f, 5);

        itemName[14] = "Ancient Windruit";
        itemDescription[14] = "Pleasantly cools your hand \n" +
                "while you're holding it.";
        throwingDamage[14] = 0;

        itemName[15] = "Berry muffin";
        itemDescription[15] = "The result of baking dough with \n" +
                "explerry is incredible flavor. Or an explosion. \n" +
                "One of the two.";
        throwingDamage[15] = 0;
        itemActing[15] = new Food(1.5f, 7);

        itemName[16] = "Fried mushrooms";
        itemDescription[16] = "Instructions: \n" +
                "              1) Eat the mushrooms.\n" +
                "              2) Get a stick.\n" +
                "              3) Do not eat the stick.";
        throwingDamage[16] = 0;
        itemActing[16] = new FriedMushroomsAct(3f, 10);

        itemName[17] = "Leaf sandwich";
        itemDescription[17] = "No one will know what's in the sandwich \n" +
                "if you eat it carefully.";
        throwingDamage[17] = 0;
        itemActing[17] = new Food(3f, 15);

        itemName[18] = "Pointed log";
        itemDescription[18] = "The only thing that does more damage \n" +
                "is what it was sharpened with.";
        throwingDamage[18] = 10;
        itemActing[18] = null;

        itemName[19] = "Coniferous branch";
        itemDescription[19] = "Take damage from the stick, get damage \n" +
                "from the needles as a bonus!";
        throwingDamage[19] = 5;
        itemActing[19] = null;

        itemName[20] = "Sharpened pebble";
        itemDescription[20] = "Cons: Uncomfortable in the hand. \n" +
                "Pros: Uncomfortable in the hand.";
        throwingDamage[20] = 2;

        itemName[21] = "Empty flask";
        itemDescription[21] = "If you're an optimist, this \n flask is filled with air. ";
        throwingDamage[21] = 2;
        itemActing[21] = null;

        itemName[22] = "Flask of water";
        itemDescription[22] = "Would you like a drink of water? \n" +
                "No? That's fine.";
        throwingDamage[22] = 2;
        itemActing[22] = null; //TODO
        ItemDeath[22] = null; //TODO

        itemName[23] = "Flask of poison";
        itemDescription[23] = "Better to throw than to drink.";
        throwingDamage[23] = 2;
        itemActing[23] = null; //TODO
        ItemDeath[23] = null; //TODO

        itemName[24] = "Flask of invincibility potion";
        itemDescription[24] = "Better to drink than to throw.";
        throwingDamage[24] = 2;
        itemActing[24] = null; //TODO
        ItemDeath[24] = null; //TODO

        itemName[25] = "Flask of slowness potion";
        itemDescription[25] = "I regret to inform you that drinking this liquid, \n" +
                               "which is in a glass vessel, will cause your speed \n" +
                                "to change for the worse for a while, after which your\n" +
                                "speed will be restored.  However, using it as part \n" +
                                "of a throwing weapon can lead to beneficial results,\n" +
                                "as the effect is not limited to you.";
        throwingDamage[25] = 2;
        itemActing[25] = null; //PotionAct();
        ItemDeath[25] = null; //TODO

        itemName[26] = "Flask of speed potion";
        itemDescription[26] = "Drink. Fast.";
        throwingDamage[26] = 2;
        itemActing[26] = null; //TODO
        ItemDeath[26] = null; //TODO

        itemName[27] = "Bronze coin";
        itemDescription[27] = "If you see this message, something went wrong.";

        itemName[28] = "Silver coin";
        itemDescription[28] = "If you see this message, something went wrong.";

        itemName[29] = "Gold coin";
        itemDescription[29] = "If you see this message, something went wrong.";

        itemName[30] = "Bag of coins";
        itemDescription[30] = "If you see this message, something went wrong.";

        itemName[31] = "Drawings on the bark";
        itemDescription[31] = "u smell like a cheater.";







    }

    static public void addMoney(int id) {
        switch (id) {
            case 27:
                Game.player.money += 1;
                break;
            case 28:
                Game.player.money += 5;
                break;
            case 29:
                Game.player.money += 25;
                break;
            case 30:
                Game.player.money += 100;
                break;
        }
    }

    static public void placeMoney(long amount, float x, float y) {
        while(amount >= 100) {
            Game.objectsList.add(new VisibleItem(new Item(30), x, y));
            amount -= 100;
        }
        while(amount >= 25) {
            Game.objectsList.add(new VisibleItem(new Item(29), x, y));
            amount -= 25;
        }
        while(amount >= 5) {
            Game.objectsList.add(new VisibleItem(new Item(28), x, y));
            amount -= 5;
        }
        while(amount >= 1) {
            Game.objectsList.add(new VisibleItem(new Item(27), x, y));
            amount -= 1;
        }
    }

    static public ArrayList<Item> splitMoney(long amount) {
        ArrayList<Item> items = new ArrayList<Item>();
        while(amount >= 100) {
            items.add(new Item(30));
            amount -= 100;
        }
        while(amount >= 25) {
            items.add(new Item(29));
            amount -= 25;
        }
        while(amount >= 5) {
            items.add(new Item(28));
            amount -= 5;
        }
        while(amount >= 1) {
            items.add(new Item(27));
            amount -= 1;
        }
        return items;
    }

    static public void placeMoney(long amount, Vector2 place) {
        while(amount >= 100) {
            Game.objectsList.add(new VisibleItem(new Item(30), place));
            amount -= 100;
        }
        while(amount >= 25) {
            Game.objectsList.add(new VisibleItem(new Item(29), place));
            amount -= 25;
        }
        while(amount >= 5) {
            Game.objectsList.add(new VisibleItem(new Item(28), place));
            amount -= 5;
        }
        while(amount >= 1) {
            Game.objectsList.add(new VisibleItem(new Item(27), place));
            amount -= 1;
        }
    }

    static public void placeMoney(long amount, float x, float y, Vector2 place) {
        while(amount >= 100) {
            Game.objectsList.add(new VisibleItem(new Item(30), x, y, place));
            amount -= 100;
        }
        while(amount >= 25) {
            Game.objectsList.add(new VisibleItem(new Item(29), x, y, place));
            amount -= 25;
        }
        while(amount >= 5) {
            Game.objectsList.add(new VisibleItem(new Item(28), x, y, place));
            amount -= 5;
        }
        while(amount >= 1) {
            Game.objectsList.add(new VisibleItem(new Item(27), x, y, place));
            amount -= 1;
        }
    }

    static public boolean checkIfSpecial(int id) {
       return (id >= 27 && id <= 30);
    }


    public static Item newItem(int id) {
        return new Item(id);
    }
}
