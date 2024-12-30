package com.shrubyway.game.visibleobject.entity;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Health;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;


public class Shruby extends Entity {
    public int money = 0;
    public Shruby(float x, float y) {
        entityID = 0;
        damage = 2f;
        health = new Health(20, 0.3f);
        speed = 10f;
        allowedMotion = false;
        action = 4;
        throwCooldown = 0.7f;
        position.set(x, y);
        regionWidth = (EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0)).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        Sound sound = ShrubyWay.assetManager.get("sounds/EFFECTS/PortalOut.ogg", Sound.class);
        sound.play(SoundSettings.soundVolume);
        animationTime = 0;
    }
    public Shruby(float x, float y, boolean f) {
        entityID = 0;
        damage = 2f;
        health = new Health(20, 0.3f);
        speed = 10f;
        allowedMotion = true;
        action = 0;
        throwCooldown = 0.7f;
        position.set(x, y);
        regionWidth = (EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0)).getRegionWidth();
        regionHeight = EntityManager.animations[entityID].get(0).get(0)[0].getKeyFrame(0).getRegionHeight();
        animationTime = 0;
    }

   @Override public Rectangle hitBox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + 150,
                position.y + 40,
                70, 140);
        return hitBox;
   }

   @Override public void getDamageWithoutMomentum(float damage, Vector2 hitPosition) {
        super.getDamageWithoutMomentum(damage, hitPosition);
        tookDamage = true;
   }

   Rectangle interactionBox;

   public Rectangle interactionBox() {
        if(interactionBox == null) interactionBox = new Rectangle(0,0,0,0);

        switch (faceDirection) {
            case 0:
                interactionBox.change(positionCenter().x - 25,
                        positionCenter().y - 150,
                        50, 150);
                break;
            case 1:
                interactionBox.change(positionCenter().x - 25,
                        positionCenter().y,
                        50, 150);
                break;
            case 2:
                interactionBox.change(positionCenter().x - 150,
                        positionCenter().y - 50,
                        150, 50);
                break;
            case 3:
                interactionBox.change(positionCenter().x,
                        positionCenter().y - 50,
                        150, 50);
                break;
        }
        return interactionBox;
    }

    @Override
    public void tryMoveTo(Vector2 direction) {
        super.tryMoveTo(direction);
        if(direction.x != 0 || direction.y != 0) moved = true;
    }

    @Override
    public void attack(Vector2 direction, Vector2 relativePosition) {
        super.attack(direction, relativePosition);
        if(attacking) attacked = true;
    }

    @Override public Rectangle attackBox() {
        if(attackBox == null)
            attackBox = new Rectangle(0,0,0,0);

        if(action == 2 && attacking) {
            switch (faceDirection) {
                case 0:
                    attackBox.change(position.x + 75,
                            position.y - 50,
                            210, 100);
                    break;
                case 1:
                    attackBox.change(position.x + 75,
                            position.y + 120,
                            210, 100);
                    break;
                case 2:
                    attackBox.change(position.x,
                            position.y + 55,
                            130, 70);
                    break;
                case 3:
                    attackBox.change(position.x + 240,
                            position.y + 55,
                            130, 70);
                    break;
            }
        } else {
            attackBox.change(0,
                    0,
                    -1, -1);
        }
        return attackBox;
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null) collisionBox = new Rectangle(0,0,0,0);
        collisionBox.change(position.x + 138,
                position.y + 5,
                95, 15);
        return collisionBox;
    }

    public void updateAnimation(int action) {
        if(!allowedMotion) return;
        if(this.action != action) {
            this.action = action;
            animationTime = AnimationGlobalTime.time();
        }
    }

    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 118);
        return tempPosition;
    }

    @Override
    public void throwItem(Vector2 shootPosition, Item item, boolean rotating) {
       if(!canThrow()) return;
        super.throwItem(shootPosition, item, rotating);
        throwed = true;
        throwCount++;
        lastThrowTime = AnimationGlobalTime.time();
    }

    public Vector2 positionItemDrop() {
        tempPosition.set(position.x + regionWidth / 2, position.y + 50);
        return tempPosition;
    }

    public int throwCount = 0;
    public boolean tookDamage = false;
    public float lastThrowTime = 0;
    public boolean moved = false, throwed = false, attacked = false;







}