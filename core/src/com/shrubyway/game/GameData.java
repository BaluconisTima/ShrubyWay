package com.shrubyway.game;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.myinterface.Inventory;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.ObjectsList;

public class GameData implements Json.Serializable {
    public Game game;
    public AnimationGlobalTime animationGlobalTime = new AnimationGlobalTime();
    public Inventory inventory = new Inventory();
    public Event event = new Event();
    public ObjectsList objectsList = new ObjectsList();

    public GameData(Game game) {
        this.game = game;
    }

    @Override public void write(Json json) {
        json.writeValue(this);
    }
    @Override public void read(Json json, JsonValue jsonData) {
        json.readFields(this, jsonData);
    }


}
