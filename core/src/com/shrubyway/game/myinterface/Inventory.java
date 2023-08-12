package com.shrubyway.game.myinterface;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Inventory {
    Texture base = GlobalAssetManager.get("interface/mainInv.png", Texture.class),
            full = GlobalAssetManager.get("interface/fullInv.png", Texture.class),
            select = GlobalAssetManager.get("interface/selectInv.png", Texture.class);
    public Boolean opened = false;
    Sound click = GlobalAssetManager.get(("sounds/EFFECTS/Click.ogg"), Sound.class);
    Rectangle buttons[][] = new Rectangle[5][9];
    public Item items[][] = new Item[5][9];
    public Integer numberOfItem[][] = new Integer[5][9];
    Integer selected = 0;
    Item itemInHand = null;
    Integer numberOfItemInHand = 0;

    public void clear() {
        for(int i = 0; i < 9; i++) {
            buttons[0][i] = new Rectangle(27 + 79.1f * i,1080 - 70 - 28 + 7,70,70);
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
    {
        clear();
    }

    private void nameAndDesc(Vector2 mousePosition, int i, int j) {
        if(buttons[i][j].checkPoint(mousePosition)) {
            if(items[i][j] != null) {
                TextDrawer.drawWithShadow(ItemManager.itemName[items[i][j].id],
                        mousePosition.x + 15,
                        mousePosition.y + 35, 0.5f);
                TextDrawer.drawWithShadow(ItemManager.itemDescription[items[i][j].id],
                        mousePosition.x + 15,
                        mousePosition.y + 5, 0.3f, 0.6f);
            }
        }
    }


    public void render(Vector2 mousePosition) {
        GlobalBatch.render(base,0,0);
       if(opened) GlobalBatch.render(full,0,0);
        GlobalBatch.render(select, 12 + 79.1f * selected, 1080 - 111.55f + 9);

        for(int i = 0; i < 9; i++) {
            buttons[0][i].render();
            if(items[0][i] != null) {
                GlobalBatch.render(ItemManager.itemTexture[items[0][i].id],
                        27 + 79.1f * i, 1080 - 70 - 28 + 7, 70, 70);
                if(numberOfItem[0][i] > 1)
                    TextDrawer.drawWithShadow(numberOfItem[0][i].toString(),
                        27 + 79.1f * i + 50 - (numberOfItem[0][i].toString().length()-1) * 17,
                        1080 - 70 + 7, 0.5f);
            }
        }
        if(opened)
            for (int i = 1; i < 5; i++)
                for (int j = 0; j < 9; j++) {
                    buttons[i][j].render();
                    if (items[i][j] != null) {
                        GlobalBatch.render(ItemManager.itemTexture[items[i][j].id],
                                27 + 79.1f * j,
                                1080 - 70 - 55 - 79.1f * i, 70, 70);
                        if (numberOfItem[i][j] > 1)
                            TextDrawer.drawWithShadow(numberOfItem[i][j].toString(),
                                    27 + 79.1f * j + 50 - (numberOfItem[i][j].toString().length() - 1) * 17,
                                    1080 - 70 - 28
                                            - 79.1f * i, 0.5f);
                    }
                }


        for(int j = 0; j < 9; j++) {
            nameAndDesc(mousePosition, 0, j);
        }
        if(opened) {
            for(int i = 1; i < 5; i++) {
               for(int j = 0; j < 9; j++) {
                   nameAndDesc(mousePosition, i, j);
               }
            }
            if(itemInHand != null) {
                GlobalBatch.render(ItemManager.itemTexture[itemInHand.id],
                        mousePosition.x - 35, mousePosition.y - 35, 70, 70);
                if(numberOfItemInHand > 1) {
                    TextDrawer.drawWithShadow(numberOfItemInHand.toString(),
                            mousePosition.x + 15 - (numberOfItemInHand.toString().length()-1) * 17,
                            mousePosition.y -10, 0.5f);
                }
            }
        }

    }

    public boolean checkClick(Vector2 point) {
        if(!opened) {
            if(point.x <= 750 && point.y >= 970) {
                changeOpened();
                return true;
            }
            return false;
        }
        if(point.x <= 750 && point.y >= 620) return true;
        changeOpened();
        return false;
    }

    public void leftClick(Vector2 point) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 9; j++) {
                if(buttons[i][j].checkPoint(point)) {
                    if(items[i][j] != null && itemInHand != null && items[i][j].id == itemInHand.id) {
                        Item temp = items[i][j];
                        if(temp != null && ItemManager.itemActing[temp.id] != null) {
                            ItemManager.itemActing[temp.id].stopActing();
                        }
                        numberOfItem[i][j] += numberOfItemInHand;
                        if(numberOfItem[i][j] >= 100) {
                            numberOfItemInHand = numberOfItem[i][j] - 99;
                            numberOfItem[i][j] = 99;
                        } else {
                            numberOfItemInHand = 0; itemInHand = null;
                        }
                    }
                    Item temp = items[i][j]; Integer temp2 = numberOfItem[i][j];
                    items[i][j] = itemInHand; numberOfItem[i][j] = numberOfItemInHand;
                    itemInHand = temp; numberOfItemInHand = temp2;
                    return;
                }
            }
        }

    }
    public void rightClick(Vector2 point) {
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
                            Item temp = items[i][j];
                            if(temp != null && ItemManager.itemActing[temp.id] != null) {
                                ItemManager.itemActing[temp.id].stopActing();
                            }
                            items[i][j] = null;
                        }
                        continue;
                    }


                    return;
                }
            }
        }

    }
    public void changeOpened() {
        click.play(SoundSettings.soundVolume);
        opened = !opened;
    }

    public void drop(Item temp, int faceDirection, Vector2 playerPosition) {
        switch (faceDirection) {
            case 0:
                Game.objectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(0,-1)));
                break;
            case 1:
                Game.objectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(0,1)));
                break;
            case 2:
                Game.objectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(-1,0)));
                break;
            case 3:
                Game.objectsList.add(new VisibleItem(temp, playerPosition.x,
                        playerPosition.y, new Vector2(1,0)));
                break;
        }
    }
    public void dropItem(int faceDirection, Vector2 playerPosition) {
        if(items[0][selected] != null) {
            drop(items[0][selected],faceDirection,playerPosition);
            take();
            }
    }

    public void dropItemHand(int faceDirection, Vector2 playerPosition) {
        while(itemInHand != null) {
            drop(itemInHand,faceDirection,playerPosition);
            numberOfItemInHand--;
            if(numberOfItemInHand == 0) itemInHand = null;
        }
    }

    public Item take() {
        if(items[0][selected] != null) {
            Item temp = items[0][selected];
            numberOfItem[0][selected]--;
            if(numberOfItem[0][selected] == 0) {
                if(ItemManager.itemActing[temp.id] != null) {
                    ItemManager.itemActing[temp.id].stopActing();
                }
                items[0][selected] = null;
            }

            return temp;
        }
        return null;
    }

    public int selectedItem() {
        if(items[0][selected] == null) return -1;
        return items[0][selected].id;
    }
    public void changeSelected(int i) {
        if(i - 1 == selected) return;
        Item temp = items[0][selected];
        if(temp != null && ItemManager.itemActing[temp.id] != null) {
            ItemManager.itemActing[temp.id].stopActing();
        }
        selected = i - 1;
    }
    public void addSelected(int i) {
        if(i == 0) return;
        Item temp = items[0][selected];
        if(temp != null && ItemManager.itemActing[temp.id] != null) {
            ItemManager.itemActing[temp.id].stopActing();
        }
        selected += i + 999999;
        selected %= 9;
    }

    public boolean addItem(Item item) {
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
  /*  @Override public void write(Json json) {
       json.writeValue(this);
    }

    @Override public void read(Json json, JsonValue jsonData) {
        json.readFields(this, jsonData);
    } */

}
