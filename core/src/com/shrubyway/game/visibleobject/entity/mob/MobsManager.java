package com.shrubyway.game.visibleobject.entity.mob;

import com.shrubyway.game.visibleobject.decoration.Bush;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.Pine;
import com.shrubyway.game.visibleobject.decoration.Rock;

import java.lang.reflect.Constructor;

public class MobsManager {
    public static int mobsNumber = 1;
    public static Constructor<? extends Mob> mobs[] = new Constructor[mobsNumber];
    private static float mobSpawnCost[] = new float[mobsNumber];

    public static void init() {
        try {
        mobs[0] = (Agaric.class).getDeclaredConstructor(float.class, float.class);
        mobs[0].newInstance(0, 0);
        mobSpawnCost[0] = 10;
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
}
