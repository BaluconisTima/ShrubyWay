package com.shrubyway.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.TimeUtils;
import com.shrubyway.game.animation.AnimationGlobalTime;

public class Health {
    private float health;
    private float maxHealth;
    private float cooldown;
    private float lastHitTime;

    private float lastHealTime;

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

    public void getDamage(float damage) {
        if(AnimationGlobalTime.time() - lastHitTime < cooldown) return;
            if(health <= 0) return;
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
}
