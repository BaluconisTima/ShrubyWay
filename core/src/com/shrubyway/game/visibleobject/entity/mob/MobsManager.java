package com.shrubyway.game.visibleobject.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MobsManager {
    public static int mobsNumber = 8;
    public static Constructor<? extends Mob> mobs[] = new Constructor[mobsNumber];
    public static float mobSpawnCost[] = new float[mobsNumber];
    public static Integer mobExp[] = new Integer[mobsNumber];
    public static ArrayList<Integer> dropTableItem[] = new ArrayList[mobsNumber];
    public static ArrayList<Float> dropTableChance[] = new ArrayList[mobsNumber];

    public static ArrayList<Integer> spawnTileTypes[] = new ArrayList[mobsNumber];

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
        spawnTileTypes[0] = new ArrayList<>(Arrays.asList(1, 3, 4, 5, 6, 9));

        mobs[1] = (Coney.class).getDeclaredConstructor(float.class, float.class);
        mobs[1].newInstance(0, 0);
        dropTableItem[1] = new ArrayList<>(Arrays.asList(2, 9));
        dropTableChance[1] = new ArrayList<>(Arrays.asList(0.6f, 0.3f));
        mobSpawnCost[1] = 14;
        mobExp[1] = 150;
        spawnTileTypes[1] = new ArrayList<>(Arrays.asList(1, 3, 7, 9));

        mobs[2] = (Explerry.class).getDeclaredConstructor(float.class, float.class);
        mobs[2].newInstance(0, 0);
        dropTableItem[2] = new ArrayList<>();
        dropTableChance[2] = new ArrayList<>();
        mobSpawnCost[2] = 0;
        mobExp[2] = 0;
        spawnTileTypes[2] = new ArrayList<>();


        mobs[3] = (ExplerryBusher.class).getDeclaredConstructor(float.class, float.class);
        mobs[3].newInstance(0, 0);
        dropTableItem[3] = new ArrayList<>(Arrays.asList(8, 8, 1));
        dropTableChance[3] = new ArrayList<>(Arrays.asList(0.4f, 0.1f, 0.2f));
        mobSpawnCost[3] = 17;
        mobExp[3] = 250;
        spawnTileTypes[3] = new ArrayList<>(Arrays.asList(1, 3, 7, 9));

        mobs[4] = (YellowLeafer.class).getDeclaredConstructor(float.class, float.class);
        mobs[4].newInstance(0, 0);
        dropTableItem[4] = new ArrayList<>(Arrays.asList(4, 4));
        dropTableChance[4] = new ArrayList<>(Arrays.asList(0.6f, 0.1f));
        mobSpawnCost[4] = 9;
        mobExp[4] = 50;
        spawnTileTypes[4] = new ArrayList<>(Arrays.asList(4));

        mobs[5] = (RedLeafer.class).getDeclaredConstructor(float.class, float.class);
        mobs[5].newInstance(0, 0);
        dropTableItem[5] = new ArrayList<>(Arrays.asList(5, 5));
        dropTableChance[5] = new ArrayList<>(Arrays.asList(0.6f, 0.1f));
        mobSpawnCost[5] = 9;
        mobExp[5] = 50;
        spawnTileTypes[5] = new ArrayList<>(Arrays.asList(5));

        mobs[6] = (GreenLeafer.class).getDeclaredConstructor(float.class, float.class);
        mobs[6].newInstance(0, 0);
        dropTableItem[6] = new ArrayList<>(Arrays.asList(3, 3));
        dropTableChance[6] = new ArrayList<>(Arrays.asList(0.6f, 0.1f));
        mobSpawnCost[6] = 9;
        mobExp[6] = 50;
        spawnTileTypes[6] = new ArrayList<>(Arrays.asList(6));

        mobs[7] = (Boulder.class).getDeclaredConstructor(float.class, float.class);
        mobs[7].newInstance(0, 0);
        dropTableItem[7] = new ArrayList<>(Arrays.asList(0, 0));
        dropTableChance[7] = new ArrayList<>(Arrays.asList(0.9f, 0.1f));
        mobSpawnCost[7] = 15;
        mobExp[7] = 250;
        spawnTileTypes[7] = new ArrayList<>(Arrays.asList(1, 3, 7, 9));

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

    public static int MobDeathCounter = 0;

    public static void makeDrop(int i, float x, float y) {
        for(int j = 0; j < dropTableItem[i].size(); j++) {
            float prob = 1;
            while(Math.random() < prob * dropTableChance[i].get(j)) {
                prob *= dropTableChance[i].get(j);
                Game.objectsList.add(new VisibleItem(new Item(dropTableItem[i].get(j)), x, y + 5));
            }
        }
    }

    static Random random = new Random();

    static public void addMob(Vector2 position, int mobID) {
        Mob mob = newOf(mobID, position.x, position.y);
        if(mob != null) Game.objectsList.add(mob);
    }

    static float tileStats[] = new float[MapSettings.TILETYPES];
    static ArrayList mobsToChoose = new ArrayList<Integer>();
    static ArrayList positions = new ArrayList<Vector2>();

    static public void generateMobsForChunk(int chunkI, int chunkJ, int cost) {
        for(int i = 0; i < MapSettings.TILETYPES; i++) {
            tileStats[i] = 0;
        }
        for(int i = 0; i < MapSettings.CHUNKSIZE; i++)
            for(int j = 0; j < MapSettings.CHUNKSIZE; j++) {
                int tileType = Game.map.background.backgroundMap[chunkI * MapSettings.CHUNKSIZE + i][chunkJ * MapSettings.CHUNKSIZE + j] - '0';
                tileStats[tileType] += 1;
            }

        for(int i = 0; i < MapSettings.TILETYPES; i++) {
            tileStats[i] /= MapSettings.CHUNKSIZE * MapSettings.CHUNKSIZE;
        }

        mobsToChoose.clear();
        positions.clear();

        int timesToTry = 32;
        for(int attempt = 0; attempt < timesToTry; attempt++) {
            int tyle = -1;
            float randFloat = random.nextFloat();
            for(int i = 0; i < MapSettings.TILETYPES; i++) {
                randFloat -= tileStats[i];
                if(randFloat < 0) {
                    tyle = i;
                    break;
                }
            }
            for(int i = 0; i < mobsNumber; i++) {
                if(spawnTileTypes[i].contains(tyle)) {
                    mobsToChoose.add(i);
                    positions.add(new Vector2((chunkI * MapSettings.CHUNKSIZE + random.nextInt(MapSettings.CHUNKSIZE)) * MapSettings.TYLESIZE,
                            (chunkJ * MapSettings.CHUNKSIZE + random.nextInt(MapSettings.CHUNKSIZE)) * MapSettings.TYLESIZE));
                }
            }
        }
        if(mobsToChoose.size() == 0) return;
        int failedAttempts = 0, maxFailedAttempts = 5;
        while(cost > 0 && failedAttempts < maxFailedAttempts) {
            int pos = random.nextInt(mobsToChoose.size());
            int mobID = (int) mobsToChoose.get(pos);
            if(mobSpawnCost[mobID] <= cost) {
                cost -= mobSpawnCost[mobID];
                addMob((Vector2)positions.get(pos), mobID);
                failedAttempts = 0;
            } else {
                failedAttempts++;
            }
        }
    }

    static public void addMobNearWithoutLimits(Vector2 playerPosition, int j) {
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
        mob.noticeDistance = 2500;
        mob.loseInterestDistance = 3000;
        if(mob != null) Game.objectsList.add(mob);
    }



}
