package com.shrubyway.game.saver;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class VisualObjectListSaver implements java.io.Serializable {
    ArrayList mobsID, mobsPosition, mobsHealth;
    ArrayList itemsID, itemsPosition;
    ArrayList decorationsID, decorationsPosition, decorationsIJ;


    public VisualObjectListSaver(Set<VisibleObject> list)  {
        mobsID = new ArrayList<Integer>();
        mobsPosition = new ArrayList<Vector2>();
        mobsHealth = new ArrayList<Health>();
        itemsID = new ArrayList<Integer>();
        itemsPosition = new ArrayList<Vector2>();
        decorationsID = new ArrayList<Integer>();
        decorationsPosition = new ArrayList<Vector2>();
        decorationsIJ = new ArrayList<Vector2>();

        for (VisibleObject object : list) {
            if(object instanceof Mob mob) {
                mobsID.add(mob.id);
                mobsPosition.add(mob.position);
                mobsHealth.add(mob.health);
            }
            else if(object instanceof VisibleItem item) {
                itemsID.add(item.item.id);
                itemsPosition.add(item.position);
            }
            else if(object instanceof Decoration decoration){
                decorationsID.add(decoration.id);
                decorationsPosition.add(decoration.position);
                decorationsIJ.add(new Vector2(decoration.decorationI, decoration.decorationJ));
            }
        }
    }

    public VisualObjectListSaver(List<VisibleObject> list) {
        mobsID = new ArrayList<Integer>();
        mobsPosition = new ArrayList<Vector2>();
        mobsHealth = new ArrayList<Health>();
        itemsID = new ArrayList<Integer>();
        itemsPosition = new ArrayList<Vector2>();
        decorationsID = new ArrayList<Integer>();
        decorationsPosition = new ArrayList<Vector2>();
        decorationsIJ = new ArrayList<Vector2>();

        for (VisibleObject object : list) {
            if(object instanceof Mob mob) {
                mobsID.add(mob.id);
                mobsPosition.add(mob.position);
                mobsHealth.add(mob.health);
            }
            else if(object instanceof VisibleItem item) {
                itemsID.add(item.item.id);
                itemsPosition.add(item.position);
            }
            else if(object instanceof Decoration decoration){
                decorationsID.add(decoration.id);
                decorationsPosition.add(decoration.position);
                decorationsIJ.add(new Vector2(decoration.decorationI, decoration.decorationJ));
            }
        }
    }
    public CopyOnWriteArrayList<VisibleObject> getList() {
        CopyOnWriteArrayList<VisibleObject> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < mobsID.size(); i++) {
            Mob mob = MobsManager.newOf((Integer) mobsID.get(i), ((Vector2) mobsPosition.get(i)).x, ((Vector2) mobsPosition.get(i)).y);
            mob.health.setHealth((Health) mobsHealth.get(i));
            mob.position.set((Vector2) mobsPosition.get(i));
            list.add(mob);
        }
        for (int i = 0; i < itemsID.size(); i++) {
            VisibleItem item = new VisibleItem(new Item((Integer) itemsID.get(i)),
                    ((Vector2) itemsPosition.get(i)).x, ((Vector2) itemsPosition.get(i)).y);
            item.position.set((Vector2) itemsPosition.get(i));
            list.add(item);
        }
        for (int i = 0; i < decorationsID.size(); i++) {
            Decoration decoration = DecorationsManager.newOf((Integer) decorationsID.get(i));
                    decoration.change(((Vector2) decorationsPosition.get(i)).x, ((Vector2) decorationsPosition.get(i)).y,
                            (int) ((Vector2) decorationsIJ.get(i)).x, (int) ((Vector2) decorationsIJ.get(i)).y);
            decoration.position.set((Vector2) decorationsPosition.get(i));
            list.add(decoration);
        }
        return list;
    }
}
