package com.shrubyway.game.map;

import com.shrubyway.game.Pair;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.VisibleObject;

import java.util.ArrayList;
import java.util.List;

public class ScreenGrid {

    private static final int GRID_SIZE = 1024;
    private static final int CHUNK_COUNT = MapSettings.MAPSIZE / GRID_SIZE;
    private ArrayList<VisibleObject>[][] CollisionBoxes = new ArrayList[GRID_SIZE][GRID_SIZE];
    private ArrayList<VisibleObject>[][] HitBoxes = new ArrayList[GRID_SIZE][GRID_SIZE];
    private ArrayList<Pair<Integer, Integer>> lastChecked = new ArrayList<>();

    public ScreenGrid() {
        for(int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                CollisionBoxes[i][j] = new ArrayList<>();
                HitBoxes[i][j] = new ArrayList<>();
            }
        }
    }

    private void clearGrid() {
         for(Pair<Integer, Integer> to: lastChecked) {
             CollisionBoxes[to.first][to.second].clear();
             HitBoxes[to.first][to.second].clear();
         }
    }

    public ArrayList<Pair<Integer, Integer>> overlapsChunks(Rectangle box, ArrayList<Pair<Integer, Integer>> result){
        if(box == null) return result;
        float x1 = box.topLeftCorner.x / GRID_SIZE, x2 = box.bottomRightCorner.x / GRID_SIZE,
            y1 = box.topLeftCorner.y / GRID_SIZE,  y2 = box.bottomRightCorner.y / GRID_SIZE;
        int x_left = (int) x1-1, x_right = (int) x2+1,
            y_top = (int) y1-1, y_bottom = (int) y2+1;

        for(int i = x_left; i <= x_right; i++) {
            for (int j = y_top; j <= y_bottom; j++) {
                result.add(new Pair<>((i + CHUNK_COUNT) % CHUNK_COUNT, (j + CHUNK_COUNT) % CHUNK_COUNT));
            }
        }
        return result;
    }
    ArrayList<Pair<Integer, Integer>> temp = new ArrayList<>();

    private void addCollisionBox(VisibleObject object) {
        if(!(object instanceof InteractiveObject)) return;
        Rectangle box = ((InteractiveObject)object).collisionBox();
        if(box == null || !box.real()) return;

        temp.clear();
        overlapsChunks(box, temp);
        for(Pair<Integer, Integer> to: temp) {
            if(CollisionBoxes[to.first][to.second].size() == 0)  {
                lastChecked.add(to);
            }
            CollisionBoxes[to.first][to.second].add(object);
        }
    }

    private void addHitBox(VisibleObject object) {
        if(!(object instanceof InteractiveObject)) return;
        Rectangle box = ((InteractiveObject)object).hitBox();
        if(box != null && !box.real()) return;

        temp.clear();
        overlapsChunks(box, temp);
        for(Pair<Integer, Integer> to: temp) {
            if(HitBoxes[to.first][to.second].size() == 0)  {
                lastChecked.add(to);
            }
            HitBoxes[to.first][to.second].add(object);
        }
    }

    private void add(VisibleObject object) {
       addCollisionBox(object);
       addHitBox(object);
    }

    public void build(List<VisibleObject> list) {
        clearGrid();
        for(VisibleObject object: list) {
            add(object);
        }
    }

    public boolean checkCollision(Rectangle collisionBox, VisibleObject object) {
        if(!(object instanceof InteractiveObject)) return false;
        temp.clear();
        overlapsChunks(((InteractiveObject)object).collisionBox(), temp);
        for(Pair<Integer, Integer> to: temp) {
            for(VisibleObject other: CollisionBoxes[to.first][to.second]) {
                if(object == other) continue;
                if(collisionBox.overlaps(((InteractiveObject)other).collisionBox())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doAttack(VisibleObject object) {
        return false;
    }


}
