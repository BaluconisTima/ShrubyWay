package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.map.MapSettings;

import java.util.concurrent.CopyOnWriteArrayList;

public class MiniMap {

    static public CopyOnWriteArrayList<Vector2> mobs = new CopyOnWriteArrayList<>();

    static public void render(int lvl, float px, float py) {
        if(!Event.happened("Map_Opened"))
            return;

        float x1 = px, y1 = py;

        px /= MapSettings.MAPSIZE; px *= 274;
        py /= MapSettings.MAPSIZE; py *= 274;

        GlobalBatch.render(ShrubyWay.assetManager.get("interface/Map" + lvl + ".png", Texture.class),
                25, 25);
       for(Vector2 to: mobs) {
           float alpha =
                   Math.max(((Math.abs(to.x - x1)%MapSettings.MAPSIZE))/ (float)MapSettings.calculationDistanceX,
                   Math.abs(((to.y - y1)%MapSettings.MAPSIZE))/ (float)MapSettings.calculationDistanceY);
           System.out.println(alpha);
           alpha /= MapSettings.TYLESIZE;
           alpha = 1 - (float)(alpha * alpha);

           GlobalBatch.batch.setColor(1, 1, 1, alpha);
           GlobalBatch.render(ShrubyWay.assetManager.get("interface/darkmin.png", Texture.class),
                   25 + 29 + to.x / MapSettings.MAPSIZE * 274, 25 + 45 + to.y / MapSettings.MAPSIZE * 274);
           GlobalBatch.batch.setColor(1, 1, 1, 1);
       }

        GlobalBatch.render(ShrubyWay.assetManager.get("interface/ShrubyIcon.png", Texture.class),
                25 + 29 + px, 25 + 45 + py);
        GlobalBatch.render(ShrubyWay.assetManager.get("interface/miniMapOverlay.png", Texture.class),
                25, 25);

    }
}
