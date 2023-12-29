package com.shrubyway.game.map;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.saver.VisualObjectListSaver;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;

import java.util.*;

public class Map {
    public Background background;
    Set<VisibleObject>[][] chunks = new HashSet[16][16];
    boolean lastCheked[][] = new boolean[16][16];
    long timeChecking = 0;

    public char decorations[][] = new char[MapSettings.TILENUMBER][MapSettings.TILENUMBER];

    public VisualObjectListSaver[][] getChunks() {
        int size = 0;
        VisualObjectListSaver[][] chunks = new VisualObjectListSaver[16][16];
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                chunks[i][j] = new VisualObjectListSaver(this.chunks[i][j]);
            }
        return chunks;
    }

    public void setChunks(VisualObjectListSaver[][] chunks) {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                this.chunks[i][j].clear();
                for(VisibleObject visibleObject : chunks[i][j].getList()) {
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

        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                chunks[i][j] = new HashSet<>();
            }

        for (int i = 0; i < MapSettings.TILENUMBER; i++)
            for (int j = 0; j < MapSettings.TILENUMBER; j++) {
                if (decorations[i][j] != '0') {
                    int type = decorations[i][j] - '1';
                    Decoration temp = DecorationsManager.newOf(type);
                    temp.change(i * MapSettings.TYLESIZE, j * MapSettings.TYLESIZE, i, j);
                    chunks[i / 16][j / 16].add(temp);
                }
            }
    }

    public void updateChunk(int i2, int j2, Vector2 playerPosition) {
        int i = i2 / 16, j = j2 / 16;
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
                        if(check instanceof InteractiveObject inter) {
                            inter.unhideBody();
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
                x /= 16;

                y = (int) check.position.y;
                while (y < 0) y += MapSettings.MAPSIZE;
                while (y >= MapSettings.MAPSIZE) y -= MapSettings.MAPSIZE;
                y /= MapSettings.TYLESIZE;
                y /= 16;

                chunks[x][y].add(check);
                if(check instanceof InteractiveObject inter) {
                    inter.hideBody();
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
                lastCheked[i2 / 16][j2 / 16] = false;
            }
        }

    }

    public void update(Vector2 playerPosition) {
        updateRenderingObjects(playerPosition);
        background.update(playerPosition);
    }

    public void addVisibleObject(VisibleObject visibleObject) {
        int x = (int) visibleObject.position.x;
        if (x < 0) x += MapSettings.MAPSIZE;
        if (x >= MapSettings.MAPSIZE) x -= MapSettings.MAPSIZE;
        x /= MapSettings.TYLESIZE;
        x /= 16;
        int y = (int) visibleObject.position.y;
        if (y < 0) y += MapSettings.MAPSIZE;
        if (y >= MapSettings.MAPSIZE) y -= MapSettings.MAPSIZE;
        y /= MapSettings.TYLESIZE;
        y /= 16;
        chunks[x][y].add(visibleObject);
    }

    public void render(Vector2 playerPosition) {
        background.render(playerPosition);
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