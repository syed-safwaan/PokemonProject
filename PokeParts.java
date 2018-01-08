/*
    PokeParts.java
    Syed Safwaan
    A collection of classes for the parts of Pokemon, from the attacks to the actual Pokemon.

    Classes:
    - Pokemon           Pokemon attributes and abilities
    - Pokemon.Attack    Pokemon attack details and abilities
    - Pokemon.Special   Pokemon attack specials details and abilities
*/

import java.lang.*;
import java.util.*;

class Pokemon {

    /* A template for constructing Pokemon objects, including Attack and Special inner classes. */

    // Fields //

    private String name, type, resistance, weakness;
    private int hp, hpCap, energy;
    private ArrayList<Attack> attacks;
    private boolean disabled, stunned;

    // Constructor //

    public Pokemon(String pokeData) {

        /* Constructs and returns a new Pokemon object given a String of data. */

        // Scanner to read data string (delimiter or token set to comma)
        Scanner pkscn = new Scanner(pokeData);
        pkscn.useDelimiter(",");

        // Read the fields one by one and modify where needed
        this.name = pkscn.next();
        this.hp = pkscn.nextInt();
        this.hpCap = this.hp;
        this.energy = 50;
        this.type = pkscn.next().trim().toUpperCase();
        this.resistance = pkscn.next().trim().toUpperCase();
        this.weakness = pkscn.next().trim().toUpperCase();

        // Reading attacks

        // Get the number of attacks (useful for getting number of attacks)
        int numAttacks = pkscn.nextInt();
        this.attacks = new ArrayList<>();
        for (int i = 0; i < numAttacks; i++) {

            // Add a new Attack Object for every set of attack data
            attacks.add(new Attack(pkscn.next().toUpperCase(), pkscn.nextInt(), pkscn.nextInt(), pkscn.hasNext() ? pkscn.next().toUpperCase() : "NONE"));
        }

        // Set all toString modifiers to false
        this.disabled = false;
        this.stunned = false;

        pkscn.close();
    }

    // Accessors //

    public String getName() {

        /* Returns the Pokemon's name as a String. */

        return this.name;
    }

    public String getType() {

        /* Returns the Pokemon's type as a String. */

        return this.type;
    }

    public String getResistance() {

        /* Returns the Pokemon's resistance as a String. */

        return this.resistance;
    }

    public String getWeakness() {

        /* Returns the Pokemon's weakness as a String. */

        return this.weakness;
    }

    public int getHP() {

        /* Returns the Pokemon's current HP. */

        return this.hp;
    }

    public int getHPCap() {

        /* Returns the Pokemon's HP cap (maximum HP possible). */

        return this.hpCap;
    }

    public void takeDamage(int damage) {

        /* Subtracts from Pokemon HP given a damage value. */

        this.hp = Math.max(0, hp - damage);
    }

    public void recoverHP() {

        /*
         * Adds 20 to the Pokemon's HP.
         * Overload #2
         */

        this.hp = Math.min(hpCap, hp + 20);
    }

    public void recoverHP(int val) {

        /*
         * Adds a given value to the Pokemon's HP.
         * Overload #2
         */

        PokeConsole.print(String.format("%s regained %d HP!\n", name, val), ConsoleColors.PURPLE_BOLD, 2);
        this.hp = Math.min(hpCap, hp + val);
    }

    public int getEnergy() {

        /* Returns the Pokemon's energy level. */

        return this.energy;
    }

    public void useEnergy(Attack attack) {

        /* Subtracts from the Pokemon's energy level given an Attack object. */

        this.energy = Math.max(0, energy - attack.cost);
    }

    public void recoverEnergy() {

        /*
         * Adds to the Pokemon's energy level by 10.
         * Overload #1
         */

        this.energy = Math.min(50, energy + 10);
    }

    public void recoverEnergy(int val) {

        /*
         * Adds to the Pokemon's energy level by a given value.
         * Overload #2
         */

        PokeConsole.print(String.format("%s regained %d energy!\n", name, val), ConsoleColors.PURPLE_BOLD, 2);
        this.energy = Math.min(50, energy + val);
    }

    public boolean isDisabled() {

        /* Returns whether the Pokemon is disabled or not. */

        return this.disabled;
    }

    public void disable() {

        /* Disables the Pokemon. */

        PokeConsole.print(String.format("%s is disabled!\n", name), ConsoleColors.PURPLE_BOLD, 2);
        this.disabled = true;
    }

    public boolean isStunned() {

        /* Returns whether the Pokemon is stunned or not. */

        return this.stunned;
    }

    public void stun() {

        /* Stuns the Pokemon. */

        PokeConsole.print(String.format("%s is stunned!\n", name), ConsoleColors.PURPLE_BOLD, 2);
        this.stunned = true;
    }

    public void unstun() {

        /* Unstuns the Pokemon. */

        PokeConsole.print(String.format("%s is no longer stunned!\n", name), ConsoleColors.PURPLE_BOLD, 2);
        this.stunned = false;
    }

    public ArrayList<Attack> getAttacks() {

        /* Returns a copy of the Attack ArrayList. */

        return new ArrayList<>(this.attacks);
    }

    // Status Checks //

    public boolean canAttack() {

        /* Returns whether the Pokemon is able to attack at all. */

        for (Attack attack : this.attacks) if (attack.isAffordable()) return true;
        return false;
    }

    public boolean fainted() {

        /* Returns the biological toString of the Pokemon. */

        return this.hp <= 0;
    }

    // Actions //

    public void attack(Pokemon other, int atkNum) {

        /* Administers an attack against another Pokemon, given the other Pokemon and the index of the chosen attack. */

        // Get the attack and its base damage after type and toString checking
        Attack atk = this.attacks.get(atkNum);
        int dmg = atk.calculateDamage(other);

        // If the attack loops
        if (atk.special.loops) {

            // Hit counter
            int hits = 0;

            // Keep attacking while the RNG allows and the other Pokemon is able to take the damage
            while (Math.random() < atk.special.hitChance && hits * dmg <= other.getHP()) {
                PokeConsole.print(String.format("%d! ", ++ hits), ConsoleColors.PURPLE_BOLD, 2);
            }

            // Damage multiplier
            dmg *= hits;

            // Administer damage based on number of hits
            if (hits == 0) PokeConsole.print("No hits...\n", ConsoleColors.BLUE_BOLD, 2);
            else {
                System.out.println();
                other.takeDamage(dmg);
                atk.special.effect(other);
            }

        } else {  // attack does not loop

            // If the RNG allows it, land a hit
            if (Math.random() < atk.special.hitChance) {
                other.takeDamage(dmg);
                atk.special.effect(other);
            } else {
                PokeConsole.print("No hits...\n", ConsoleColors.BLUE_BOLD, 2);
                dmg = 0;
            }
        }

        useEnergy(atk);

        // Output final results and attack success toString
        PokeConsole.print(String.format("%s deals %d damage!\n", name, dmg), ConsoleColors.RED_BOLD_BRIGHT, 2);
        if (dmg != 0) {
            if (atk.isSuperEffective(other)) PokeConsole.print("It's super effective!\n", ConsoleColors.GREEN_BOLD_BRIGHT, 2);
            else if (atk.isNotEffective(other)) PokeConsole.print("It's not very effective...\n", ConsoleColors.BLUE_BOLD_BRIGHT, 2);
        }
    }

    @Override public String toString() {

        /* Returns a String representation of the Pokemon's current status. */

        StringBuilder bar = new StringBuilder()
            .append(PokeConsole.colour(String.format("%-13s", name), ConsoleColors.CYAN_BOLD_BRIGHT))  // name
            .append(PokeConsole.colour("[", ConsoleColors.BLACK_BOLD));
        int fullPixels = (int) ((double) hp / hpCap * 30);  // HP left
        int emptyPixels = 30 - fullPixels;                  // HP taken
        for (int i = 0; i < fullPixels; i ++) bar.append(PokeConsole.colour("█", ConsoleColors.GREEN_BOLD_BRIGHT));
        for (int i = 0; i < emptyPixels; i ++) bar.append(PokeConsole.colour("▒", ConsoleColors.RED_BOLD_BRIGHT));
        bar
            .append(PokeConsole.colour("]", ConsoleColors.BLACK_BOLD))
            .append(PokeConsole.colour(String.format(" [%3d/%-3d] ", hp, hpCap), ConsoleColors.GREEN_BOLD))      // HP
            .append(PokeConsole.colour(String.format("[%2d/50]", energy), ConsoleColors.PURPLE_BOLD_BRIGHT));    // energy

        return bar.toString();
    }

    class Attack {

        /* A template for constructing Attack objects for use by the surrounding Pokemon class. */

        // Fields //

        private String name;
        private int cost, damage;
        private Special special;

        // Constructor //

        private Attack(String name, int cost, int damage, String specialName) {

            /* Constructs and returns a new Attack object given certain values. */

            this.name = name;
            this.cost = cost;
            this.damage = damage;
            this.special = new Special(specialName);
        }

        // Accessors //

        public String getName() {

            /* Returns the Attack name as a String. */

            return this.name;
        }

        public int getCost() {

            /* Returns the Attack energy cost. */

            return this.cost;
        }

        public int getDamage() {

            /* Returns the Attack's base damage. */

            return this.damage;
        }

        public Special getSpecial() {

            /* Returns the Special object field of the Attack. */

            return this.special;
        }

        // Property Checks //

        public boolean isAffordable() {

            /* Returns whether the Pokemon can afford to use the Attack or not. */

            return Pokemon.this.energy >= this.cost;
        }

        public boolean isSuperEffective(Pokemon other) {

            /* Returns whether the Attack is supereffective against a given Pokemon or not. */

            return Pokemon.this.type.equals(other.getWeakness());
        }

        public boolean isNotEffective(Pokemon other) {

            /* Returns whether the Attack is ineffective against a given Pokemon or not. */

            return Pokemon.this.type.equals(other.getResistance());
        }

        public int calculateDamage(Pokemon other) {

            /* Returns the final damage of an Attack, given a Pokemon to attack. */

            int dmg = this.damage                                               // base
                * (Pokemon.this.type.equals(other.getWeakness()) ? 2 : 1)       // supereffective
                / (Pokemon.this.type.equals(other.getResistance()) ? 2 : 1)     // ineffective
                - (Pokemon.this.disabled ? 10 : 0);                             // disability deduction

            // Safeguard against giving negative damage
            return Math.max(0, dmg);
        }

        public void effect(Pokemon other) {

            /* Applies the Special effect on a given Pokemon. */

            this.special.effect(other);
        }

        @Override public String toString() {

            /* Returns a String representation of the Attack's details. */

            return
                PokeConsole.colour(String.format("%-16s", name), ConsoleColors.CYAN_BOLD_BRIGHT) +   // name
                PokeConsole.colour(String.format(" DMG: %-3d", damage), ConsoleColors.RED_BOLD) +    // base damage
                PokeConsole.colour(String.format(" COST: %-3d", cost), ConsoleColors.BLUE_BOLD) +    // energy cost
                PokeConsole.colour(String.format(" SPECIAL: %-10s", special.name), ConsoleColors.PURPLE_BOLD_BRIGHT);    // special
        }
    }

    private class Special {

        /* A template for making Special objects for use by its neighbouring Attack class and surrounding Pokemon class. */

        // Fields //

        private String name;
        private double effectChance, hitChance;  // chance of activating effect, chance of landing hit
        private boolean loops;  // is the attack a loop (eg. wild storm)

        // Constructor //

        private Special(String name) {

            /* Constructs and returns a new Special object, depending on a given name. */

            // if the name string is empty, set it to "NONE"
            this.name = (name.trim().isEmpty() ? "NONE" : name);

            // Switch block to assign values to fields
            switch (this.name) {
                case "NONE":
                    this.effectChance = 1;
                    this.hitChance = 1;
                    this.loops = false;
                    break;
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
                case "HEAL":
                    this.effectChance = 1;
                    this.hitChance = .5;
                    this.loops = true;
                default:
                    this.effectChance = 1;
                    this.hitChance = 1;
                    this.loops = false;
                    break;
            }
        }

        // Accessors //

        public String getName() {

            /* Returns the Special's name as a String. */

            return this.name;
        }

        public double getEffectChance() {

            /* Returns the Special's effect chance. */

            return this.effectChance;
        }

        public double getHitChance() {

            /* Returns the Special's hit chance. */

            return this.hitChance;
        }

        public boolean loops() {

            /* Returns whether the Special enabled looping attacks or not. */

            return this.loops;
        }

        public void effect(Pokemon other) {

            /* Administers effect on a given Pokemon. */

            switch (this.name) {
                case "STUN":
                    if (Math.random() < this.effectChance) other.stun();
                    break;
                case "DISABLE":
                    other.disable();
                    break;
                case "WILD CARD":
                    // nothing ...
                    break;
                case "WILD STORM":
                    // nothing ...
                    break;
                case "RECHARGE":
                    Pokemon.this.recoverEnergy(20);
                    break;
                case "HEAL":
                    Pokemon.this.recoverHP(20);
                    break;
                default:
                    break;
            }
        }
    }
}
