/*
    PokeParts.java
    Syed Safwaan
    Classes that pertain to the player and his properties and possible actions. Every Pokemon Master needs his
    essential abilities.

    Classes:
    - Trainer           Contains all methods that Player and Opponent inherit (abstract parent of Player, Opponent)
    - Trainer.Choices   Contains final fields for use in decision-making
    - Player            For the user to play
    - Opponent          The computer that plays against the player
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

abstract class Trainer {

    /* A abstract class that the other classes in the file inherit from. */

    // Fields //

    String name;
    ArrayList<Pokemon> pokeParty;
    Pokemon active;
    int choice, atkChoice, pokeChoice;

    // Constructor //

    public Trainer(String name) {

        /* Constructs and returns a new Trainer object. */

        this.name = name;
        this.pokeParty = new ArrayList<>();
        this.active = null;
    }

    // Accessors //

    public String getName() {

        /* Returns the name of the Trainer as a String. */

        return this.name;
    }

    public ArrayList<Pokemon> getParty() {

        /* Returns a copy of the Trainer's party. */

        return new ArrayList<>(this.pokeParty);
    }

    public int getChoice() {

        /* Returns the Trainer's turn choice. */

        return this.choice;
    }

    // Status Checks //

    public boolean hasActive() {

        /* Returns whether the Trainer has a valid active Pokemon when called. */

        return this.pokeParty.contains(active);
    }

    public void setActive() {

        /* Sets the active Pokemon to one at a pre-established index in the Trainer's party. */

        active = pokeParty.get(pokeChoice);
    }

    public boolean canFight() {

        /* Returns whether the Trainer is able to continue fighting with their party. */

        for (Pokemon pokemon : pokeParty) if (!pokemon.fainted()) return true;
        return false;
    }

    public boolean canRetreat() {

        /* Returns whether the Trainer is able to issue a command to retreat. */

        for (Pokemon pokemon : pokeParty) if (pokemon != active && !pokemon.fainted()) return true;
        return false;
    }

    // Abilities //

    public void removePoke(Pokemon pokemon) {

        /* Removes a given Pokemon from the Trainer's party. */

        pokeParty.remove(pokemon);
    }

    public void energyRegain() {

        /* Recharges the entire party by 20 energy points. */

        for (Pokemon pokemon : pokeParty) {
            pokemon.recoverEnergy();
        }
    }

    // Battle Actions //

    public void pass() {

        /* Outputs a pass message. */

        PokeConsole.print(String.format("%s passes!\n", name), ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
    }

    public void prepAction() {

        /* Prepares the Trainer to perform the action they decided previously. */

        switch (choice) {
            case Choices.ATTACK:
                pickAttack();
                break;
            case Choices.RETREAT:
                retreat();
                break;
            case Choices.PASS:
                System.out.println(name + " selected pass.");
                break;
        }
    }

    public void act(Trainer other) {

        /* Performs the action the Trainer decided on previously. */

        switch (choice) {
            case Choices.ATTACK:
                PokeConsole.print(String.format("%s's %s uses %s!\n", name, active.getName(), active.getAttacks().get(atkChoice).getName()), ConsoleColors.RED_BOLD_BRIGHT, 10);
                active.attack(other.active, atkChoice);
                break;
            case Choices.RETREAT:
                setActive();
                PokeConsole.print(String.format("%s switches to %s!\n", name, active.getName()), ConsoleColors.BLACK_BOLD, 10);
                break;
            case Choices.PASS:
                pass();
                break;
        }
    }

    // Abstract Methods //

    // These methods differ between the Player and Opponent

    abstract public void makeParty(ArrayList<String> pokeStrings, int partySize);

    abstract public void chooseAction();

    abstract public void pickAttack();

    abstract public void retreat();

    abstract public void prepAfterFaint();

    protected class Choices {

        /* A collection of fields for use in decision making in battles. */

        // Each field as a value corresponding to a possible action
        public static final int ATTACK = 1;
        public static final int RETREAT = 2;
        public static final int PASS = 3;
    }
}

class Player extends Trainer {

    public Player(String name) { super(name); }

    @Override public void makeParty(ArrayList<String> pokeStrings, int partySize) {
        pokeParty.clear();

        // List of Pokemon names
        ArrayList<String> pokeNames = PokeMore.getPokeNames(pokeStrings);

        // List to hold choices
        ArrayList<Integer> chosen = new ArrayList<>();

        do {

            // Display all names
            PokeTextFormatter.displayPokeNames(pokeNames, 4);

            // Print already chosen pokemon if there are any
            if (!chosen.isEmpty()) {
                PokeConsole.print("Your current Pokemon:", ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
                for (int c : chosen) PokeConsole.print(" " +pokeNames.get(c - 1), ConsoleColors.PURPLE_BOLD, 10);
                PokeConsole.print(String.format("\n%d pokemon left to pick.\n", partySize - chosen.size()), ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
            }

            // Get a number from the user, and then a name from the names list
            int choice = PokePrompt.numPrompt(pokeNames.size());
            String chosenPoke = pokeNames.get(choice - 1);

            if (chosen.contains(choice)) {
                PokeConsole.print(String.format("You already chose %s!\n", chosenPoke), ConsoleColors.RED_BOLD, 10);
                PokeConsole.clear();
                continue;
            } else {
                PokeConsole.clear();
                PokeTextFormatter.displayPokeInfo(pokeStrings.get(choice - 1), choice);

                if (!PokePrompt.ynPrompt(chosenPoke, "y")) {
                    PokeConsole.clear();
                    continue;
                }

                chosen.add(choice);
                PokeConsole.print(String.format("%s selected!\n", chosenPoke), ConsoleColors.GREEN_BOLD_BRIGHT, 10);
            }

            PokePrompt.cnPrompt();
        } while (chosen.size() < partySize);

        Collections.sort(chosen);
        Collections.reverse(chosen);
        for (int c : chosen) pokeParty.add(new Pokemon(pokeStrings.remove(c - 1)));
        Collections.reverse(pokeParty);

        PokeConsole.print("All Pokemon selected!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
        PokePrompt.cnPrompt();
    }

    @Override public void chooseAction() {
        if (active.isStunned()) {
            PokeConsole.print(String.format("%s is stunned. You are forced to pass!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 10);
            PokePrompt.cnPrompt();
            choice = Choices.PASS;
        } else if (!(active.canAttack() || canRetreat())) {
            PokeConsole.print("You cannot retreat and you have no energy! You are forced to pass!\n", ConsoleColors.RED_BOLD_BRIGHT, 10);
            choice = Choices.PASS;
            PokePrompt.cnPrompt();
        } else {
            if (!active.canAttack()) {
                PokeConsole.print(String.format("%s doesn't have enough energy for any attacks!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 10);
            }

            if (!canRetreat()) {
                PokeConsole.print(String.format("%s is your only Pokemon left. You can't retreat!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 10);
            }

            PokeConsole.print("What will you do?\n", ConsoleColors.GREEN_BOLD_BRIGHT, 10);

            while (true) {
                PokeConsole.print(active.toString() + "\n", 10);
                PokeConsole.print("╔═══╦═════════════════╗\n", ConsoleColors.BLACK_BOLD, 10);
                PokeConsole.print("║ 1 ║ Attack          ║\n", (active.canAttack() ? ConsoleColors.BLACK_BOLD : ConsoleColors.RED_BOLD), 10);
                PokeConsole.print("║ 2 ║ Retreat Pokemon ║\n", (canRetreat() ? ConsoleColors.BLACK_BOLD : ConsoleColors.RED_BOLD), 10);
                PokeConsole.print("║ 3 ║ Pass Turn       ║\n", ConsoleColors.BLACK_BOLD, 10);
                PokeConsole.print("╚═══╩═════════════════╝\n", ConsoleColors.BLACK_BOLD, 10);

                choice = PokePrompt.numPrompt(3);

                if (!active.canAttack() && choice == Choices.ATTACK || !canRetreat() && choice == Choices.RETREAT) {
                    PokeConsole.clear();
                    PokeConsole.print("You are unable to perform that action!\n", ConsoleColors.RED_BOLD, 10);
                    continue;
                }

                break;
            }
        }
        PokeConsole.clear();
    }

    @Override public void retreat() {
        PokeConsole.print("What Pokemon would you like to use?\n", ConsoleColors.BLUE, 10);

        int newPoke;
        while (true) {
            PokeConsole.print("╔═══╦═════════════════════════════════════════════════════════════════╗\n", ConsoleColors.BLACK_BOLD, 0);
            for (Pokemon pokemon : pokeParty) {
                PokeConsole.print(String.format("║ %d ║ ", pokeParty.indexOf(pokemon) + 1), ConsoleColors.BLACK_BOLD, 10);
                PokeConsole.print(pokemon.toString(), 10);
                PokeConsole.print(" ║\n", ConsoleColors.BLACK_BOLD, 20);
            }
            PokeConsole.print("╚═══╩═════════════════════════════════════════════════════════════════╝\n", ConsoleColors.BLACK_BOLD, 10);

            if (hasActive()) newPoke = PokePrompt.numPromptWithExit(pokeParty.size()) - 1;
            else newPoke = PokePrompt.numPrompt(pokeParty.size()) - 1;

            if (newPoke == -1) {
                PokeConsole.clear();
                chooseAction();
                return;
            }

            if (pokeParty.get(newPoke) == active) {
                PokeConsole.clear();
                PokeConsole.print("You already have that Pokemon out!\n", ConsoleColors.RED_BOLD_BRIGHT, 10);
                continue;
            }


            if (PokePrompt.ynPrompt(pokeParty.get(newPoke).getName(), "y")) break;
        }
        pokeChoice = newPoke;
        PokeConsole.print(String.format("%s, I choose you!\n", pokeParty.get(pokeChoice).getName()), ConsoleColors.BLACK_ITALICS, 10);
    }

    @Override public void prepAfterFaint() {
        PokeConsole.print("Your last pokemon was knocked out. You are forced to pick a new pokemon!\n", ConsoleColors.RED_BOLD_BRIGHT, 10);
        retreat();
        setActive();
        chooseAction();
    }

    @Override public void pickAttack() {
        PokeConsole.print("What attack would you like to use?\n", ConsoleColors.BLUE, 10);
        PokeConsole.print(String.format("%s has %d energy left.\n", active.getName(), active.getEnergy()), ConsoleColors.CYAN_BOLD_BRIGHT, 10);

        PokeConsole.print("╔═══╦═════════════════════════════════════════════════════════╗\n", ConsoleColors.BLACK_BOLD, 0);
        for (Pokemon.Attack attack : active.getAttacks()) {
            PokeConsole.print(String.format("║ %d ║ ", active.getAttacks().indexOf(attack) + 1), ConsoleColors.BLACK_BOLD, 10);
            PokeConsole.print(attack.toString(), 10);
            PokeConsole.print(" ║\n", ConsoleColors.BLACK_BOLD, 20);
        }
        PokeConsole.print("╚═══╩═════════════════════════════════════════════════════════╝\n", ConsoleColors.BLACK_BOLD, 0);

        atkChoice = PokePrompt.numPrompt(active.getAttacks().size()) - 1;
    }
}

class Opponent extends Trainer {

    public Opponent(String name) { super(name); }

    @Override public void makeParty(ArrayList<String> pokeStrings, int partySize) {
        pokeParty.clear();

        ArrayList<Integer> chosen = new ArrayList<>();
        Random rng = new Random();

        PokeConsole.print(String.format("%s's turn to pick Pokemon!\n", name), ConsoleColors.PURPLE_BOLD, 20);

        do {
            int choice = rng.nextInt(pokeStrings.size());
            if (!chosen.contains(choice)) {
                chosen.add(choice);
                PokeConsole.print(String.format("Pokemon #%d selected!\n", chosen.size()), ConsoleColors.GREEN_BOLD_BRIGHT, 10);
            }
        } while (chosen.size() < partySize);

        Collections.sort(chosen);
        Collections.reverse(chosen);
        for (int c : chosen) pokeParty.add(new Pokemon(pokeStrings.remove(c - 1)));
        Collections.reverse(pokeParty);

        PokeConsole.print("All Pokemon selected!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
        PokePrompt.cnPrompt();
    }

    @Override public void chooseAction() {
        if (!hasActive())                                                       choice = Choices.RETREAT;
        else if (active.isStunned() || !(active.canAttack() || canRetreat()))   choice = Choices.PASS;
        else                                                                    choice = Choices.ATTACK;
    }

    @Override public void retreat() {
        pokeChoice = 0;
        PokeConsole.print(String.format("%s, I choose you!\n", pokeParty.get(pokeChoice).getName()), ConsoleColors.BLACK_ITALICS, 10);
    }

    @Override public void prepAfterFaint() {
        retreat();
        setActive();
        chooseAction();
        prepAction();
    }

    @Override public void pickAttack() {
        for (Pokemon.Attack attack : active.getAttacks()) {
            if (attack.isAffordable()) {
                atkChoice = active.getAttacks().indexOf(attack);
            }
        }
    }
}
