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
    private ArrayList<Attack> attacks;
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
        this.attacks = new ArrayList<>();
        for (int i = 0; i < numAttacks; i++) {
            attacks.add(new Attack(pkscn.next().toUpperCase(), pkscn.nextInt(), pkscn.nextInt(), pkscn.hasNext() ? pkscn.next().toUpperCase() : "none"));
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

    public int getEnergy() {
        return this.energy;
    }

    public void useEnergy(Attack attack) {
        energy = attack.cost;
    }

    public void recoverEnergy() {
        energy = (energy + 10 > 50 ? 50 : energy);
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

    public boolean canAttack() {
        for (Attack attack : attacks) {
            if (this.energy >= attack.cost) return true;
        }
        return false;
    }

    public boolean isAlive() { return this.hp > 0; }

    public void takeDamage(int damage) {
        this.hp = (hp - damage <= 0 ? 0 : hp - damage);
    }

    public void attack(Pokemon other, int atkNum) {
        other.takeDamage(this.attacks.get(atkNum).calculateDamage(other));
        this.attacks.get(atkNum).special.effect(other);
    }

    public String status() {
        StringBuilder bar = new StringBuilder()
            .append(PokeConsole.color(String.format("%-13s", name), ConsoleColors.CYAN_BOLD_BRIGHT))
            .append(PokeConsole.color("[", ConsoleColors.BLACK_BOLD));
        int fullPixels = (int) ((double) hp / hpCap) * 30;
        int emptyPixels = 30 - fullPixels;
        for (int i = 0; i < fullPixels; i ++) bar.append(PokeConsole.color("█", ConsoleColors.GREEN_BOLD_BRIGHT));
        for (int i = 0; i < emptyPixels; i ++) bar.append(PokeConsole.color("█", ConsoleColors.RED_BOLD_BRIGHT));
        bar
            .append(PokeConsole.color("]", ConsoleColors.BLACK_BOLD))
            .append(PokeConsole.color(String.format(" [%3d/%-3d] ", hp, hpCap), ConsoleColors.GREEN_BOLD))
            .append(PokeConsole.color(String.format("[%2d/50]", energy), ConsoleColors.PURPLE_BOLD_BRIGHT));

        return bar.toString();
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

        public void effect(Pokemon other) {
            this.special.effect(other);
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
                    break;
                case "WILD CARD":
                    this.effectChance = 1;
                    this.hitChance = .5;
                    this.loops = false;
                    break;
                case "WILD STORM":
                    this.effectChance = 1;
                    this.hitChance = .5;
                    this.loops = true;
                    break;
                case "RECHARGE":
                    this.effectChance = 1;
                    this.hitChance = 1;
                    this.loops = false;
                    break;
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
