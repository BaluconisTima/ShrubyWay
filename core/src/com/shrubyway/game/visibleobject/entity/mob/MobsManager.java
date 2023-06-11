package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MobsManager {
    public static int mobsNumber = 2;
    public static Constructor<? extends Mob> mobs[] = new Constructor[mobsNumber];
    private static float mobSpawnCost[] = new float[mobsNumber];
    public static ArrayList<Integer> dropTableItem[] = new ArrayList[mobsNumber];
    public static ArrayList<Float> dropTableChance[] = new ArrayList[mobsNumber];

    public static void init() {
        try {
        mobs[0] = (Agaric.class).getDeclaredConstructor(float.class, float.class);
        mobs[0].newInstance(0, 0);
        dropTableItem[0] = new ArrayList<>(Arrays.asList(1, 0));
        dropTableChance[0] = new ArrayList<>(Arrays.asList(0.5f, 0.1f));
        mobSpawnCost[0] = 10;

        mobs[1] = (Coney.class).getDeclaredConstructor(float.class, float.class);
        mobs[1].newInstance(0, 0);
        dropTableItem[1] = new ArrayList<>(Arrays.asList(2));
        dropTableChance[1] = new ArrayList<>(Arrays.asList(0.7f));
        mobSpawnCost[1] = 10;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Mob newOf(int i, float x, float y){
        try {
            return mobs[i].newInstance(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void makeDrop(int i, float x, float y) {
        for(int j = 0; j < dropTableItem[i].size(); j++) {
            float prob = 1;
            while(Math.random() < prob * dropTableChance[i].get(j)) {
                prob *= dropTableChance[i].get(j);
                ObjectsList.add(new VisibleItem(new Item(dropTableItem[i].get(j)), x, y + 5));
            }
        }
    }


    private static float AccountSpawner = 0;

    public static void playerAddUpdate(float delta) {
        AccountSpawner += delta;
    }


    static Random random = new Random();

    static void addMobNear(Vector2 playerPosition, int j) {
        float x = playerPosition.x;
        float y = playerPosition.y;
        if(Math.random() < 0.5) {
           if(Math.random() < 0.5) x -= MapSettings.visibleDistanceX * MapSettings.TYLESIZE + 10;
           else x += MapSettings.visibleDistanceX * MapSettings.TYLESIZE + 10;
           y += (float) (Math.random() * 2 * MapSettings.visibleDistanceY  * MapSettings.TYLESIZE)
                   - MapSettings.visibleDistanceY * MapSettings.TYLESIZE;

        } else {
            if(Math.random() < 0.5) y -= MapSettings.visibleDistanceY * MapSettings.TYLESIZE + 10;
            else y += MapSettings.visibleDistanceY * MapSettings.TYLESIZE + 10;
            x += (float) (Math.random() * 2 * MapSettings.visibleDistanceX * MapSettings.TYLESIZE)
                    - MapSettings.visibleDistanceX  * MapSettings.TYLESIZE;
        }

        Mob mob = newOf(j, x, y);
        ObjectsList.add(mob);
    }
    static public void tryGenerateMob(Vector2 playerPosition) {
        for(int i = 0; i < mobsNumber; i++) {
            int j = random.nextInt(mobsNumber);
            if(AccountSpawner >= mobSpawnCost[j]) {
                AccountSpawner -= mobSpawnCost[j];
                addMobNear(playerPosition, j);

            }
        }
    }



}
