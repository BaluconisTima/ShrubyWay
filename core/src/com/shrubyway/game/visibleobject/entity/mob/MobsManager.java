package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MobsManager {
    public static int mobsNumber = 5;
    public static Constructor<? extends Mob> mobs[] = new Constructor[mobsNumber];
    private static float mobSpawnCost[] = new float[mobsNumber];
    public static Integer mobExp[] = new Integer[mobsNumber];
    public static ArrayList<Integer> dropTableItem[] = new ArrayList[mobsNumber];
    public static ArrayList<Float> dropTableChance[] = new ArrayList[mobsNumber];


    static public Integer getExp(Mob mob) {
        return mobExp[mob.id];
    }

    public static void init() {
        try {
        mobs[0] = (Agaric.class).getDeclaredConstructor(float.class, float.class);
        mobs[0].newInstance(0, 0);
        dropTableItem[0] = new ArrayList<>(Arrays.asList(6));
        dropTableChance[0] = new ArrayList<>(Arrays.asList(0.6f));
        mobSpawnCost[0] = 10;
        mobExp[0] = 100;

        mobs[1] = (Coney.class).getDeclaredConstructor(float.class, float.class);
        mobs[1].newInstance(0, 0);
        dropTableItem[1] = new ArrayList<>(Arrays.asList(2, 9));
        dropTableChance[1] = new ArrayList<>(Arrays.asList(0.6f, 0.3f));
        mobSpawnCost[1] = 10;
        mobExp[1] = 150;

        mobs[2] = (Explerry.class).getDeclaredConstructor(float.class, float.class);
        mobs[2].newInstance(0, 0);
        dropTableItem[2] = new ArrayList<>();
        dropTableChance[2] = new ArrayList<>();
        mobSpawnCost[2] = 1;
        mobExp[2] = 0;

        mobs[3] = (ExplerryBusher.class).getDeclaredConstructor(float.class, float.class);
        mobs[3].newInstance(0, 0);
        dropTableItem[3] = new ArrayList<>(Arrays.asList(8, 8, 1));
        dropTableChance[3] = new ArrayList<>(Arrays.asList(0.4f, 0.1f, 0.2f));
        mobSpawnCost[3] = 10;
        mobExp[3] = 250;

        mobs[4] = (YellowLeafer.class).getDeclaredConstructor(float.class, float.class);
        mobs[4].newInstance(0, 0);
        dropTableItem[4] = new ArrayList<>(Arrays.asList(4, 4));
        dropTableChance[4] = new ArrayList<>(Arrays.asList(0.6f, 0.1f));
        mobSpawnCost[4] = 10;
        mobExp[4] = 50;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Mob newOf(int i, float x, float y){
        if(MobCount == 1 && !Event.happened("Forest_Tutorial_More_Mobs")) return null;
        MobCount++;
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
                Game.objectsList.add(new VisibleItem(new Item(dropTableItem[i].get(j)), x, y + 5));
            }
        }
    }


    private static float AccountSpawner = 0;

    public static void playerAddUpdate(float delta) {
        AccountSpawner += delta;
    }


    static Random random = new Random();

    static public int MobCount = 0;

    static public void addMobNear(Vector2 playerPosition, int j) {
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
        if(mob != null) Game.objectsList.add(mob);
    }
    static public void tryGenerateMob(Vector2 playerPosition) {
        int allCount = 0;
        for(int i = 0; i < mobsNumber; i++) {
            allCount += mobSpawnCost[i];
        }
        allCount = (int) (allCount * Math.random());

        for(int i = 0; i < mobsNumber; i++) {
            if(allCount < mobSpawnCost[i]) {
                if(AccountSpawner >= mobSpawnCost[i]) {
                    AccountSpawner -= mobSpawnCost[i];
                    addMobNear(playerPosition, i);
                }
                return;
            }
           allCount -= mobSpawnCost[i];
        }
    }



}
