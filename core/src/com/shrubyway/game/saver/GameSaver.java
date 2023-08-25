package com.shrubyway.game.saver;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.ObjectsList;

import java.io.Serializable;
import java.util.HashMap;

public class GameSaver implements Serializable {
    HashMap<String, Integer> eventHashMap;
    HashMap<Integer, Boolean> eventsHappened;
    Item items[][];
    Integer numberOfItem[][];
    Vector2 playerPosition;
    Health playerHealth;
    VisualObjectListSaver visualObjectListSaver;
    VisualObjectListSaver [][] chunks;

    int fireLevel, waterLevel, earthLevel, airLevel;
    float localExp;

    public void saveGameFiles() {
       eventHashMap = Event.eventHashMap;
       eventsHappened = Event.eventsHappened;
       items = Game.inventory.items;
       numberOfItem = Game.inventory.numberOfItem;
       playerPosition = Game.player.position;
       playerHealth = Game.player.health;
       visualObjectListSaver = new VisualObjectListSaver(Game.objectsList.getList());
       chunks = Game.map.getChunks();
       fireLevel = ElementPumping.fireLevel;
       waterLevel = ElementPumping.waterLevel;
       earthLevel = ElementPumping.earthLevel;
       airLevel = ElementPumping.airLevel;
       localExp = ElementPumping.localExp;
    }

    public void loadGameFiles() {
        Event.eventHashMap = eventHashMap;
        Event.eventsHappened = eventsHappened;
        Game.inventory.items = items;
        Game.inventory.numberOfItem = numberOfItem;
        Game.player.position.set(playerPosition);
        Game.localCamera.position.set(Game.player.positionCenter().x, Game.player.positionCenter().y, 0);
        Game.player.health.setHealth(playerHealth);

        ObjectsList objectsList = new ObjectsList();
        objectsList.getList().addAll(visualObjectListSaver.getList());
        objectsList.getList().add(Game.player);
        Game.objectsList = objectsList;
        Game.map.setChunks(chunks);
        ElementPumping.fireLevel = fireLevel;
        ElementPumping.waterLevel = waterLevel;
        ElementPumping.earthLevel = earthLevel;
        ElementPumping.airLevel = airLevel;
        ElementPumping.localExp = localExp;
    }

}