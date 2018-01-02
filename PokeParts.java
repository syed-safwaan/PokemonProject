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
        for (int i = 0; i < numAttacks; i ++) {
            attacks[i] = new Attack(pkscn.next().toUpperCase(), pkscn.nextInt(), pkscn.nextInt(), pkscn.next().toUpperCase());
        }

        disabled = false;
        stunned = false;

        pkscn.close();
    }

    public String getName() { return this.name; }

    public String getType() { return this.type; }

    public String getResistance() { return this.resistance; }

    public String getWeakness() { return this.weakness; }

    public int getHP() { return this.hp; }

    public int getHPCap() { return this.hpCap; }

    public Attack[] getAttacks() { return this.attacks; }

    public boolean isDisabled() { return this.disabled; }

    public void disable() { disabled = true; }

    public boolean isStunned() { return this.stunned; }

    public void setStun(boolean cond) { stunned = cond; }

    @Override public String toString() {
        return String.format("NAME: %s - TYPE: %s - RESISTANCE: %s - WEAKNESS: %s - HP: %d - ENERGY: %d - ATTACKS: %s", this.name, this.type, this.resistance, this.weakness, this.hp, this.energy, Arrays.asList(attacks).toString());
    }

    class Attack {
        private String name;
        private int cost, damage;
        private Special special;

        public Attack(String name, int cost, int damage, String specialName) {
            this.name = name;
            this.cost = cost;
            this.damage = damage;

        }

        public String getName() { return this.name; }

        public int getCost() { return this.cost; }

        public int getDamage() { return this.damage; }

        public Special getSpecial() { return this.special; }

        public boolean isAffordable() {
            return Pokemon.this.energy - this.cost >= 0;
        }

        public int calculateDamage(Pokemon other) {
            return this.damage
                    * (Pokemon.this.type.equals(other.getWeakness()) ? 2 : 1)
                    / (Pokemon.this.type.equals(other.getResistance()) ? 2 : 1)
                    - (Pokemon.this.disabled ? 10 : 0);
        }

        @Override public String toString() {
            return String.format("NAME: %s - COST: %d - DAMAGE: %d - SPECIAL: %s", this.name, this.cost, this.damage, this.special);
        }
    }

    abstract class Special {
        protected String name, desc;
        protected double chance = 1;
        protected boolean loop = false;

        abstract void effect(Pokemon other);

        public String getName() { return this.name; }

        @Override public String toString() {
            return name + desc;
        }
    }

    class Stun extends Special {
        private String name = "STUN", desc = "";

        public void effect(Pokemon other) {
            if (Math.random() < .5) {
                other.setStun(true);
            }
        }
    }

    class Disable extends Special {
        private String name = "DISABLE", desc = "";

        public void effect(Pokemon other) {
            other.disable();
        }
    }

    class WildCard extends Special {
        private String name = "WILD CARD", desc = "";
        private double chance = .5;

        public void effect(Pokemon other) {
            // none
        }
    }

    class WildStorm extends Special {
        private String name = "WILD STORM", desc = "";
        private double chance = .5;
        private boolean loop = true;

        public void effect(Pokemon other) {
            // none
        }
    }

    class Recharge extends Special {
        private String name = "RECHARGE", desc = "";
        private double chance = 1;

        public void effect(Pokemon other) {
            Pokemon.this.hp = (Pokemon.this.hp + 20 > Pokemon.this.hpCap) ? hpCap : hp + 20;
        }
    }
}


