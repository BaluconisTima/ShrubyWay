package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Inventory {
    static Texture base = new Texture("interface/mainInv.png"),
            full = new Texture("interface/fullInv.png"),
            select = new Texture("interface/selectInv.png");
    static public Boolean opened = false;
    static Sound click = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/Click.ogg"));
    static Rectangle buttons[][] = new Rectangle[5][9];
    static Item items[][] = new Item[5][9];
    static Integer numberOfItem[][] = new Integer[5][9];

    static Integer selected = 0;


    static Item itemInHand = null;
    static Integer numberOfItemInHand = 0;

    static public void clear() {
        for(int i = 0; i < 9; i++) {
            buttons[0][i] = new Rectangle(27 + 79.1f * i,1080 - 70 - 28,70,70);
        }
        for(int j = 1; j < 5; j++) {
            for(int i = 0; i < 9; i++) {
                buttons[j][i] = new Rectangle(27 + 79.1f * i,1080 - 70 - 55 - 79.1f * j,70,70);
            }
        }
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 9; j++) {
                items[i][j] = null;
                numberOfItem[i][j] = 0;
            }
    }
    static {
        clear();
    }

    static private void nameAndDesc(Batch batch, Vector2 mousePosition, int i, int j) {
        if(buttons[i][j].checkPoint(mousePosition)) {
            if(items[i][j] != null) {
                TextDrawer.drawWithShadow(batch, ItemManager.itemName[items[i][j].id],
                        mousePosition.x + 15,
                        mousePosition.y + 35, 0.5f);
                TextDrawer.drawWithShadow(batch, ItemManager.itemDescription[items[i][j].id],
                        mousePosition.x + 15,
                        mousePosition.y + 5, 0.3f, 0.6f);
            }
        }
    }


    static public void render(Batch batch, Vector2 mousePosition) {
       batch.draw(base,0,0);
       if(opened) batch.draw(full,0,0);

        for(int i = 0; i < 9; i++) {
            buttons[0][i].render(batch);
            if(items[0][i] != null) {
                batch.draw(ItemManager.itemTexture[items[0][i].id],
                        27 + 79.1f * i, 1080 - 70 - 28, 70, 70);
                if(numberOfItem[0][i] > 1)
                    TextDrawer.drawWithShadow(batch, numberOfItem[0][i].toString(),
                        27 + 79.1f * i + 50 - (numberOfItem[0][i].toString().length()-1) * 17,
                        1080 - 70, 0.5f);
            }
        }
        if(opened)
            for (int i = 1; i < 5; i++)
                for (int j = 0; j < 9; j++) {
                    buttons[i][j].render(batch);
                    if (items[i][j] != null) {
                        batch.draw(ItemManager.itemTexture[items[i][j].id],
                                27 + 79.1f * j,
                                1080 - 70 - 55 - 79.1f * i, 70, 70);
                        if (numberOfItem[i][j] > 1)
                            TextDrawer.drawWithShadow(batch, numberOfItem[i][j].toString(),
                                    27 + 79.1f * j + 50 - (numberOfItem[i][j].toString().length() - 1) * 17,
                                    1080 - 70 - 28
                                            - 79.1f * i, 0.5f);
                    }
                }

        batch.draw(select, 12 + 79.1f * selected, 1080 - 111.55f);
        for(int j = 0; j < 9; j++) {
            nameAndDesc(batch, mousePosition, 0, j);
        }
        if(opened) {
            for(int i = 1; i < 5; i++) {
               for(int j = 0; j < 9; j++) {
                   nameAndDesc(batch, mousePosition, i, j);
               }
            }
            if(itemInHand != null) {
                batch.draw(ItemManager.itemTexture[itemInHand.id],
                        mousePosition.x - 35, mousePosition.y - 35, 70, 70);
                if(numberOfItemInHand > 1) {
                    TextDrawer.drawWithShadow(batch, numberOfItemInHand.toString(),
                            mousePosition.x + 15 - (numberOfItemInHand.toString().length()-1) * 17,
                            mousePosition.y -10, 0.5f);
                }
            }
        }

    }

    static public boolean checkClick(Vector2 point) {
        if(!opened) {
            if(point.x <= 750 && point.y >= 970) {
                changeOpenned();
                return true;
            }
            return false;
        }
        if(point.x <= 750 && point.y >= 620) return true;
        changeOpenned();
        return false;
    }

    static public void leftClick(Vector2 point) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 9; j++) {
                if(buttons[i][j].checkPoint(point)) {
                    if(items[i][j] != null && itemInHand != null && items[i][j].id == itemInHand.id) {
                        numberOfItem[i][j] += numberOfItemInHand;
                        if(numberOfItem[i][j] >= 100) {
                            numberOfItemInHand = numberOfItem[i][j] - 99;
                            numberOfItem[i][j] = 99;
                        } else { numberOfItemInHand = 0; itemInHand = null; }
                    }
                    Item temp = items[i][j]; Integer temp2 = numberOfItem[i][j];
                    items[i][j] = itemInHand; numberOfItem[i][j] = numberOfItemInHand;
                    itemInHand = temp; numberOfItemInHand = temp2;
                    return;
                }
            }
        }

    }
    static public void rightClick(Vector2 point) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 9; j++) {
                if(buttons[i][j].checkPoint(point)) {
                    if(items[i][j] == null && itemInHand == null) continue;
                    if(items[i][j] == null && itemInHand != null) {
                        numberOfItemInHand--;
                        numberOfItem[i][j] = 1;
                        items[i][j] = itemInHand;
                        if(numberOfItemInHand == 0) {
                            itemInHand = null;
                        }
                        continue;
                    }
                    if(items[i][j] != null && itemInHand == null) {
                        numberOfItemInHand = 1;
                        numberOfItem[i][j]--;
                        itemInHand = items[i][j];
                        if(numberOfItem[i][j] == 0) {
                            items[i][j] = null;
                        }
                        continue;
                    }
                    if(items[i][j].id == itemInHand.id && numberOfItemInHand < 99) {
                        numberOfItemInHand++;
                        numberOfItem[i][j]--;
                        if(numberOfItem[i][j] == 0) {
                            items[i][j] = null;
                        }
                        continue;
                    }


                    return;
                }
            }
        }

    }
    static public void changeOpenned() {
        click.play(SoundSettings.soundVolume);
        opened = !opened;
    }

    static public void drop(Item temp, int faceDirection, Vector2 playerPosition) {
        switch (faceDirection) {
            case 0:
                ObjectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(0,-1)));
                break;
            case 1:
                ObjectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(0,1)));
                break;
            case 2:
                ObjectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(-1,0)));
                break;
            case 3:
                ObjectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(1,0)));
                break;
        }
    }
    static public void dropItem(int faceDirection, Vector2 playerPosition) {
        if(items[0][selected] != null) {
            drop(items[0][selected],faceDirection,playerPosition);
                numberOfItem[0][selected]--;
                if(numberOfItem[0][selected] == 0) items[0][selected] = null;
            }
    }

    static public void dropItemHand(int faceDirection, Vector2 playerPosition) {
        while(itemInHand != null) {
            drop(itemInHand,faceDirection,playerPosition);
            numberOfItemInHand--;
            if(numberOfItemInHand == 0) itemInHand = null;
        }
    }

    static public Item take() {

        if(items[0][selected] != null) {
            Item temp = items[0][selected];
            numberOfItem[0][selected]--;
            if(numberOfItem[0][selected] == 0) items[0][selected] = null;

            return temp;
        }
        return null;
    }

    static public int selectedItem() {
        if(items[0][selected] == null) return -1;
        return items[0][selected].id;
    }
    static public void changeSelected(int i) {
        selected = i - 1;
    }
    static public void addSelected(int i) {
        selected += i + 999999;
        selected %= 9;
    }

    static public boolean addItem(Item item) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 9; j++) {
                if(items[i][j] != null &&
                        items[i][j].id == item.id && numberOfItem[i][j] < 99) {
                    numberOfItem[i][j]++;
                    return true;
                }
            }
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 9; j++) {
                if(items[i][j] == null) {
                    items[i][j] = item;
                    numberOfItem[i][j] = 1;
                    return true;
                }
            }
        return false;
    }

}
