package com.shrubyway.game.map;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.saver.VisualObjectListSaver;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;

import java.util.*;

public class Map {
    public Background background;
    Set<VisibleObject>[][] chunks = new HashSet[MapSettings.TILENUMBER / MapSettings.CHUNKSIZE][MapSettings.TILENUMBER / MapSettings.CHUNKSIZE];
    boolean lastCheked[][] = new boolean[MapSettings.TILENUMBER / MapSettings.CHUNKSIZE][MapSettings.TILENUMBER / MapSettings.CHUNKSIZE];

    int mobTotalCostInChunk[][] = new int[MapSettings.TILENUMBER / MapSettings.CHUNKSIZE][MapSettings.TILENUMBER / MapSettings.CHUNKSIZE];
    long timeChecking = 0;

    public char decorations[][] = new char[MapSettings.TILENUMBER][MapSettings.TILENUMBER];

    public void clearMobTotalCost() {
        for (int i = 0; i < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; i++)
            for (int j = 0; j < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; j++) {
                mobTotalCostInChunk[i][j] = 0;
            }
    }

    public void addCost(Vector2 position, int cost) {
        int x = (int) position.x;
        if (x < 0) x += MapSettings.MAPSIZE;
        if (x >= MapSettings.MAPSIZE) x -= MapSettings.MAPSIZE;
        x /= MapSettings.TYLESIZE;
        x /= MapSettings.CHUNKSIZE;
        int y = (int) position.y;
        if (y < 0) y += MapSettings.MAPSIZE;
        if (y >= MapSettings.MAPSIZE) y -= MapSettings.MAPSIZE;
        y /= MapSettings.TYLESIZE;
        y /= MapSettings.CHUNKSIZE;

        mobTotalCostInChunk[x][y] += cost;
    }

    public VisualObjectListSaver[][] getChunks() {
        VisualObjectListSaver[][] chunks = new VisualObjectListSaver[MapSettings.TILENUMBER / MapSettings.CHUNKSIZE][MapSettings.TILENUMBER / MapSettings.CHUNKSIZE];
        for (int i = 0; i < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; i++)
            for (int j = 0; j < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; j++) {
                chunks[i][j] = new VisualObjectListSaver(this.chunks[i][j]);
            }
        return chunks;
    }

    public void setChunks(VisualObjectListSaver[][] chunks) {
        for (int i = 0; i < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; i++)
            for (int j = 0; j < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; j++) {
                this.chunks[i][j].clear();
                for(VisibleObject visibleObject : chunks[i][j].getList()) {
                    if(visibleObject instanceof Mob) {
                        addCost(visibleObject.position, (int) MobsManager.mobSpawnCost[((Mob)visibleObject).id]);
                    }
                    this.chunks[i][j].add(visibleObject);
                }
            }
    }

    public int lvl;

    private void decorationsLoad(int level) {
        String fileName = "maps/" + level + "/Decorations.txt";
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/" + fileName))) {
            int j = 0;
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                for (int q = 0; q < MapSettings.TILENUMBER; q++) {
                    decorations[q][MapSettings.TILENUMBER - 1 -j] = temp.charAt(q);
                }
                j++;
            }
        } catch (Exception e) {
        }
    }

    public void pauseSounds() {
        background.pauseSounds();
    }
    public void resumeSounds() {
        background.resumeSounds();
    }


    public Map(int level) {
        lvl = level;
        background = new Background(level);
        decorationsLoad(level);

        for (int i = 0; i < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; i++)
            for (int j = 0; j < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; j++) {
                chunks[i][j] = new HashSet<>();
            }

        for (int i = 0; i < MapSettings.TILENUMBER; i++)
            for (int j = 0; j < MapSettings.TILENUMBER; j++) {
                if (decorations[i][j] != '0') {
                    int type = decorations[i][j] - '1';
                    Decoration temp = DecorationsManager.newOf(type);
                    temp.change(i * MapSettings.TYLESIZE, j * MapSettings.TYLESIZE, i, j);
                    chunks[i / MapSettings.CHUNKSIZE][j / MapSettings.CHUNKSIZE].add(temp);
                }
            }
    }

    public void updateChunk(int i2, int j2, Vector2 playerPosition) {
        int i = i2 / MapSettings.CHUNKSIZE, j = j2 / MapSettings.CHUNKSIZE;
        if (!lastCheked[i][j]) {
            lastCheked[i][j] = true;
            Iterator<VisibleObject> iterator = chunks[i][j].iterator();

            while(iterator.hasNext()) {
                VisibleObject check = iterator.next();
                boolean fx = false, fy = false;
                if (check.position.x >
                        playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceX
                        && check.position.x <
                        playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceX)
                    fx = true;
                else if (check.position.x - MapSettings.MAPSIZE >
                        playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceX
                        && check.position.x - MapSettings.MAPSIZE <
                        playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceX) {
                    check.position.x -= MapSettings.MAPSIZE;
                    fx = true;
                } else if (check.position.x + MapSettings.MAPSIZE >
                        playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceX
                        && check.position.x + MapSettings.MAPSIZE <
                        playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceX) {
                    check.position.x += MapSettings.MAPSIZE;
                    fx = true;
                }

                if (check.position.y >
                        playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                        && check.position.y <
                        playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY)
                    fy = true;
                else if (check.position.y - MapSettings.MAPSIZE >
                        playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                        && check.position.y - MapSettings.MAPSIZE <
                        playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {
                    check.position.y -= MapSettings.MAPSIZE;
                    fy = true;
                } else if (check.position.y + MapSettings.MAPSIZE >
                        playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                        && check.position.y + MapSettings.MAPSIZE <
                        playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {
                    check.position.y += MapSettings.MAPSIZE;
                    fy = true;
                }

                if (fx && fy)
                    if (check.position.x > playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                            && check.position.x < playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceY &&
                            check.position.y > playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                            && check.position.y < playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {
                        if(check instanceof InteractiveObject) {
                            ((InteractiveObject)check).unhideBody();
                        }
                        if(check instanceof Mob) {
                            addCost(check.position, -(int) MobsManager.mobSpawnCost[((Mob)check).id]);
                        }
                        Game.objectsList.add(check);
                        iterator.remove();
                    }
            }
        }
    }

    public void updateRenderingObjects(Vector2 playerPosition) {
        timeChecking++;
        int x, y;
       List<VisibleObject> temp = Game.objectsList.getList(), tempList = new ArrayList<>();


         for (VisibleObject check : temp) {
            if (Math.abs(check.position.x - playerPosition.x) >
                    MapSettings.TYLESIZE * MapSettings.calculationDistanceX ||
                    Math.abs(check.position.y - playerPosition.y) >
                            MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {


                x = (int) check.position.x;
                while (x < 0) x += MapSettings.MAPSIZE;
                while (x >= MapSettings.MAPSIZE) x -= MapSettings.MAPSIZE;
                x /= MapSettings.TYLESIZE;
                x /= MapSettings.CHUNKSIZE;

                y = (int) check.position.y;
                while (y < 0) y += MapSettings.MAPSIZE;
                while (y >= MapSettings.MAPSIZE) y -= MapSettings.MAPSIZE;
                y /= MapSettings.TYLESIZE;
                y /= MapSettings.CHUNKSIZE;

                if(check instanceof Mob) {
                    addCost(check.position, (int) MobsManager.mobSpawnCost[((Mob)check).id]);
                }
                chunks[x][y].add(check);
                if(check instanceof InteractiveObject) {
                    ((InteractiveObject)check).hideBody();
                }
                tempList.add(check);
            }
        }
        Game.objectsList.getList().removeAll(tempList);
        tempList.clear();

        x = (int) playerPosition.x;
        y = (int) playerPosition.y;
        x /= MapSettings.TYLESIZE;
        y /= MapSettings.TYLESIZE;
        boolean fx = false, fy = false;

        for (int i = x - MapSettings.calculationDistanceX; i < x + MapSettings.calculationDistanceX; i++) {
            for (int j = y - MapSettings.calculationDistanceY; j < y + MapSettings.calculationDistanceY; j++) {
                int i2 = (i + MapSettings.TILENUMBER) % MapSettings.TILENUMBER,
                        j2 = (j + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
                updateChunk(i2, j2, playerPosition);
            }
        }

        for (int i = x - MapSettings.calculationDistanceX; i < x + MapSettings.calculationDistanceX; i++) {
            for (int j = y - MapSettings.calculationDistanceY; j < y + MapSettings.calculationDistanceY; j++) {
                int i2 = (i + MapSettings.TILENUMBER) % MapSettings.TILENUMBER,
                        j2 = (j + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
                lastCheked[i2 / MapSettings.CHUNKSIZE][j2 / MapSettings.CHUNKSIZE] = false;
            }
        }

    }

    public void update(Vector2 playerPosition) {
        updateRenderingObjects(playerPosition);
        updateMobSpawning(playerPosition, false);
        background.update(playerPosition);
    }

    public void updateMobSpawning(Vector2 playerPosition, boolean initialGeneration) {
        if(!Event.happened("Mob_spawning_allowed")) return;
        int x = (int) playerPosition.x;
        if (x < 0) x += MapSettings.MAPSIZE;
        if (x >= MapSettings.MAPSIZE) x -= MapSettings.MAPSIZE;
        x /= MapSettings.TYLESIZE;
        x /= MapSettings.CHUNKSIZE;
        int y = (int) playerPosition.y;
        if (y < 0) y += MapSettings.MAPSIZE;
        if (y >= MapSettings.MAPSIZE) y -= MapSettings.MAPSIZE;
        y /= MapSettings.TYLESIZE;
        y /= MapSettings.CHUNKSIZE;

        int balance = 20 + (int) (Math.random() * 3) + (int)Math.sqrt(ElementPumping.getLVL());

        if (timeChecking % 3000 == 0) {
            for(int i = 0; i < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; i++)
                for(int j = 0; j < MapSettings.TILENUMBER / MapSettings.CHUNKSIZE; j++) {
                    int dist = Math.min((i - x + (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE)) % (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE),
                            (x - i + (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE)) % (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE)) +
                            + Math.min((j - y + (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE)) % (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE),
                            (y - j + (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE)) % (MapSettings.TILENUMBER / MapSettings.CHUNKSIZE));
                    if (dist > 1 && dist < 3 && mobTotalCostInChunk[i][j] < balance) {
                        MobsManager.generateMobsForChunk(i, j, balance - mobTotalCostInChunk[i][j]);
                    }
                }
        }
    }

    public void addVisibleObject(VisibleObject visibleObject) {
        int x = (int) visibleObject.position.x;
        if (x < 0) x += MapSettings.MAPSIZE;
        if (x >= MapSettings.MAPSIZE) x -= MapSettings.MAPSIZE;
        x /= MapSettings.TYLESIZE;
        x /= MapSettings.CHUNKSIZE;
        int y = (int) visibleObject.position.y;
        if (y < 0) y += MapSettings.MAPSIZE;
        if (y >= MapSettings.MAPSIZE) y -= MapSettings.MAPSIZE;
        y /= MapSettings.TYLESIZE;
        y /= MapSettings.CHUNKSIZE;
        chunks[x][y].add(visibleObject);
    }

    public void render(Vector2 cameraPosition) {
        background.render(cameraPosition);
    }

    public void makeStep(Vector2 step, Vector2 playerPosition) {
        background.makeStep(step, playerPosition);
    }

    public boolean checkLiquid(Vector2 position) {
        return background.checkLiquid(position);
    }

    public char checkTile(Vector2 position) {
        return background.checkTile(position);
    }

}