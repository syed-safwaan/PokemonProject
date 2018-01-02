import java.io.IOException;
import java.util.*;

class PokeConsole {
    private static int pokePrints = 100;

    public static void pokePrint(String content, int speed) {
        try {
            for (int c = 0; c < content.length(); c ++) {
                Thread.sleep(/*speed*/0);
                System.out.print(content.charAt(c));
            }
        } catch (InterruptedException e) {
            System.err.println("wot");
        }

        pokePrints ++;
    }
    public static void pokePrint(String content, String colour, int speed) {
        try {
            for (int c = 0; c < content.length(); c ++) {
                Thread.sleep(/*speed*/0);
                System.out.print(color(Character.toString(content.charAt(c)), colour));
            }
        } catch (InterruptedException e) {
            System.err.println("wot");
        }

        pokePrints ++;
    }

    public static void clearConsole() {
        for (int i = 0; i < pokePrints; i ++) System.out.print("\033[H\033[2J");
        System.out.flush();

        pokePrints = 100;
    }

    public static String color(String str, String colour) {
        return colour + str + ConsoleColors.RESET;
    }
    public static String color(String str, String colour, String reset) {
        return colour + str + reset;
    }

    public static void errorShutdown(String err) {
        clearConsole();
        pokePrint("Error: " + err + "\n", ConsoleColors.RED_BOLD, 1);
        PokePrompt.cnPrompt();
        System.exit(0);
    }
}

class PokePrompt {
    private static Scanner stdin = new Scanner(System.in);

    public static void cnPrompt() {
        PokeConsole.pokePrint("Press ENTER to continue...", ConsoleColors.BLACK_BOLD_BRIGHT, 20);
        try { System.in.read(); } catch (IOException e) {}
        PokeConsole.clearConsole();
    }

    public static boolean ynPrompt(String given, String def) {
        String y = (def.equals("y") ? "Y" : "y"), n = (def.equals("n") ? "N" : "n");

        PokeConsole.pokePrint("Is ", ConsoleColors.BLUE, 10);
        PokeConsole.pokePrint(given, ConsoleColors.BLACK_BOLD, 10);
        PokeConsole.pokePrint(" okay? ", ConsoleColors.BLUE, 10);
        PokeConsole.pokePrint(String.format("[%s/%s] ", y, n), ConsoleColors.BLACK_BOLD, 10);

        String input = stdin.nextLine().trim().toLowerCase();
        if (input.isEmpty()) input = def;

        PokeConsole.clearConsole();
        return input.equals("y");
    }

    public static String qPrompt(String prompt, String def) {
        String input;
        do {
            PokeConsole.pokePrint(prompt + " ", ConsoleColors.BLUE, 10);
            input = stdin.nextLine().trim().toUpperCase();
        } while (input.isEmpty() || !ynPrompt(input, def));

        PokeConsole.clearConsole();
        return input;

    }

    public static int numPrompt(int range, ArrayList<String> options) {
        PokeConsole.pokePrint(String.format(PokeConsole.color("Enter in an option.", ConsoleColors.YELLOW_BOLD) + PokeConsole.color(" [1 .. %d].\n", ConsoleColors.BLACK_BOLD_BRIGHT), range), 10);

        int input;
        while (true) {
            try {
                input = stdin.nextInt();
            } catch (InputMismatchException e) {
                PokeConsole.pokePrint("Could not parse. :(\n", ConsoleColors.RED_BOLD, 10);
                continue;
            } finally {
                stdin.nextLine();
            }

            if (input < 1 || input > range) {
                PokeConsole.pokePrint("Not an option in range. :(\n", ConsoleColors.RED_BOLD, 10);
                continue;
            }

            break;
        }

        PokeConsole.clearConsole();
        return input;
    }

    public static int numPrompt(int range, ArrayList<String> options, String def) {
        PokeConsole.pokePrint(String.format(PokeConsole.color("Enter in an option.", ConsoleColors.YELLOW_BOLD) + PokeConsole.color(" [1 .. %d].\n", ConsoleColors.BLACK_BOLD_BRIGHT), range), 10);

        int input;
        while (true) {
            try {
                input = stdin.nextInt();
            } catch (InputMismatchException e) {
                PokeConsole.pokePrint("Could not parse. :(\n", ConsoleColors.RED_BOLD, 10);
                continue;
            } finally {
                stdin.nextLine();
            }

            if (input < 1 || input > range) {
                PokeConsole.pokePrint("Not an option in range. :(\n", ConsoleColors.RED_BOLD, 10);
                continue;
            }

            if (ynPrompt(options.get(input - 1), def)) break;
            else PokeConsole.pokePrint(String.format(PokeConsole.color("Enter in an option.", ConsoleColors.YELLOW_BOLD) + PokeConsole.color(" [1 .. %d].\n", ConsoleColors.BLACK_BOLD_BRIGHT), range), 10);
        }

        PokeConsole.clearConsole();
        return input;
    }
}

class PokeTextFormatter {
    static String mem =
            "╔═══╦═══╦═══╗" +
            "║   ║   ║   ║" +
            "╠═══╬═══╬═══╣" +
            "║   ║   ║   ║" +
            "╚═══╩═══╩═══╝";

    public static void speechBox(String text, String speakerOrHeading, String colour, int speed) {
        PokeConsole.pokePrint("╔══════════════════════════════════════════════════════════╗\n║", ConsoleColors.BOLD, 0);
        PokeConsole.pokePrint(String.format(" %-57s", speakerOrHeading), colour, 2);
        PokeConsole.pokePrint("║\n╠══════════════════════════════════════════════════════════╣\n║", ConsoleColors.BOLD, 0);

        StringBuilder curLine = new StringBuilder();
        for (String word : text.split(" ")) {
            if ((curLine + word).length() + 1 > 56) {
                PokeConsole.pokePrint(String.format("%-58s", curLine.toString()), colour, speed);
                PokeConsole.pokePrint("║\n║", ConsoleColors.BOLD, 0);
                curLine = new StringBuilder();
            }
            curLine.append(" ").append(word);
        }

        PokeConsole.pokePrint(String.format("%-58s", curLine.toString()), colour, speed);
        PokeConsole.pokePrint("║\n╚══════════════════════════════════════════════════════════╝\n", ConsoleColors.BOLD, 0);

        PokePrompt.cnPrompt();
    }

    public static void displayPokeNames(ArrayList<String> pokeStrings, int columns) {
        for (int s = 0; s < pokeStrings.size(); s ++) {
            String name = pokeStrings.get(s).split(",")[0].toUpperCase();
            PokeConsole.pokePrint(String.format("%-20s%s", String.format("%d. %s", s + 1, name), (s + 1) % columns == 0 || s + 1 == pokeStrings.size() ? "\n" : ""), ConsoleColors.BLACK_BOLD, 1);
        }
    }

    public static ArrayList<String> getPokeNames(ArrayList<String> pokeStrings) {
        ArrayList<String> names = new ArrayList<>();
        for (String s : pokeStrings) names.add(s.split(",")[0].toUpperCase());
        return names;
    }

    public static void displayPokeInfo(String pokeData, int num) {
        Scanner pkscn = new Scanner(pokeData);
        pkscn.useDelimiter(",");

        PokeConsole.pokePrint("╔════════════════════════════════╗\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.pokePrint(String.format("%-30s", String.format("%d. %s", num, pkscn.next())), ConsoleColors.CYAN_BOLD_BRIGHT, 0);
        PokeConsole.pokePrint(" ║\n╠════════════════════════════════╣\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.pokePrint(String.format("%-30s", String.format("HP           %s", pkscn.next())), ConsoleColors.BLACK_BOLD, 0);
        PokeConsole.pokePrint(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.pokePrint(String.format("%-30s", String.format("TYPE         %s", pkscn.next())), ConsoleColors.BLACK_BOLD, 0);
        PokeConsole.pokePrint(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        String resistance = pkscn.next().trim(), weakness = pkscn.next().trim();
        PokeConsole.pokePrint(String.format("%-30s", String.format("RESISTANCE   %s", resistance.isEmpty() ? "none" : resistance)), ConsoleColors.BLACK_BOLD, 0);
        PokeConsole.pokePrint(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
        PokeConsole.pokePrint(String.format("%-30s", String.format("WEAKNESS     %s", weakness.isEmpty() ? "none" : weakness)), ConsoleColors.BLACK_BOLD, 0);
        PokeConsole.pokePrint(" ║\n╠════════════════════════════════╣\n", ConsoleColors.YELLOW_BOLD, 0);

        int numAttacks = pkscn.nextInt();
        for (int i = 1; i <= numAttacks; i ++) {
            String name = pkscn.next(), cost = pkscn.next(), damage = pkscn.next(), special = pkscn.next().trim();

            PokeConsole.pokePrint("║ ", ConsoleColors.YELLOW_BOLD, 0);
            PokeConsole.pokePrint(String.format("%-30s", String.format("ATTACK %d: %s", i, name)), ConsoleColors.CYAN_BOLD_BRIGHT, 0);
            PokeConsole.pokePrint(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
            PokeConsole.pokePrint(String.format("%-30s", String.format("DMG: %s COST: %s", damage, cost)), ConsoleColors.BLACK_BOLD, 0);
            PokeConsole.pokePrint(" ║\n║ ", ConsoleColors.YELLOW_BOLD, 0);
            PokeConsole.pokePrint(String.format("%-30s", String.format("SPECIAL: %s", special.isEmpty() ? "none" : special)), ConsoleColors.BLACK_BOLD, 0);
            PokeConsole.pokePrint(" ║\n", ConsoleColors.YELLOW_BOLD, 0);
        }
        PokeConsole.pokePrint("╚════════════════════════════════╝\n", ConsoleColors.YELLOW_BOLD, 0);
    }
}

class ConsoleColors {
    private static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

    // Reset
    public static final String RESET = isWindows ? "" : "\033[0m";  // Text Reset
    public static final String BOLD = isWindows ? "" : "\033[1m";  // Text Bold
    public static final String ITALICS = isWindows ? "" : "\033[3m";  // Text Bold
    public static final String UNDERLINE = isWindows ? "" : "\033[4m";  // Text Underline
    public static final String REVERSE = isWindows ? "" : "\033[7m";  // Text Underline

    // Regular Colors
    public static final String BLACK = isWindows ? "" : "\033[0;30m";   // BLACK
    public static final String RED = isWindows ? "" : "\033[0;31m";     // RED
    public static final String GREEN = isWindows ? "" : "\033[0;32m";   // GREEN
    public static final String YELLOW = isWindows ? "" : "\033[0;33m";  // YELLOW
    public static final String BLUE = isWindows ? "" : "\033[0;34m";    // BLUE
    public static final String PURPLE = isWindows ? "" : "\033[0;35m";  // PURPLE
    public static final String CYAN = isWindows ? "" : "\033[0;36m";    // CYAN
    public static final String WHITE = isWindows ? "" : "\033[0;37m";   // WHITE


    // Bold
    public static final String BLACK_BOLD = isWindows ? "" : "\033[1;30m";  // BLACK
    public static final String RED_BOLD = isWindows ? "" : "\033[1;31m";    // RED
    public static final String GREEN_BOLD = isWindows ? "" : "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = isWindows ? "" : "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = isWindows ? "" : "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = isWindows ? "" : "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = isWindows ? "" : "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = isWindows ? "" : "\033[1;37m";  // WHITE

    // Italics
    public static final String BLACK_ITALICS = isWindows ? "" : "\033[3;30m";   // BLACK
    public static final String RED_ITALICS = isWindows ? "" : "\033[3;31m";     // RED
    public static final String GREEN_ITALICS = isWindows ? "" : "\033[3;32m";   // GREEN
    public static final String YELLOW_ITALICS = isWindows ? "" : "\033[3;33m";  // YELLOW
    public static final String BLUE_ITALICS = isWindows ? "" : "\033[3;34m";    // BLUE
    public static final String PURPLE_ITALICS = isWindows ? "" : "\033[3;35m";  // PURPLE
    public static final String CYAN_ITALICS = isWindows ? "" : "\033[3;36m";    // CYAN
    public static final String WHITE_ITALICS = isWindows ? "" : "\033[3;37m";   // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = isWindows ? "" : "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = isWindows ? "" : "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = isWindows ? "" : "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = isWindows ? "" : "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = isWindows ? "" : "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = isWindows ? "" : "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = isWindows ? "" : "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = isWindows ? "" : "\033[4;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = isWindows ? "" : "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = isWindows ? "" : "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = isWindows ? "" : "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = isWindows ? "" : "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = isWindows ? "" : "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = isWindows ? "" : "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = isWindows ? "" : "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = isWindows ? "" : "\033[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = isWindows ? "" : "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = isWindows ? "" : "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = isWindows ? "" : "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = isWindows ? "" : "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = isWindows ? "" : "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = isWindows ? "" : "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = isWindows ? "" : "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = isWindows ? "" : "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = isWindows ? "" : "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = isWindows ? "" : "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = isWindows ? "" : "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = isWindows ? "" : "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = isWindows ? "" : "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = isWindows ? "" : "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = isWindows ? "" : "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = isWindows ? "" : "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;107m";   // WHITE
}

