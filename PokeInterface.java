/*
    PokeInterface.java
    Syed Safwaan
    A collection of classes used to handle the interface of the game, from colours to.

    Classes:
    - PokeConsole           output and general console management
    - PokePrompt            input
    - PokeTextFormatter     more output, as well as data formatting from pokemon strings
    - ConsoleColors         colours!
*/

import java.io.IOException;
import java.util.*;

class PokeConsole {

    /* a collection of methods which handle creative output, as well as console management. */

    public static void print(String content, int speed) {

        /*
         *  Prints out a given string character by character as if emulating human typing.
         *  Overload #1
         */

        // Try catch block to handle the Thread.sleep() exception
        try {

            // Print out the string character by character
            for (int c = 0; c < content.length(); c ++) {
                Thread.sleep(/*speed*/0);
                System.out.print(content.charAt(c));
            }
        } catch (InterruptedException e) {

            // In case of an error, just write confusion to the error log
            System.err.println("wot");
        }
    }

    public static void print(String content, String colour, int speed) {

        /*
         *  Prints out a given string character by character with colour as if emulating human typing.
         *  Overload #1
         */

        // Add the colour codes to the string
        content = color(content, colour);

        // Do everything the same as overload #1
        try {
            for (int c = 0; c < content.length(); c ++) {
                Thread.sleep(/*speed*/0);
                System.out.print(content.charAt(c));
            }
        } catch (InterruptedException e) {
            System.err.println("wot");
        }
    }

    public static void clearConsole() {

        /* Clears console of all previous output. */

        // PURGE CONSOLE
        for (int i = 0; i < 15; i ++) System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String color(String str, String colour) {

        /* Returns a String with a colour code prepended and colour reset appended. */

        return colour + str + ConsoleColors.RESET;
    }

    public static void errorShutdown(String err) {

        /* Displays error message and shuts down entire program. */

        clearConsole();
        print(String.format("Error: %s\n", err), ConsoleColors.RED_BOLD, 1);
        PokePrompt.cnPrompt();
        System.exit(0);
    }
}

class PokePrompt {

    // Scanner object to take in input from prompt
    private static Scanner stdin = new Scanner(System.in);

    public static void cnPrompt() {

        /* Waits for user to press ENTER before continuing. */

        PokeConsole.print("Press ENTER to continue...", ConsoleColors.BLACK_BOLD_BRIGHT, 20);
        try { System.in.read(); } catch (IOException e) { System.err.println("wot"); }
        PokeConsole.clearConsole();
    }

    public static boolean ynPrompt(String given, String def) {

        /* Returns user confirmation on a certain choice or input. [Yes, No] */

        // Modelled after Linux Bash prompts in most user-made programs

        // Prepare the strings for output (to signal which is default)
        String y = (def.equals("y") ? "Y" : "y"), n = (def.equals("n") ? "N" : "n");

        // Output question preceding prompt
        PokeConsole.print("Is ", ConsoleColors.BLUE, 10);
        PokeConsole.print(given, ConsoleColors.BLACK_BOLD, 10);
        PokeConsole.print(" okay? ", ConsoleColors.BLUE, 10);
        PokeConsole.print(String.format("[%s/%s] ", y, n), ConsoleColors.BLACK_BOLD, 10);

        // Get input, and set to default if empty
        String input = stdin.nextLine().trim().toLowerCase();
        if (input.isEmpty()) input = def;

        PokeConsole.clearConsole();
        return input.equals("y");
    }

    public static String qPrompt(String prompt, String def) {

        /* Returns a String inputted by a user after proofchecking and editing. */

        // String to collect input
        String input;

        // Loop to repeat input process till approved
        do {
            PokeConsole.print(prompt + " ", ConsoleColors.BLUE, 10);
            input = stdin.nextLine().trim().toUpperCase();
        } while (input.isEmpty() || !ynPrompt(input, def));

        PokeConsole.clearConsole();
        return input;
    }

    public static int numPrompt(int range) {

        /* Returns an integer inputted by a user after proofchecking, to simplify getting input. */

        PokeConsole.print(String.format(PokeConsole.color("Enter in an option.", ConsoleColors.YELLOW_BOLD) + PokeConsole.color(" [1 .. %d].\n", ConsoleColors.BLACK_BOLD_BRIGHT), range), 10);

        // Integer to collect input
        int input;

        while (true) {

            // Check for improper input to catch errors
            try {
                input = stdin.nextInt();
            } catch (InputMismatchException e) {
                PokeConsole.print("Could not parse. :(\n", ConsoleColors.RED_BOLD, 10);
                continue;
            } finally {
                stdin.nextLine();
            }

            // Check for out of range input
            if (input < 1 || input > range) {
                PokeConsole.print("Not an option in range. :(\n", ConsoleColors.RED_BOLD, 10);
                continue;
            }

            // Break when both tests have been passed
            break;
        }

        PokeConsole.clearConsole();
        return input;
    }
}

class PokeTextFormatter {

    /* A collection of methods that help create stylized output and text for use in game. */

//    static String mem =
//            "╔═══╦═══╦═══╗" +
//            "║   ║   ║   ║" +
//            "╠═══╬═══╬═══╣" +
//            "║   ║   ║   ║" +
//            "╚═══╩═══╩═══╝";

    public static void speechBox(String text, String speakerOrHeading, String colour, int speed) {

        /* Outputs a bunch of text structured like a text box from a game. */

        // Output top portion containing speaker or heading
        PokeConsole.print("╔══════════════════════════════════════════════════════════╗\n║ ", ConsoleColors.BOLD, 0);
        PokeConsole.print(String.format("%-56s", speakerOrHeading), colour, 2);
        PokeConsole.print(" ║\n╠══════════════════════════════════════════════════════════╣\n║", ConsoleColors.BOLD, 0);

        // Output the actual message text

        // StringBuilder for managing the text on each line
        StringBuilder curLine = new StringBuilder();

        // For every word in our message
        for (String word : text.split(" ")) {

            // If the current line is too long
            if ((curLine + word).length() + 1 > 56) {

                // Get into the next line
                PokeConsole.print(String.format("%-58s", curLine.toString()), colour, speed);
                PokeConsole.print("║\n║", ConsoleColors.BOLD, 0);
                curLine = new StringBuilder();
            }

            // Add the next word to our line
            curLine.append(" ").append(word);
        }

        // Output the last line and bottom of the box
        PokeConsole.print(String.format("%-58s", curLine.toString()), colour, speed);
        PokeConsole.print("║\n╚══════════════════════════════════════════════════════════╝\n", ConsoleColors.BOLD, 0);

        PokePrompt.cnPrompt();
    }

    public static void displayPokeNames(ArrayList<String> pokeStrings, int columns) {

        /* Outputs columns of text given an ArrayList of Strings. */

        // Output every option numbered, using a newline at the end of each print if the multiple is right
        for (int s = 0; s < pokeStrings.size(); s ++) {
            String name = pokeStrings.get(s).split(",")[0].toUpperCase();
            PokeConsole.print(String.format("%-20s%s", String.format("%d. %s", s + 1, name), (s + 1) % columns == 0 || s + 1 == pokeStrings.size() ? "\n" : ""), ConsoleColors.BLACK_BOLD, 1);
        }
    }

    public static void displayPokeInfo(String pokeData, int num) {

        /* Outputs a data card containing all info given in a String. */

        // Scanner to scan string
        Scanner pkscn = new Scanner(pokeData);
        pkscn.useDelimiter(",");

        // Extract pokemon basic data from string
        String
            pName = pkscn.next(),
            hp = pkscn.next(),
            type = pkscn.next(),
            resistance = pkscn.next().trim(),
            weakness = pkscn.next().trim();

        // Output the body of the card (pokemon basic info)
        PokeConsole.print("╔════════════════════════════════╗\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.print(String.format("%-30s", String.format("%d. %s", num, pName)), ConsoleColors.CYAN_BOLD_BRIGHT, 0);
        PokeConsole.print(" ║\n╠════════════════════════════════╣\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.print(String.format("%-30s", String.format("HP           %s", hp)), ConsoleColors.RED_BOLD_BRIGHT, 0);
        PokeConsole.print(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.print(String.format("%-30s", String.format("TYPE         %s", type)), ConsoleColors.BLACK_BOLD, 0);
        PokeConsole.print(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);

        PokeConsole.print(String.format("%-30s", String.format("RESISTANCE   %s", resistance.isEmpty() ? "none" : resistance)), ConsoleColors.GREEN_BOLD_BRIGHT, 0);
        PokeConsole.print(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.print(String.format("%-30s", String.format("WEAKNESS     %s", weakness.isEmpty() ? "none" : weakness)), ConsoleColors.BLUE_BOLD_BRIGHT, 0);
        PokeConsole.print(" ║\n╠════════════════════════════════╣\n", ConsoleColors.YELLOW_BOLD, 0);

        // Output the attacks info

        // Number of attacks
        int numAttacks = pkscn.nextInt();

        // For each set of attack data in the string
        for (int i = 1; i <= numAttacks; i ++) {

            // Extract attack data from string
            String
                aName = pkscn.next(),
                cost = pkscn.next(),
                damage = pkscn.next(),
                special = (pkscn.hasNext() ? pkscn.next() : "none");

            // Output the attack data
            PokeConsole.print("║ ", ConsoleColors.YELLOW_BOLD, 0);
            PokeConsole.print(String.format("%-30s", String.format("ATTACK %d: %s", i, aName)), ConsoleColors.CYAN_BOLD_BRIGHT, 0);
            PokeConsole.print(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
            PokeConsole.print(String.format("%-30s", String.format("DMG: %s COST: %s", damage, cost)), ConsoleColors.YELLOW_BOLD_BRIGHT, 0);
            PokeConsole.print(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
            PokeConsole.print(String.format("%-30s", String.format("SPECIAL: %s", special)), ConsoleColors.PURPLE_BOLD_BRIGHT, 0);
            PokeConsole.print(" ║\n", ConsoleColors.YELLOW_BOLD, 0);
        }
        PokeConsole.print("╚════════════════════════════════╝\n", ConsoleColors.YELLOW_BOLD, 0);
    }
}


