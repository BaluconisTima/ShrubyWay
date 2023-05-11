package com.shrubyway.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Map {
    Background background;
    CopyOnWriteArrayList<VisibleObject>[][] chunks = new CopyOnWriteArrayList[16][16];
    boolean lastCheked[][] = new boolean[16][16];
    long timeChecking = 0;

    char decorations[][] = new char[MapSettings.TILENUMBER][MapSettings.TILENUMBER];
    public List<VisibleObject> tempList;

    private void decorationsLoad(int level) {
        String fileName = "maps/" + level + "/Decorations.txt";
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/" + fileName))) {
            int j = 0;
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                for (int q = 0; q < MapSettings.TILENUMBER; q++) {
                    decorations[j][q] = temp.charAt(q);
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
        tempList = new ArrayList<>();
        background = new Background(level);
        decorationsLoad(level);

        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                chunks[i][j] = new CopyOnWriteArrayList<>();
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
        if (!lastCheked[i2 / 16][j2 / 16]) {
            lastCheked[i2 / 16][j2 / 16] = true;
            tempList.clear();
            for (VisibleObject check : chunks[i2 / 16][j2 / 16]) {
                boolean fx = false, fy = false;
                if (check.position.x >
                        playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceX
                        && check.position.x <
                        playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceX)
                    fx = true;
                else if (check.position.x - 38400 >
                        playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceX
                        && check.position.x - 38400 <
                        playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceX) {
                    check.position.x -= 38400;
                    fx = true;
                } else if (check.position.x + 38400 >
                        playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceX
                        && check.position.x + 38400 <
                        playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceX) {
                    check.position.x += 38400;
                    fx = true;
                }

                if (check.position.y >
                        playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                        && check.position.y <
                        playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY)
                    fy = true;
                else if (check.position.y - 38400 >
                        playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                        && check.position.y - 38400 <
                        playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {
                    check.position.y -= 38400;
                    fy = true;
                } else if (check.position.y + 38400 >
                        playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                        && check.position.y + 38400 <
                        playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {
                    check.position.y += 38400;
                    fy = true;
                }

                if (fx && fy)
                    if (check.position.x > playerPosition.x - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                            && check.position.x < playerPosition.x + MapSettings.TYLESIZE * MapSettings.calculationDistanceY &&
                            check.position.y > playerPosition.y - MapSettings.TYLESIZE * MapSettings.calculationDistanceY
                            && check.position.y < playerPosition.y + MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {
                        ObjectsList.add(check);
                        tempList.add(check);
                    }
            }
            chunks[i2 / 16][j2 / 16].removeAll(tempList);
            tempList.clear();
        }
    }

    public void updateRenderingObjects(Vector2 playerPosition) {
        timeChecking++;
        int x, y;
        CopyOnWriteArrayList<VisibleObject> temp = new CopyOnWriteArrayList<>(ObjectsList.getList());
        tempList.clear();
        for (VisibleObject check : temp) {
            if (Math.abs(check.position.x - playerPosition.x) >
                    MapSettings.TYLESIZE * MapSettings.calculationDistanceX ||
                    Math.abs(check.position.y - playerPosition.y) >
                            MapSettings.TYLESIZE * MapSettings.calculationDistanceY) {
                x = (int) check.position.x;
                while (x < 0) x += 38400;
                while (x >= 38400) x -= 38400;
                x /= MapSettings.TYLESIZE;
                x /= 16;
                y = (int) check.position.y;
                while (y < 0) y += 38400;
                while (y >= 38400) y -= 38400;
                y /= MapSettings.TYLESIZE;
                y /= 16;
                chunks[x][y].add(check);
                tempList.add(check);
            }
        }
        ObjectsList.getList().removeAll(tempList);
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
        if (x < 0) x += 38400;
        if (x >= 38400) x -= 38400;
        x /= 150;
        x /= 16;
        int y = (int) visibleObject.position.y;
        if (y < 0) y += 38400;
        if (y >= 38400) y -= 38400;
        y /= 150;
        y /= 16;
        chunks[x][y].add(visibleObject);
    }

    public void render(SpriteBatch batch, Vector2 playerPosition) {
        background.render(batch, playerPosition);
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