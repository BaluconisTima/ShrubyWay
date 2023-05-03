package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.HealthBar;
import com.shrubyway.game.Inventory;
import com.shrubyway.game.TextDrawer;
import com.shrubyway.game.adapters.MyInputAdapter;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.Shraby;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Screen{
    Shraby player;
    Map map;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector2 cameraPosition;
    Vector2 mousePosition;

    MyInputAdapter inputProcessor;


    public Game() {
        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        inputProcessor = new MyInputAdapter();
        MobsManager.init();
        DecorationsManager.init();
        map = new Map(1);
        player = new Shraby(50, 50);

        cameraPosition = new Vector2(player.positionCenter());
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(cameraPosition.x, cameraPosition.y, 0);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        ObjectsList.add(player);
        ItemManager.init();
        AnimationGlobalTime.x = 0f;
    }
    public void correctPosition() {
        Vector2 temp = new Vector2(player.position());
        if (temp.x < 0) {
            temp.add(new Vector2(38400, 0));
            cameraPosition.add(new Vector2(38400, 0));
        }
        if (temp.y < 0) {
            temp.add(new Vector2(0, 38400));
            cameraPosition.add(new Vector2(0, 38400));
        }
        if (temp.x >= 38400) {
            temp.add(new Vector2(-38400, 0));
            cameraPosition.add(new Vector2(-38400, 0));
        }
        if (temp.y >= 38400) {
            temp.add(new Vector2(0, -38400));
            cameraPosition.add(new Vector2(0, -38400));
        }
        player.changePosition(temp);
    }

    boolean leftClick = false, rightClick = false;
    public void playerInputWorking() {
        player.running(inputProcessor.isRuning());
        Vector2 movingVector = inputProcessor.getMovementDirection();
        mousePosition = new Vector2(inputProcessor.mousePosition().x + cameraPosition.x - Gdx.graphics.getWidth() / 2,
                inputProcessor.mousePosition().y + cameraPosition.y - Gdx.graphics.getHeight() / 2);
        leftClick = inputProcessor.isMouseLeft();
        rightClick = inputProcessor.isMouseRight();
        player.tryMoveTo(movingVector);
        correctPosition();


        if (inputProcessor.isSpacePressed()) {
            player.attack();
        }
        if((leftClick || rightClick) &&
                !Inventory.checkClick(inputProcessor.mousePosition())) {
            if(leftClick) {
                if(player.canThrow()) {
                    Item temp = Inventory.takeToThrow();
                    if(temp != null) {
                        player.throwItem(mousePosition, temp, true);
                    }

                }
            }
            if(rightClick) {
                ObjectsList.add(new VisibleItem(new Item(0), mousePosition.x, mousePosition.y));
            }
            leftClick = false;
            rightClick = false;
        }
    }
    public float lastDrop = -1000000000f;

    public void menuInputWorking() {
        if(inputProcessor.isEscPressed()) {
            if(gamePaused) {
                resume();
            } else {
                pause();
            }
        }
    }
    public void interfaceInputWorking() {
        if(inputProcessor.isEPressed()){
            Inventory.changeOpenned();
        }

        int x = inputProcessor.numberPressed();
        if(x > 0) {
            Inventory.changeSelected(x);
        }
        x = inputProcessor.getScroll();
        Inventory.addSelected(x);

        if(inputProcessor.isQPressed() && (AnimationGlobalTime.x -
                lastDrop) > 0.1f) {
            lastDrop = AnimationGlobalTime.x;
            Inventory.dropItem(player.faceDirection, player.positionItemDrop());
        }
        if(leftClick){
            leftClick = false;
            Inventory.leftClick(inputProcessor.mousePosition());
        }
        if(rightClick){
            rightClick = false;
            Inventory.rightClick(inputProcessor.mousePosition());
        }
        if(!Inventory.opened) {
            Inventory.dropItemHand(player.faceDirection, player.positionItemDrop());
        }

    }


    CopyOnWriteArrayList<VisibleObject> temp = new CopyOnWriteArrayList<VisibleObject>();
    CopyOnWriteArrayList<InteractiveObject> temp2 = new CopyOnWriteArrayList<InteractiveObject>();

    float lastMobUpdate = 5f;
    public void globalProcessing() {
        if(player.health.timeAfterHeal() >= 3 && Math.random() < 0.05f) {
            player.health.heal(1);
        }
        if(AnimationGlobalTime.x - lastMobUpdate > 1f) {
            lastMobUpdate = AnimationGlobalTime.x;
            MobsManager.playerAddUpdate(1);
            MobsManager.tryGenerateMob(player.positionLegs());
        }

        temp.clear(); temp2.clear();
        for(VisibleObject obj: ObjectsList.getList()) {
            temp.add(obj);
        }
        map.updateRenderingObjects(player.positionCenter());

        for (VisibleObject obj : temp) {
            if(!ObjectsList.getList().contains(obj)) continue;
            if (obj instanceof Bullet) {
                ((Bullet)obj).processBullet(player.positionCenter());

            } else
            if (obj instanceof Entity) {
                Entity temp = (Entity) obj;
                temp.liquidStatus(map.checkLiquid(((Entity) obj).positionLegs()));
                if(obj instanceof Mob) {
                    ((Mob) obj).ai(player.positionLegs());
                }
                if (((Entity) obj).makingStep(map.checkTile(((Entity) obj).positionLegs()))) {

                    map.makeStep(((Entity) obj).positionLegs(), player.positionCenter());
                }
            } else
            if(obj instanceof VisibleItem){
                ((VisibleItem) obj).moveToPlayer(player.positionItemDrop());
            }
        }

        for(VisibleObject obj: ObjectsList.getList()) {
            if(obj instanceof InteractiveObject) temp2.add((InteractiveObject) obj);
        }

        for(InteractiveObject obj : temp2){
            if(!ObjectsList.getList().contains(obj)) continue;

            if(obj.attackBox() != null && obj.attackBox().topLeftCorner.x < obj.attackBox().bottomRightCorner.x) {
                for(InteractiveObject obj2 : temp2){
                    if(!ObjectsList.getList().contains(obj2)) continue;
                    if(obj == obj2) continue;

                    if(obj2.hitBox() != null) {
                        if (obj.attackBox().overlaps(obj2.hitBox())) {
                            if(obj instanceof Bullet) {
                                if(((Bullet)obj).whoThrow == obj2) continue;
                            }
                            if(obj2 instanceof Decoration) {
                                ((Decoration) obj2).interact();
                            } else
                            if(obj2 instanceof Entity) {
                                ((Entity) obj2).getDamage(obj.damage,
                                        obj.positionCenter());
                            }
                            if(obj instanceof Bullet) {
                                ((Bullet) obj).die();
                            }
                        }
                    }
                }
            }
        }

        for(VisibleObject obj: ObjectsList.getList()) {
            if(obj instanceof Entity) {
                ((Entity) obj).update();
            }
        }
        ObjectsList.sort();
    }

    public void cameraUpdate() {
        cameraPosition.lerp(player.positionCenter(),
                0.1f);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Math.round(cameraPosition.x), Math.round(cameraPosition.y), 0);
        camera.update();
    }
    public void gameTick() {
        playerInputWorking();
        interfaceInputWorking();
        AnimationGlobalTime.x += Gdx.graphics.getDeltaTime();
        globalProcessing();
        cameraUpdate();
    }

    public void renderObjects() {
        map.render(batch, player.position());
        for (VisibleObject obj : ObjectsList.getList()) {
            obj.render(batch);
        }
    }

    public void renderInterface() {
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        HealthBar.render(batch, player.health);
        Inventory.render(batch, inputProcessor.mousePosition());

        if(gamePaused) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                    Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
            shapeRenderer.setColor(0, 0, 0, 0.2f);
            shapeRenderer.rect(0, 0, 1920, 1080);
            shapeRenderer.end();
        }
    }


    public void pause() {
        gamePaused = true;
        map.pauseSounds();
    }

    public void resume() {
        gamePaused = false;
        map.resumeSounds();
    }

    ShapeRenderer shapeRenderer;
    public void renderFrame() {
        shapeRenderer = new ShapeRenderer();
        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        renderObjects();
        renderInterface();
        batch.end();
    }

    Boolean gamePaused = false;

    @Override public void updateScreen() {
        menuInputWorking();
        if(!gamePaused) gameTick();
    }

    @Override public void renderScreen() {
        renderFrame();
    }

}
