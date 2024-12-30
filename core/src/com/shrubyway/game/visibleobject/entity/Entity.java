package com.shrubyway.game.visibleobject.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.Health;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.animation.Animator;
import com.shrubyway.game.effect.Effect;
import com.shrubyway.game.effect.effecttypes.InvincibilityEffect;
import com.shrubyway.game.effect.effecttypes.SpeedEffect;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ThrowableItem;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundAtPosition;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.entity.mob.Mob;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


abstract public class Entity extends InteractiveObject {


    protected int entityID = -1;

    List<Effect> effects = new CopyOnWriteArrayList<>();

    public Health health;
    public byte faceDirection = 0;
    protected int action = 0;
    protected boolean inLiquid = false;

    protected float waterOverlayScale = 1f;
    protected Vector2 momentum = new Vector2(0, 0);
    protected float speed = 10f;
    protected boolean isRunning = false;
    protected float regionWidth, regionHeight;
    protected Vector2 tempPosition = new Vector2(0,0);

    protected float lastStepTime = 0f;
    protected float stepCooldown = 0.3f;

    protected char lastTile = '0';
    protected Boolean allowedMotion = false;
    static Sound soundAttack;

    protected float loopedAnimationTime = (float)Math.random() * 1000.0f;

    static  {
        soundAttack = ShrubyWay.assetManager.get("sounds/EFFECTS/Swing.ogg", Sound.class);
    }


    public Boolean dead() {
        return (action == 3);
    }

    public void addEffect(Effect effect) {
        for(Effect effect1 : effects) {
            if(effect1.getClass() == effect.getClass()) {
                effect1.merge(effect);
                return;
            }
        }
        effects.add(effect);
    }
    public void removeEffect(Effect effect) {
        if(effects.contains(effect))
            effects.remove(effect);
    }


    public float getSpeed1() {
        float tempSpeed = 0; tempSpeed += (speed);
        if(inLiquid) tempSpeed *= 0.75;
        if(action != 2) {if (isRunning) tempSpeed *= 1.25;}
        else tempSpeed *= 1.5;
        return tempSpeed * speedScale;
    }

    public float getSpeed() {
        return Math.abs(getSpeed1());
    }

    public boolean isSpeedNegative() {
        return (getSpeed1() < 0);
    }
    float speedScale = 1f;
    boolean invincible = false;
    private Color effectColor = Color.WHITE;

    public void updateEffects(float delta) {
        speedScale = 1f;
        invincible = false;
        for(Effect effect : effects) {
            effect.update(delta);
        }

        float red = 1, green = 1, blue = 1, alpha = 1, sum = 1;
        for(Effect effect : effects) {
            float multiplier = 0.6f * (float)effect.colorStrength();
            red += effect.color.r * multiplier;
            green += effect.color.g * multiplier;
            blue += effect.color.b * multiplier;
            alpha += effect.color.a * multiplier;
            sum += multiplier;
            if (effect instanceof SpeedEffect) {
                speedScale *= ((SpeedEffect) effect).speed;
            }
            if(effect instanceof InvincibilityEffect) {
                invincible = true;
            }
        }

        effectColor = new Color(red / sum, green / sum,
                blue / sum, alpha / sum);

    }
    public void update(float delta) {
        updateEffects(delta);
        tryMoveMomentum(delta);
        attacking = false;
        if(health.getHealth() <= 0) {
            die();
        }
        faceDirection = (byte)Math.min(faceDirection,
                (EntityManager.animations[entityID].get(action).size() - 1));

        if(action == 3 && EntityManager.animations[entityID].get(action).get(faceDirection)[inLiquid ? 1: 0].
                isAnimationFinished(AnimationGlobalTime.time() - animationTime)) {
            deleteBody();
            Game.objectsList.del(this);
        }

        if(!allowedMotion) {
            if(action != 3 &&
                    EntityManager.animations[entityID].get(action).get(faceDirection)[inLiquid ? 1: 0].
                            isAnimationFinished(AnimationGlobalTime.time() - animationTime)) {
                if(!isStoped) allowedMotion = true;
                action = 0;
                animationTime = AnimationGlobalTime.time();
            }
        }
    }
    public boolean canAct() {
        return (allowedMotion && !isStoped);
    }

    boolean isStoped = false;

    public void stopMovment() {
        isStoped = true;
        allowedMotion = false;
    }

    public void AllowMovment() {
        isStoped = false;
        allowedMotion = true;
    }

    public void getDamageWithoutMomentum(float damage, Vector2 hitPosition) {
        if (invincible) return;
        if(health.getHealth() > 0 && damage != 0) {
            health.getDamage(damage, hitPosition);
            GlobalSoundManager.addSound(new
                    SoundAtPosition(EntityManager.soundDamage[entityID], positionCenter()));
        }
        if(health.getHealth() <= 0) {
            die();
        }
    }
    protected Vector2 direction = new Vector2(0,0);
    public void getDamage(float damage, Vector2 AttackPosition, Vector2 hitPosition) {
        if (invincible) return;
        direction.set(positionCenter().x - AttackPosition.x + (float)Math.random() * 30 - 15,
                positionCenter().y - AttackPosition.y  + (float)Math.random() * 30 - 15);
        direction.nor();
        direction.scl(damage / health.getMaxHealth() * 100f);
        getDamageWithoutMomentum(damage, hitPosition);

        momentum.add(direction);
    }


    protected void changeAnimationsFor(Vector2 direction, int actionLocal) {
        if(!allowedMotion) return;
        action = actionLocal;
       if(direction != null && direction.len() != 0) {
           if (Math.abs(direction.x) > Math.abs(direction.y)) {
               if (direction.x < 0) faceDirection = 2;
               else if (direction.x > 0) faceDirection = 3;
           } else {
               if (direction.y < 0) faceDirection = 0;
               else if (direction.y > 0) faceDirection = 1;
           }
       }
       //System.out.println("action: " + action + " faceDirection: " + faceDirection);
    }
    public void running(boolean running) {
        if(!allowedMotion) return;
        isRunning = running;
    }
    protected Vector2 tempDirection = new Vector2(0,0);
    protected Vector2 tempDirection2 = new Vector2(0,0);

    public void tryMoveMomentum(float delta) {
        if(action == 3) return;
        momentum.scl((float)Math.pow(0.86f, delta * 60));
        nextMovement.add(momentum);
    }

   public boolean liquid() {
        return inLiquid;
   }

   public Vector2 nextMovement = new Vector2(0,0);
    public void tryMoveTo(Vector2 direction){
        if(!allowedMotion) { return; }
        if(isSpeedNegative()) direction.scl(-1);
        if(direction.len() != 0) {
            direction.nor();
            tempDirection.set(direction);
            tempDirection.scl(getSpeed());
            if(isSpeedNegative()) tempDirection.scl(-1);
            changeAnimationsFor(tempDirection, 1);
            if(isSpeedNegative()) tempDirection.scl(-1);
            nextMovement.set(nextMovement.x + tempDirection.x, nextMovement.y + tempDirection.y);
        } else {
            changeAnimationsFor(tempDirection, 0);
            tempDirection.scl(0);
        }
    };


    @Override protected void createBody() {
        if(CollisionBody == null) {
            CollisionBody = Game.collisionChecker.add(collisionBox(), new Vector2(0, 0));
        }
    }


    public void updatePosition(Vector2 direction) {
        position.add(direction);
        nextMovement.set(0,0);
    }

    public void renderShadow() {
        GlobalBatch.render(ShrubyWay.assetManager.get("effects/shadow.png", Texture.class), Math.round(positionLegs().x) - 80, Math.round(positionLegs().y) - 20);
    }
    @Override public void render() {

        if(health.timeAfterHit() < 0.2f) {
            effectColor.set(1, Math.min(effectColor.g, health.timeAfterHit() * 5f),
                    Math.min(effectColor.b, health.timeAfterHit() * 5f), 1);
        }
        GlobalBatch.batch.setColor(effectColor);

        float tempSpeed = speed / Math.abs(getSpeed());
        if(action != 1) tempSpeed = 1;

        EntityManager.animations[entityID].get(action).get(faceDirection)[inLiquid ? 1: 0].
                setFrameDuration(1f/(24f / tempSpeed));
        GlobalBatch.render(EntityManager.animations[entityID].get(action).get(faceDirection)[inLiquid ? 1: 0].
                        getKeyFrame(AnimationGlobalTime.time() - animationTime + loopedAnimationTime * (EntityManager.looping[entityID][action] ? 1:0), EntityManager.looping[entityID][action]),
                Math.round(position.x), Math.round(position.y) - (inLiquid ? -5 : 83));
        collisionBox().render();
        hitBox().render();

        effectColor.set(1, 1, 1, 1);
        GlobalBatch.batch.setColor(effectColor);
       if(inLiquid) renderWaterOverlay();
    }
    public float alpha = 1f;


    protected static Animation<TextureRegion> WaterOverlay;
    protected static float waterWidth = 180, waterHeight = 46;
    protected void renderWaterOverlay() {
          if(WaterOverlay == null) {

              WaterOverlay = Animator.toAnimation(
                      ShrubyWay.assetManager.get("effects/water_overlay.png", Texture.class),14, 0, 0);
              waterHeight = WaterOverlay.getKeyFrame(0).getRegionHeight();
              waterWidth = WaterOverlay.getKeyFrame(0).getRegionWidth();
          }
        if(this instanceof Mob)
            GlobalBatch.batch.setColor(1, 1, 1, alpha);

        GlobalBatch.render(WaterOverlay.getKeyFrame(AnimationGlobalTime.time(), true),
                positionCenter().x - 90 * waterOverlayScale, positionLegs().y - 23, waterWidth * waterOverlayScale, waterHeight);
        GlobalBatch.batch.setColor(1, 1, 1, 1);
    }

    public void addMomentum(Vector2 x) {
        momentum.add(x);
    }
    public Vector2 position() {
        return position;
    }

    public void die() {
     if(action == 3) return;
     GlobalSoundManager.addSound(new SoundAtPosition(EntityManager.soundDeath[entityID], positionCenter()));
     animationTime = AnimationGlobalTime.time();
     allowedMotion = false;
     action = 3;
    }

    public float attackCooldown = 0.5f;
    protected float animationTime = 0f;
    protected float lastAttackTime;


    public void attack(Vector2 direction, Vector2 relativePosition) {
        if(!allowedMotion && action != 2) return;
        float attackCooldown = 0.5f * ElementPumping.getAttackCooldownMultiplier(damageLevel);

        if((AnimationGlobalTime.time() - lastAttackTime) > attackCooldown) {
            Vector2 directionTemp =
                    new Vector2(direction.x - relativePosition.x, direction.y - relativePosition.y);
            changeAnimationsFor(directionTemp, 2);
            attacking = true;
            animationTime = AnimationGlobalTime.time();
            allowedMotion = false;
            action = 2;
            lastAttackTime = AnimationGlobalTime.time();
            soundAttack.play(SoundSettings.soundVolume);
        }
    }
    public float throwCooldown = 0.5f;
    protected float lastThrowTime;
    protected boolean attacking = false;

    public boolean canThrow() {
        if(!allowedMotion) return false;
        return (AnimationGlobalTime.time() - lastThrowTime >=
                throwCooldown * ElementPumping.getThrowCooldownMultiplier(throwLevel));
    }
    public void throwItem(Vector2 shootPosition, Item item, boolean rotating) {
        if(!canThrow()) return;
        lastThrowTime = AnimationGlobalTime.time();

        float damageAddition = ElementPumping.getThrowDamageAddition(ElementPumping.airLevel),
                speedScale = ElementPumping.getThrowSpeedMutiplier(throwLevel);

        Bullet bullet = new ThrowableItem(positionCenter(), shootPosition, item, this,
                rotating, damageAddition, speedScale);
        changeAnimationsFor(bullet.direction, 2);
        Game.objectsList.add(bullet);
    }

    @Override public Rectangle collisionBox(){return collisionBox;}
    @Override public Rectangle hitBox() {return hitBox;}

    protected boolean checkCollisions(){
        Rectangle temp = collisionBox();
        return Game.screenGrid.checkCollision(temp, this);
    };
    public void liquidStatus(boolean isLiquid) {
        inLiquid = isLiquid;
    }
    public void changePosition(Vector2 positionNew) {
        position.set(positionNew);
    }
    @Override public Vector2 positionCenter() {
        tempPosition.set(position.x + regionWidth / 2, position.y + regionHeight / 2);
        return tempPosition;
    }
    public Vector2 positionLegs() {
        tempPosition.set(position.x + regionWidth / 2, position.y);
        return tempPosition;
    }
    public boolean makingStep(char tile) {
        if(tile != lastTile) lastStepTime = 0f;
        lastTile = tile;
        if(action != 1) return false;
        if((AnimationGlobalTime.time() - lastStepTime)  >= stepCooldown / (getSpeed() / 10)) {
            lastStepTime = AnimationGlobalTime.time() + (float)Math.random() * 0.05f;
            return true;
        }
        return false;
    }

    @Override public void dispose() {
        super.dispose();
        health.dispose();
    }

}
