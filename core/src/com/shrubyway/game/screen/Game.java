package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.*;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Food;
import com.shrubyway.game.item.Harmonica;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.myinterface.HealthBar;
import com.shrubyway.game.myinterface.Inventory;
import com.shrubyway.game.myinterface.MiniMap;
import com.shrubyway.game.saver.GameSaver;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;
import com.shrubyway.game.visibleobject.effect.Explosion;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.EntityManager;
import com.shrubyway.game.visibleobject.entity.Shruby;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Screen implements java.io.Serializable {
    static public OrthographicCamera localCamera;
    Vector2 mousePosition;

    public boolean gameOver = false, menu = false;
    LoadingScreen loadingScreen;
    Event event;

    public static ObjectsList objectsList = new ObjectsList();
    public static Inventory inventory = new Inventory();
    GameSaver gameSaver = new GameSaver();
    public static Map map;
    static public Shruby player;

    Button continueButton, settingsButton, menuButton;


    public Game() {
       GlobalAssetManager.loadAll();
       if(loadingScreen == null)
           loadingScreen = new LoadingScreen();
       loadingScreen.startLoading();
    }

    private void init() {
        objectsList.getList().clear();
        GlobalBatch.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        soundClick = GlobalAssetManager.get("sounds/EFFECTS/Click.ogg", Sound.class);

        float bWidht = GlobalAssetManager.get("interface/c1.png", Texture.class).getWidth(),
                bHeight = GlobalAssetManager.get("interface/c1.png", Texture.class).getHeight();

        continueButton= new Button(GlobalAssetManager.get("interface/c1.png", Texture.class),
                GlobalAssetManager.get("interface/c2.png", Texture.class), 1920 / 2 -
                bWidht / 2, 1080 / 2 - bHeight / 2 + bHeight + 50);
        settingsButton = new Button(GlobalAssetManager.get("interface/s1.png", Texture.class),
                        GlobalAssetManager.get("interface/s2.png", Texture.class), 1920 / 2 -
                bWidht / 2, 1080 / 2 - bHeight / 2);
        menuButton = new Button(GlobalAssetManager.get("interface/m1.png", Texture.class),
                        GlobalAssetManager.get("interface/m2.png", Texture.class), 1920 / 2 -
                bWidht / 2, 1080 / 2 - bHeight / 2 - bHeight - 50);
        inventory = new Inventory();
        EntityManager.init();
        DecorationsManager.init();
        ItemManager.init();
        MobsManager.init();

        objectsList = new ObjectsList();
        event = new Event();
        map = new Map(1);
        player = new Shruby(5555, 34200);
        localCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        localCamera.position.set(player.positionCenter().x, player.positionCenter().y, 0);
        localCamera.update();

        objectsList.add(player);
        AnimationGlobalTime.clear();
    }

    public void correctPosition() {
        Vector2 temp = new Vector2(player.position());
        if (temp.x < 0) {
            temp.add(new Vector2(MapSettings.MAPSIZE, 0));
            localCamera.position.add(new Vector3(MapSettings.MAPSIZE, 0, 0));
        }
        if (temp.y < 0) {
            temp.add(new Vector2(0, MapSettings.MAPSIZE));
            localCamera.position.add(new Vector3(0, MapSettings.MAPSIZE, 0));
        }
        if (temp.x >= MapSettings.MAPSIZE) {
            temp.add(new Vector2(-MapSettings.MAPSIZE, 0));
            localCamera.position.add(new Vector3(-MapSettings.MAPSIZE, 0, 0));
        }
        if (temp.y >= MapSettings.MAPSIZE) {
            temp.add(new Vector2(0, -MapSettings.MAPSIZE));
            localCamera.position.add(new Vector3(0, -MapSettings.MAPSIZE, 0));
        }
        player.changePosition(temp);
    }

    boolean leftClick = false, rightClick = false;

    public void playerInputWorking() {
        mousePosition = new Vector2(ShrubyWay.inputProcessor.mousePosition().x
                + localCamera.position.x - Gdx.graphics.getWidth() / 2,
                ShrubyWay.inputProcessor.mousePosition().y
                        + localCamera.position.y - Gdx.graphics.getHeight() / 2);
        if(ShrubyWay.inputProcessor.isCPressed()) {
            saveGame();
        }
        if(ShrubyWay.inputProcessor.isLPressed()) {
            loadGame();
        }
        boolean spacePressed = ShrubyWay.inputProcessor.isSpacePressed();
        if (spacePressed && player.canAct()) {
            int temp = inventory.selectedItem();
            if(temp != -1) {
                Boolean f = true;
                if((ItemManager.itemActing[temp] instanceof Food && player.health.getHealth()
                        == player.health.getMaxHealth())) f = false;
                if(ItemManager.itemActing[temp] == null) f = false;

                if(f) {
                    ItemManager.itemActing[temp].Acting();
                    if (ItemManager.itemActing[temp].checkAct()) {
                        if (ItemManager.itemActing[temp] instanceof Food) {

                            Item temp2 = inventory.take();
                            player.health.heal(
                                    ((Food) ItemManager.itemActing[temp2.id]).heling);
                        }
                        if (ItemManager.itemActing[temp] instanceof Harmonica) {
                            ShrubyWay.inputProcessor.setSpacePressed(false);
                        }
                    }
                    player.updateAnimation(ItemManager.itemActing[temp].actingAnimation);
                } else {
                    if(ItemManager.itemActing[temp] != null) ItemManager.itemActing[temp].stopActing();
                }
            }

        } else {
            int temp = inventory.selectedItem();
            if(temp != -1) {
                if(ItemManager.itemActing[temp] != null) {
                    ItemManager.itemActing[temp].stopActing();
                }
            }
        }
        leftClick = ShrubyWay.inputProcessor.isMouseLeft();
        rightClick = ShrubyWay.inputProcessor.isMouseRight();

        if(inventory.selectedItem() == -1 || ItemManager.itemActing[inventory.selectedItem()] == null
        || !ItemManager.itemActing[inventory.selectedItem()].stillActing) {
            player.running(ShrubyWay.inputProcessor.isRuning());
            Vector2 movingVector = ShrubyWay.inputProcessor.getMovementDirection();
            if (movingVector.len() != 0) {
                if (inventory.selectedItem() != -1) {
                    if (ItemManager.itemActing[inventory.selectedItem()] != null) {
                        ItemManager.itemActing[inventory.selectedItem()].stopActing();
                    }
                }
            }
            player.tryMoveTo(movingVector);
            correctPosition();
        }



        if ((leftClick || rightClick) &&
                !inventory.checkClick(ShrubyWay.inputProcessor.mousePosition())) {
            if (leftClick) {
                player.attack(mousePosition);
            }
            if (rightClick) {
                if (player.canThrow()) {
                    int selected = inventory.selectedItem();
                    if (selected != -1 && ItemManager.throwingDamage[selected] != 0) {
                        Item temp = inventory.take();
                        player.throwItem(mousePosition, temp, true);
                    }
                }
            }

            leftClick = false;
            rightClick = false;
        }
    }

    public float lastDrop = -1000000000f;
    Sound soundClick;


    public void menuInputWorking() {
        if (ShrubyWay.inputProcessor.isEscPressed()) {
            if (gamePaused) {
                resume();
            } else {
                pause();
            }
        }
        if(gamePaused) {
            continueButton.update();
            menuButton.update();
            settingsButton.update();
            if(!ShrubyWay.inputProcessor.isMouseLeft()) return;
            if(continueButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                soundClick.play(SoundSettings.soundVolume);
                resume();
            }
            if(menuButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                soundClick.play(SoundSettings.soundVolume);
                resume();
                menu = true;
            }
            if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                soundClick.play(SoundSettings.soundVolume);
                // settings = true;
            }
        }
    }

    public void interfaceInputWorking() {
        if (ShrubyWay.inputProcessor.isEPressed()) {
            inventory.changeOpened();
        }
        int x = ShrubyWay.inputProcessor.numberPressed();
        if (x > 0) {
            inventory.changeSelected(x);
        }
        x = ShrubyWay.inputProcessor.getScroll();
        inventory.addSelected(x);

        if (ShrubyWay.inputProcessor.isQPressed() && (AnimationGlobalTime.time() -
                lastDrop) > 0.1f) {
            lastDrop = AnimationGlobalTime.time();
            inventory.dropItem(player.faceDirection, player.positionItemDrop());
        }
        if (leftClick) {
            leftClick = false;
            inventory.leftClick(ShrubyWay.inputProcessor.mousePosition());
        }
        if (rightClick) {
            rightClick = false;
            inventory.rightClick(ShrubyWay.inputProcessor.mousePosition());
            ShrubyWay.inputProcessor.setMouseRight(false);
        }

        if (!inventory.opened) {
            inventory.dropItemHand(player.faceDirection, player.positionItemDrop());
        }

    }


    CopyOnWriteArrayList<VisibleObject> temp = new CopyOnWriteArrayList<VisibleObject>();
    CopyOnWriteArrayList<InteractiveObject> temp2 = new CopyOnWriteArrayList<>();

    float lastMobUpdate = 5f;

    public void globalProcessing() {

        if (AnimationGlobalTime.time() - lastMobUpdate > 1f) {
            lastMobUpdate = AnimationGlobalTime.time();
            MobsManager.playerAddUpdate(1);
            MobsManager.tryGenerateMob(player.positionLegs());
        }
        temp.clear();
        temp2.clear();
        for (VisibleObject obj : objectsList.getList()) {
            temp.add(obj);
        }
        map.update(player.positionLegs());

        for (VisibleObject obj : temp) {
            if (!objectsList.getList().contains(obj)) continue;
            if(obj instanceof Explosion exp) {

                for(VisibleObject obj2 : temp) {
                    if(obj2 instanceof Entity ent2) {
                        ((Entity) obj2).addMomentum(exp.getMomentum(ent2.positionCenter()));
                        if(!exp.damaged) {
                            ent2.getDamage(exp.getDamage(ent2.positionCenter()));
                        }
                    }
                    if(obj2 instanceof Bullet bul) {

                    }
                    if(obj2 instanceof Decoration dec) {
                        if(!exp.damaged) {
                            if(exp.getDamage(dec.positionCenter()) != 0) dec.interact();
                        }
                    }
                }
                exp.damaged = true;
            } else
            if (obj instanceof Bullet bul) {
                bul.processBullet(player.positionCenter());

            } else if (obj instanceof Entity ent) {
                ent.liquidStatus(map.checkLiquid(ent.positionLegs()));
                if (ent.makingStep(map.checkTile(ent.positionLegs()))) {
                    map.makeStep(ent.positionLegs(), player.positionLegs());
                }
                if (obj instanceof Mob mob) {
                    mob.ai(player.positionLegs());
                }
            } else if (obj instanceof VisibleItem visobj) {
                visobj.moveToPlayer(player.positionItemDrop(), inventory);
            }
        }

        for (VisibleObject obj : objectsList.getList()) {
            if (obj instanceof InteractiveObject io) temp2.add(io);
        }

        for (InteractiveObject obj : temp2) {
            if (!objectsList.getList().contains(obj)) continue;

            if (obj.attackBox() != null && obj.attackBox().topLeftCorner.x < obj.attackBox().bottomRightCorner.x) {
                for (InteractiveObject obj2 : temp2) {
                    if (!objectsList.getList().contains(obj2)) continue;
                    if (obj == obj2) continue;

                    if (obj2.hitBox() != null) {
                        if (obj.attackBox().overlaps(obj2.hitBox())) {
                            if (obj instanceof Bullet bul) {
                                if (bul.whoThrow == obj2) continue;
                            }
                            if (obj2 instanceof Decoration dec) {
                                dec.interact();
                            } else if (obj2 instanceof Entity ent) {
                                ent.getDamage(obj.damage,
                                        obj.positionCenter());
                            }
                            if (obj instanceof Bullet bul) {
                                bul.die();
                            }
                        }
                    }
                }
            }
        }

        for (VisibleObject obj : objectsList.getList()) {
            if (obj instanceof Entity ent) {
                ent.update();
            }
        }
        objectsList.sort();
        if(!objectsList.contains(player)) {
            gameOver = true;
            map.pauseSounds();
        }
        GlobalSoundManager.update(player.positionCenter());
    }

    public void cameraUpdate() {
        localCamera.position.add(new Vector3(-CameraEffects.getAddPositionExplosion().x,
                -CameraEffects.getAddPositionExplosion().y, 0));
        localCamera.position.lerp(new Vector3(player.positionCenter(),0), 0.1f);
        CameraEffects.update();
        localCamera.position.add(new Vector3(CameraEffects.getAddPositionExplosion(), 0));
        localCamera.update();
    }

    public void gameTick() {
        playerInputWorking();
        interfaceInputWorking();
        globalProcessing();
        cameraUpdate();
    }


    public void renderShadows() {
        for(VisibleObject obj : objectsList.getList()) {
            if(obj instanceof Entity ent) {
                if(!ent.liquid()) {
                    ent.renderShadow();
                }
            }
        }
    }

    public void renderObjects() {
        for (VisibleObject obj : objectsList.getList()) {
            obj.render();
        }
    }

    public void renderInterface() {
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        HealthBar.render(player.health);
        inventory.render(ShrubyWay.inputProcessor.mousePosition());
        //TextDrawer.drawWithShadow("" + player.position, 100, 500, 1);
        MiniMap.render(map.lvl, player.positionLegs().x, player.positionLegs().y);
        if (gamePaused) {
             GlobalBatch.render(GlobalAssetManager.get("interface/shadow.png", Texture.class),
                    0, 0);
            if(menuButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                menuButton.renderSellected();
            } else menuButton.render();
            if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                settingsButton.renderSellected();
            } else settingsButton.render();
            if(continueButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                continueButton.renderSellected();
            } else continueButton.render();
        }
    }


    public void pause() {
        gamePaused = true;
        AnimationGlobalTime.pause();
        map.pauseSounds();
    }

    public void resume() {
        gamePaused = false;
        AnimationGlobalTime.resume();
        map.resumeSounds();
    }


    public void renderFrame() {
        GlobalBatch.setProjectionMatrix(localCamera);
        ScreenUtils.clear(1, 1, 1, 1);
        //batch.begin();
        map.render(player.position());
        renderShadows();
        renderObjects();
        renderInterface();
       // batch.end();
    }

    Boolean gamePaused = false;
    Boolean gameInitialized = false;
    @Override
    public void updateScreen() {
        if(!gameInitialized) {
                if (GlobalAssetManager.update()) {
                    gameInitialized = true;
                    init();
                    updateScreen();
                }
            loadingScreen.updateStatus((int)(GlobalAssetManager.getProgress() * 100));
            loadingScreen.updateScreen();
        } else {
           // SoundSettings.changeMusic("music/Forest.mp3");
            menuInputWorking();
            if (!gamePaused) gameTick();
        }

    }

    @Override
    public void renderScreen() {
        if(!gameInitialized) {
            loadingScreen.renderScreen();
        }else {
            renderFrame();
        }
    }

    public void saveGame() {
         gameSaver.saveGameFiles();
        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "000.txt";

        File shrubyDirectory = new File(userHome, "ShrubyWay");
        if (!shrubyDirectory.exists()) {
            shrubyDirectory.mkdirs();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(gameSaver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void loadGame() {
        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "000.txt";

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            gameSaver = (GameSaver) objectInputStream.readObject();
            gameSaver.loadGameFiles();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
