package com.shrubyway.game;

import com.badlogic.gdx.utils.TimeUtils;

public class Health {
    private float health;
    private float maxHealth;
    private float cooldown;
    private float lastHitTime;

    public Health(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.cooldown = 0;
        this.lastHitTime = 0;
    }
    public Health(float maxHealth, float cooldown) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.cooldown = cooldown;
        this.lastHitTime = 0;
    }

    public void getDamage(float damage) {
        if(TimeUtils.millis() - lastHitTime >= cooldown) {
            health -= damage;
            lastHitTime = TimeUtils.millis();
        }
    }
    public void heal(float heal) {
        health += heal;
        if(health > maxHealth) health = maxHealth;
    }
    public float getHealth() {
        return health;
    }
    public float getMaxHealth() {
        return maxHealth;
    }
    public float timeAfterHit() {
        return TimeUtils.millis() - lastHitTime;
    }
}
