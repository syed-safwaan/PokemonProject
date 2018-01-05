/*
    PokeParts.java
    Syed Safwaan
    Classes that pertain to the player and his properties and possible actions. Every Pokemon Master needs his
    essential abilities.
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

abstract class Trainer {
    protected String name;
    protected ArrayList<Pokemon> pokeParty;
    protected Pokemon active;


    public Trainer(String name, ArrayList<String> pokeStrings, int partySize) {
        this.name = name;
        this.pokeParty = new ArrayList<>();
        this.makeParty(pokeStrings, partySize);
    }

    public String getName() { return this.name; }

    public ArrayList<Pokemon> getParty() { return this.pokeParty; }

    public boolean canFight() {
        for (Pokemon pokemon : pokeParty) {
            if (pokemon.isAlive()) return true;
        }
        return false;
    }

    abstract public void makeParty(ArrayList<String> pokeStrings, int partySize);

    abstract public int chooseAction();

    public void pass() {
        PokeConsole.print(String.format("%s passes!", name), ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
    }

    abstract public void retreat();

    abstract public void attack(Trainer other);
}

class Player extends Trainer {

    public Player(String name, ArrayList<String> pokeStrings, int partySize) { super(name, pokeStrings, partySize); }

    @Override public void makeParty(ArrayList<String> pokeStrings, int partySize) {

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
            } else {
                PokeTextFormatter.displayPokeInfo(pokeStrings.get(choice - 1), choice);

                if (!PokePrompt.ynPrompt(chosenPoke, "y")) continue;

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

    @Override public int chooseAction() {
        if (active == null) {
            PokeConsole.print("Your last pokemon was knocked out. You are forced to pick a new pokemon!\n", ConsoleColors.RED_BOLD_BRIGHT, 10);
            return 2;
        }

        if (active.isStunned()) {
            PokeConsole.print(String.format("%s is stunned. You are forced to pass!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 10);
            return 3;
        }

        if (!active.canAttack()) {
            PokeConsole.print(String.format("%s doesn't have enough energy for any attacks!\n", active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 10);
        }

        PokeConsole.print("What will you do?\n", ConsoleColors.GREEN_BOLD_BRIGHT, 10);

        while (true) {
            PokeConsole.print("╔═══╦═════════════════╗\n", ConsoleColors.BLACK_BOLD, 10);
            PokeConsole.print("║ 1 ║ Attack          ║\n", (active.canAttack() ? ConsoleColors.BLACK_BOLD : ConsoleColors.RED_BOLD), 10);
            PokeConsole.print("║ 2 ║ Retreat Pokemon ║\n", ConsoleColors.BLACK_BOLD, 10);
            PokeConsole.print("║ 3 ║ Pass Turn       ║\n", ConsoleColors.BLACK_BOLD, 10);
            PokeConsole.print("╚═══╩═════════════════╝\n", ConsoleColors.BLACK_BOLD, 10);

            int input = PokePrompt.numPrompt(3);

            if (!active.canAttack() && input == 1) {
                PokeConsole.print("That's not possible!\n", ConsoleColors.RED_BOLD, 10);
                PokePrompt.cnPrompt();
                continue;
            }

            return input;
        }
    }

    @Override public void retreat() {

    }

    @Override public void attack(Trainer other) {

    }
}

class Opponent extends Trainer {

    public Opponent(String name, ArrayList<String> pokeStrings, int partySize) { super(name, pokeStrings, partySize); }

    @Override public void makeParty(ArrayList<String> pokeStrings, int partySize) {
        ArrayList<String> pokeNames = PokeMore.getPokeNames(pokeStrings);
        ArrayList<Integer> chosen = new ArrayList<>();
        Random rng = new Random();

        do {
            int choice = rng.nextInt(pokeStrings.size());
            if (!chosen.contains(choice)) {
                chosen.add(choice);
                PokeConsole.print(String.format("%s selected!\n", pokeNames.get(choice - 1)), ConsoleColors.GREEN_BOLD_BRIGHT, 10);
            }
        } while (chosen.size() < partySize);

        Collections.sort(chosen);
        Collections.reverse(chosen);
        for (int c : chosen) pokeParty.add(new Pokemon(pokeStrings.remove(c - 1)));
        Collections.reverse(pokeParty);

        PokeConsole.print("All Pokemon selected!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
        PokePrompt.cnPrompt();
    }

    @Override public int chooseAction() {
        if (active == null) {
            return 2;
        }

        if (active.isStunned()) {
            PokeConsole.print(String.format("%s's %s is stunned. You are forced to pass!\n", name, active.getName()), ConsoleColors.RED_BOLD_BRIGHT, 10);
            return 3;
        }

        if (active.canAttack()) {
            return 1;
        }

        return 3;
    }

    @Override public void retreat() {

    }

    @Override public void attack(Trainer other) {

    }
}
