package com.shrubyway.game.saver;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.linemaker.ActionLogic;
import com.shrubyway.game.linemaker.ActionLogicManager;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.myinterface.TutorialHints;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.waiters.Waiter;
import com.shrubyway.game.waiters.WaiterManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
    ArrayList<Waiter> waiters;

    ArrayList<ActionLogic> ActionLogics;

    int fireLevel, waterLevel, earthLevel, airLevel;
    int MobDeathCounter;
    float localExp, lastRegen, time;

    int PlayerMoney = 0;

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
       lastRegen = Game.lastRegen;
       PlayerMoney = Game.player.money;
       ActionLogics = ActionLogicManager.activeLogics;
       MobDeathCounter = MobsManager.MobDeathCounter;
       waiters = WaiterManager.waiters;
       time = AnimationGlobalTime.time();
    }


     public void loadDefaultSettings() {
        Event.eventHashMap = new HashMap<String, Integer>();
        Event.eventsHappened = new HashMap<Integer, Boolean>();
        Game.inventory.items = new Item[5][9];
        Game.inventory.numberOfItem = new Integer[5][9];
        Game.player.position.set(17300, 18600);
        Game.player.health = new Health(20, 0.3f);
        ElementPumping.fireLevel = 1;
        ElementPumping.waterLevel = 1;
        ElementPumping.earthLevel = 1;
        ElementPumping.airLevel = 1;
        ElementPumping.localExp = 0;
        Game.player.money = 0;
        Game.lastRegen = 0;
        WaiterManager.waiters = new ArrayList<Waiter>();
        ActionLogicManager.activeLogics = new ArrayList<ActionLogic>();
        MobDeathCounter = 0;
        Game.overlay = null;
        TutorialHints.currentHint = -1;
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
        Game.lastRegen = lastRegen;
        AnimationGlobalTime.setTime(time);
        Game.player.money = PlayerMoney;
        ActionLogicManager.activeLogics = ActionLogics;
        WaiterManager.waiters = waiters;
        MobsManager.MobDeathCounter = MobDeathCounter;
        TutorialHints.currentHint = -1;
    }

    static public boolean checkSaveFile() {
        String userHome = System.getenv("APPDATA");
        String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "SAVE.txt";
        File file = new File(filePath);
        return file.exists();
    }

    static public void resetSave() {
        if(!checkSaveFile()) return;
        String userHome = System.getenv("APPDATA");
        String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "SAVE.txt";
        File file = new File(filePath);
        file.delete();
    }



}
