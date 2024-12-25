package com.shrubyway.game;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.effect.DamageDisplay;

public class Health implements java.io.Serializable {
    private float health;
    private float maxHealth;
    private float cooldown;
    private float lastHitTime;

    private float lastHealTime;

    public Integer defenseLevel = null;

    public Health(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.cooldown = 0;
        this.lastHitTime = -1000;
    }

    public Health(float maxHealth, float cooldown) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.cooldown = cooldown;
        this.lastHitTime = -1000;
    }

    public void getDamage(float damage, Vector2 hitPosition) {
        if(AnimationGlobalTime.time() - lastHitTime < cooldown) return;
            if(health <= 0) return;

            if(defenseLevel != null) {
                damage = damage * ElementPumping.getDefenseMultiplier(defenseLevel);
            }
            Game.objectsList.add(new DamageDisplay(hitPosition.x, hitPosition.y, damage));
            health -= damage;
            lastHitTime = AnimationGlobalTime.time();
    }
    public float lastHitTime() {
        return lastHitTime;
    }
    public void heal(float heal) {
        health += heal;
        lastHealTime = AnimationGlobalTime.time();
        if(health > maxHealth) health = maxHealth;
    }

    public void changeHealth(float health) {
        this.health = health;
    }
    public void setHealth(Health health) {
        this.health = health.getHealth();
        this.maxHealth = health.getMaxHealth();
        this.cooldown = health.cooldown;
    }
    public float getHealth() {
        return health;
    }
    public float getMaxHealth() {
        return maxHealth;
    }
    public float timeAfterHit() {
        return AnimationGlobalTime.time() - lastHitTime;
    }
    public float timeAfterHeal() {
        return AnimationGlobalTime.time() - lastHealTime;
    }

    public void dispose() {
        health = 0;
        maxHealth = 0;
        cooldown = 0;
        lastHitTime = 0;
        lastHealTime = 0;
    }
}
