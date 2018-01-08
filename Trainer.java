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

        /* Recharges the entire party by 10 energy points. */

        for (Pokemon pokemon : pokeParty) {
            pokemon.recoverEnergy();
        }
    }

    public void HPRegain() {

        /* Recharges the entire party by 20 energy points. */

        for (Pokemon pokemon : pokeParty) {
            pokemon.recoverHP();
        }
    }

    // Battle Actions //

    public void pass() {

        /* Outputs a pass message. */

        PokeConsole.print(String.format("%s passes!\n", name), ConsoleColors.YELLOW_BOLD_BRIGHT, 2);
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
                break;
        }
    }

    public void act(Trainer other) {

        /* Performs the action the Trainer decided on previously. */

        switch (choice) {
            case Choices.ATTACK:
                PokeConsole.print(String.format("%s's %s uses %s!\n", name, active.getName(), active.getAttacks().get(atkChoice).getName()), ConsoleColors.RED_BOLD_BRIGHT, 2);
                active.attack(other.active, atkChoice);
                break;
            case Choices.RETREAT:
                setActive();
                PokeConsole.print(String.format("%s switches to %s!\n", name, active.getName()), ConsoleColors.BLACK_BOLD, 2);
                break;
            case Choices.PASS:
                pass();
                break;
        }
    }

    public void actionSequence() {

        /* Runs the usual action sequence (pick an action and prep it). */

        chooseAction();
        prepAction();
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

    /* A template for making Players, the playable Trainer that is used by the user. */

    // Fields are inherited

    public Player(String name) {

        /* Constructs and returns a new Player object. */

        // Just call the superclass's constructor to init fields
        super(name);
    }

    @Override public void makeParty(ArrayList<String> pokeStrings, int partySize) {

        /* Creates the Player's Pokemon party. */

        // Clear the old party out (when replaying games, this is important)
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
                PokeConsole.print("Your current Pokemon:", ConsoleColors.YELLOW_BOLD_BRIGHT, 2);
                for (int c : chosen) PokeConsole.print(" " + pokeNames.get(c), ConsoleColors.PURPLE_BOLD, 2);
                PokeConsole.print(String.format("\n%d pokemon left to pick.\n", partySize - chosen.size()), ConsoleColors.YELLOW_BOLD_BRIGHT, 2);
            }

            // Get a number from the Player, and then a name from the names list
            int choice = PokePrompt.numPrompt(pokeNames.size()) - 1;
            String chosenPoke = pokeNames.get(choice);

            // Make sure the Player didn't pick the same Pokemon twice
            if (chosen.contains(choice)) {
                PokeConsole.print(String.format("You already chose %s!\n", chosenPoke), ConsoleColors.RED_BOLD, 2);
                PokeConsole.clear();
                continue;
            } else {

                // Show Pokemon data
                PokeConsole.clear();
                PokeTextFormatter.displayPokeInfo(pokeStrings.get(choice), choice + 1);

                // Ask if they want to confirm this pokemon
                if (!PokePrompt.ynPrompt(chosenPoke, "y")) {
                    PokeConsole.clear();
                    continue;
                }

                // If confirmed, add the chosen index to the list
                chosen.add(choice);
                PokeConsole.print(String.format("%s selected!\n", chosenPoke), ConsoleColors.GREEN_BOLD_BRIGHT, 2);
            }

            PokePrompt.cnPrompt();
        } while (chosen.size() < partySize);  // while the Player still needs to pick pokemon

        // Sort the choices in reverse order of magnitude (largest to smallest)
        // This is to avoid all indexing issues with choices
        Collections.sort(chosen);
        Collections.reverse(chosen);

        // Add each chosen Pokemon to the party
        for (int c : chosen) pokeParty.add(new Pokemon(pokeStrings.remove(c)));
        Collections.reverse(pokeParty);

        PokeConsole.print("All Pokemon selected!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 2);
        PokePrompt.cnPrompt();
    }

    @Override public void chooseAction() {

        /* Prompts the Player to make a choice for the current round. */

        if (active.isStunned()) {  // if the active Pokemon is stunned, force a pass
            PokeConsole.print(String.format("%s is stunned. You are forced to pass!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 2);
            PokePrompt.cnPrompt();
            choice = Choices.PASS;
        } else if (!(active.canAttack() || canRetreat())) {  // if the active pokemon is unable to attack or retreat, force a pass
            PokeConsole.print("You cannot retreat and you have no energy! You are forced to pass!\n", ConsoleColors.RED_BOLD_BRIGHT, 2);
            choice = Choices.PASS;
            PokePrompt.cnPrompt();
        } else {  // no force is needed

            // Notify the Player if they can't attack or retreat

            if (!active.canAttack()) {
                PokeConsole.print(String.format("%s doesn't have enough energy for any attacks!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 2);
            }

            if (!canRetreat()) {
                PokeConsole.print(String.format("%s is your only Pokemon left. You can't retreat!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 2);
            }

            // Output options

            PokeConsole.print("What will you do?\n", ConsoleColors.GREEN_BOLD_BRIGHT, 2);

            while (true) {
                PokeConsole.print(active.toString() + "\n", 2);
                PokeConsole.print("╔═══╦═════════════════╗\n", ConsoleColors.BLACK_BOLD, 2);
                PokeConsole.print("║ 1 ║ Attack          ║\n", (active.canAttack() ? ConsoleColors.BLACK_BOLD : ConsoleColors.RED_BOLD), 2);
                PokeConsole.print("║ 2 ║ Retreat Pokemon ║\n", (canRetreat() ? ConsoleColors.BLACK_BOLD : ConsoleColors.RED_BOLD), 2);
                PokeConsole.print("║ 3 ║ Pass Turn       ║\n", ConsoleColors.BLACK_BOLD, 2);
                PokeConsole.print("╚═══╩═════════════════╝\n", ConsoleColors.BLACK_BOLD, 2);

                // Get input and make sure the choice can be made
                choice = PokePrompt.numPrompt(3);

                if (!active.canAttack() && choice == Choices.ATTACK || !canRetreat() && choice == Choices.RETREAT) {
                    PokeConsole.clear();
                    PokeConsole.print("You are unable to perform that action!\n", ConsoleColors.RED_BOLD, 2);
                    continue;
                }

                break;
            }
        }
        PokeConsole.clear();
    }

    @Override public void pickAttack() {

        /* Prompts the Player for their selection of attack. */

        // Integer to get input without immediately modifying atkChoice
        int newAtk;

        // Output options

        PokeConsole.print("What attack would you like to use?\n", ConsoleColors.BLUE, 2);
        PokeConsole.print(String.format("%s has %d energy left.\n", active.getName(), active.getEnergy()), ConsoleColors.CYAN_BOLD_BRIGHT, 2);

        PokeConsole.print("╔═══╦═════════════════════════════════════════════════════════╗\n", ConsoleColors.BLACK_BOLD, 0);
        for (Pokemon.Attack attack : active.getAttacks()) {
            PokeConsole.print(String.format("║ %d ║ ", active.getAttacks().indexOf(attack) + 1), ConsoleColors.BLACK_BOLD, 2);
            PokeConsole.print(attack.toString(), 2);
            PokeConsole.print(" ║\n", ConsoleColors.BLACK_BOLD, 20);
        }
        PokeConsole.print("╚═══╩═════════════════════════════════════════════════════════╝\n", ConsoleColors.BLACK_BOLD, 0);

        // Get Player input
        newAtk = PokePrompt.numPromptWithExit(active.getAttacks().size()) - 1;

        // Exit back to chooseAction() if the input was 0 (since we do input - 1, it checks for -1)
        if (newAtk == -1) {
            PokeConsole.clear();
            chooseAction();
            return;
        }

        // Change attack choice
        atkChoice = newAtk;
    }

    @Override public void retreat() {

        /* Executes Player Pokemon retreat protocol. */

        // If the Player has more than 1 option left
        if (pokeParty.size() > 1) {


            // Integer to get choice
            int newPoke;

            // While the Player still needs to make a choice
            while (true) {

                // Output options
                PokeConsole.print("What Pokemon would you like to use?\n", ConsoleColors.BLUE, 2);

                PokeConsole.print("╔═══╦═════════════════════════════════════════════════════════════════╗\n", ConsoleColors.BLACK_BOLD, 0);
                for (Pokemon pokemon : pokeParty) {
                    PokeConsole.print(String.format("║ %d ║ ", pokeParty.indexOf(pokemon) + 1), ConsoleColors.BLACK_BOLD, 2);
                    PokeConsole.print(pokemon.toString(), 2);
                    PokeConsole.print(" ║\n", ConsoleColors.BLACK_BOLD, 20);
                }
                PokeConsole.print("╚═══╩═════════════════════════════════════════════════════════════════╝\n", ConsoleColors.BLACK_BOLD, 2);

                // Get Player input
                if (hasActive()) newPoke = PokePrompt.numPromptWithExit(pokeParty.size()) - 1;  // if the Player doesn't have to retreat
                else newPoke = PokePrompt.numPrompt(pokeParty.size()) - 1;  // if the use must retreat (Pokemon just fainted)

                // Exit back to chooseAction() if the input was 0 (since we do input - 1, it checks for -1)
                if (newPoke == -1) {
                    PokeConsole.clear();
                    chooseAction();
                    return;
                }

                // If the Player selected the active Pokemon, let them know that they can't do that
                if (pokeParty.get(newPoke) == active) {
                    PokeConsole.clear();
                    PokeConsole.print("You already have that Pokemon out!\n", ConsoleColors.RED_BOLD_BRIGHT, 2);
                    continue;
                }

                // If the Player confirms their entry, exit out of loop
                if (PokePrompt.ynPrompt(pokeParty.get(newPoke).getName(), "y")) break;
            }

            // Set the Pokemon choice index to the input
            pokeChoice = newPoke;
        } else {  // Player has no choice for retreating
            PokeConsole.print(String.format("%s is the only Pokemon left!\n", pokeParty.get(0).getName()), ConsoleColors.RED_BOLD_BRIGHT, 2);
            pokeChoice = 0;
        }

        PokeConsole.print(String.format("%s, I choose you!\n", pokeParty.get(pokeChoice).getName()), ConsoleColors.BLACK_ITALICS, 2);
    }

    @Override public void prepAfterFaint() {

        /* Execute preparatory protocol after active Pokemon has fainted. */

        PokeConsole.print("Your last pokemon was knocked out. You are forced to pick a new pokemon!\n", ConsoleColors.RED_BOLD_BRIGHT, 2);
        retreat();
        setActive();
    }
}

class Opponent extends Trainer {

    /* A template for making Opponents, the computer-controlled Trainer. */

    // Fields are inherited

    public Opponent(String name) {

        /* Constructs and returns a new Opponent object. */

        // Just call the superclass's constructor to init fields
        super(name);
    }

    @Override public void makeParty(ArrayList<String> pokeStrings, int partySize) {

        /* Creates the Opponent's Pokemon party. */

        // Clear the old party out
        pokeParty.clear();

        // List to hold choices
        ArrayList<Integer> chosen = new ArrayList<>();

        // RNG for the random effect
        Random rng = new Random();

        PokeConsole.print(String.format("%s's turn to pick Pokemon!\n", name), ConsoleColors.PURPLE_BOLD, 20);

        do {

            // Get a number from the RNG
            int choice = rng.nextInt(pokeStrings.size());

            // If the Opponent has not picked this number yet
            if (!chosen.contains(choice)) {

                // Add the choice in
                chosen.add(choice);
                PokeConsole.print(String.format("Pokemon #%d selected!\n", chosen.size()), ConsoleColors.GREEN_BOLD_BRIGHT, 2);
            }
        } while (chosen.size() < partySize);  // while the Opponent still needs to pick pokemon

        // Sort the choices in reverse order of magnitude (largest to smallest)
        // This is to avoid all indexing issues with choices
        Collections.sort(chosen);
        Collections.reverse(chosen);

        // Add each chosen Pokemon to the party
        for (int c : chosen) pokeParty.add(new Pokemon(pokeStrings.remove(c)));
        Collections.reverse(pokeParty);

        PokeConsole.print("All Pokemon selected!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 2);
        PokePrompt.cnPrompt();
    }

    @Override public void chooseAction() {

        /* Prompts the Opponent to make a choice for the current round. */

        if (!hasActive())                                                       choice = Choices.RETREAT;   // their last pokemon was knocked out
        else if (active.isStunned() || !(active.canAttack() || canRetreat()))   choice = Choices.PASS;      // they can't attack or retreat
        else                                                                    choice = Choices.ATTACK;    // they're not in any special circumstance
    }

    @Override public void pickAttack() {

        /* Prompts the Opponent for their selection of attack. */

        // Just cycle through the attacks until one affordable attack is found
        for (Pokemon.Attack attack : active.getAttacks()) {
            if (attack.isAffordable()) {
                atkChoice = active.getAttacks().indexOf(attack);
            }
        }
    }

    @Override public void retreat() {

        /* Executes Opponent Pokemon retreat protocol. */

        // Pick a random Pokemon in the party
        pokeChoice = (new Random()).nextInt(pokeParty.size());
        PokeConsole.print(String.format("%s, I choose you!\n", pokeParty.get(pokeChoice).getName()), ConsoleColors.BLACK_ITALICS, 2);
    }

    @Override public void prepAfterFaint() {

        /* Execute preparatory protocol after active Pokemon has fainted. */

        retreat();
        setActive();
    }
}
