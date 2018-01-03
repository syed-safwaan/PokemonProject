/*
    PokeParts.java
    Syed Safwaan
    A collection of classes for the parts of Pokemon, from the attacks to the actual Pokemon.

    Classes:
    - Pokemon
    - Attack (Inner class of Pokemon)
    - Special (Inner class of Pokemon)
*/

import java.lang.*;
import java.util.*;

class Pokemon {

    private String name, type, resistance, weakness;
    private int hp, hpCap, energy;
    private Attack[] attacks;
    private boolean disabled, stunned;

    public Pokemon(String pokeData) {
        Scanner pkscn = new Scanner(pokeData);
        pkscn.useDelimiter(",");

        this.name = pkscn.next();
        this.hp = pkscn.nextInt();
        this.hpCap = this.hp;
        this.energy = 50;
        this.type = pkscn.next().trim().toUpperCase();
        this.resistance = pkscn.next().trim().toUpperCase();
        this.weakness = pkscn.next().trim().toUpperCase();

        int numAttacks = pkscn.nextInt();
        this.attacks = new Attack[numAttacks];
        for (int i = 0; i < numAttacks; i++) {
            attacks[i] = new Attack(pkscn.next().toUpperCase(), pkscn.nextInt(), pkscn.nextInt(), pkscn.next().toUpperCase());
        }

        disabled = false;
        stunned = false;

        pkscn.close();
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getResistance() {
        return this.resistance;
    }

    public String getWeakness() {
        return this.weakness;
    }

    public int getHP() {
        return this.hp;
    }

    public int getHPCap() {
        return this.hpCap;
    }

    public Attack[] getAttacks() {
        return this.attacks;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void disable() {
        disabled = true;
    }

    public boolean isStunned() {
        return this.stunned;
    }

    public void stun() {
        stunned = true;
    }

    public void unstun() {
        stunned = false;
    }

    class Attack {
        private String name;
        private int cost, damage;
        private Special special;

        private Attack(String name, int cost, int damage, String specialName) {
            this.name = name;
            this.cost = cost;
            this.damage = damage;
            this.special = new Special(specialName);
        }

        public String getName() {
            return this.name;
        }

        public int getCost() {
            return this.cost;
        }

        public int getDamage() {
            return this.damage;
        }

        public Special getSpecial() {
            return this.special;
        }

        public boolean isAffordable() {
            return Pokemon.this.energy >= this.cost;
        }

        public int calculateDamage(Pokemon other) {
            return this.damage
                    * (Pokemon.this.type.equals(other.getWeakness()) ? 2 : 1)
                    / (Pokemon.this.type.equals(other.getResistance()) ? 2 : 1)
                    - (Pokemon.this.disabled ? 10 : 0);
        }
    }

    private class Special {

        private String name;
        private double effectChance, hitChance;
        private boolean loops;

        private Special(String name) {
            this.name = name;
            switch (name) {
                case "STUN":
                    this.effectChance = .5;
                    this.hitChance = 1;
                    this.loops = false;
                    break;
                case "DISABLE":
                    this.effectChance = 1;
                    this.hitChance = 1;
                    this.loops = false;
                case "WILD CARD":
                    this.effectChance = 1;
                    this.hitChance = .5;
                    this.loops = false;
                case "WILD STORM":
                    this.effectChance = 1;
                    this.hitChance = .5;
                    this.loops = true;
                case "RECHARGE":
                    this.effectChance = 1;
                    this.hitChance = 1;
                    this.loops = false;
            }
        }

        public String getName() {
            return this.name;
        }

        public double getEffectChance() {
            return this.effectChance;
        }

        public double getHitChance() {
            return this.hitChance;
        }

        public boolean loops() {
            return this.loops;
        }

        public void effect(Pokemon other) {
            switch (this.name) {
                case "STUN":
                    if (Math.random() < this.effectChance) other.stun();
                case "DISABLE":
                    other.disable();
                case "WILD CARD":
                    // nothing ...
                case "WILD STORM":
                    // nothing ...
                case "RECHARGE":
                    Pokemon.this.hp = (hp + 20 > hpCap ? hpCap : hp + 20);
            }
        }
    }
}
