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

    public Trainer(String name, ArrayList<String> pokeStrings) {
        this.name = name;
        this.pokeParty = new ArrayList<>();
        this.makeParty(pokeStrings);
    }

    public String getName() { return this.name; }

    public ArrayList<Pokemon> getParty() { return this.pokeParty; }

    abstract void makeParty(ArrayList<String> pokeStrings);
}

class Player extends Trainer {

    public Player(String name, ArrayList<String> pokeStrings) { super(name, pokeStrings); }

    @Override public void makeParty(ArrayList<String> pokeStrings) {

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
                System.out.println();
            }

            // Get a number from the user, and then a name from the names list
            int choice = PokePrompt.numPrompt(pokeNames);
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
        } while (chosen.size() < 4);

        Collections.sort(chosen);
        Collections.reverse(chosen);
        for (int c : chosen) pokeParty.add(new Pokemon(pokeStrings.remove(c - 1)));
        Collections.reverse(pokeParty);

        PokeConsole.print("All Pokemon selected!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
        PokePrompt.cnPrompt();
    }
}

class Opponent extends Trainer {

    public Opponent(String name, ArrayList<String> pokeStrings) { super(name, pokeStrings); }

    @Override public void makeParty(ArrayList<String> pokeStrings) {
        ArrayList<String> pokeNames = PokeMore.getPokeNames(pokeStrings);
        ArrayList<Integer> chosen = new ArrayList<>();
        Random rng = new Random();

        do {
            int choice = rng.nextInt(pokeStrings.size());
            if (!chosen.contains(choice)) {
                chosen.add(choice);
                PokeConsole.print(String.format("%s selected!\n", pokeNames.get(choice - 1)), ConsoleColors.GREEN_BOLD_BRIGHT, 10);
            }
        } while (chosen.size() < 4);

        Collections.sort(chosen);
        Collections.reverse(chosen);
        for (int c : chosen) pokeParty.add(new Pokemon(pokeStrings.remove(c - 1)));
        Collections.reverse(pokeParty);

        PokeConsole.print("All Pokemon selected!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
        PokePrompt.cnPrompt();
    }
}
