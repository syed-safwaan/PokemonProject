/*
    Syed Safwaan
    PokeMore.java
    A collection of convenience methods and fields for use in the game.
*/

import java.io.*;
import java.util.ArrayList;

class PokeMore {

    /* A collection of convenience methods for use in game. */

    public static ArrayList<String> loadPokeData() {

        /* Returns an ArrayList of Strings that contain Pokemon data for the Pokemon constructor to use. */

        // ArrayList to hold strings
        ArrayList<String> pokeStrings = new ArrayList<>();

        // Attempt to open file and read all strings from it
        try (BufferedReader pokeFile = new BufferedReader(new FileReader("pokedata.txt"))) {
            int numPokes = Integer.parseInt(pokeFile.readLine());
            for (int i = 0; i < numPokes; i ++) pokeStrings.add(pokeFile.readLine());
        } catch (FileNotFoundException e) {
            PokeConsole.errorShutdown("Could not find 'pokedata.txt.'");
        } catch (IOException e) {
            PokeConsole.errorShutdown("Unable to complete reading 'pokedata.txt.'");
        }

        return pokeStrings;
    }

    public static ArrayList<String> getPokeNames(ArrayList<String> pokeStrings) {

        /* Returns an ArrayList of Pokemon names given an ArrayList of Pokemon data. */

        ArrayList<String> names = new ArrayList<>();
        for (String s : pokeStrings) names.add(s.split(",")[0].toUpperCase());
        return names;
    }
}

class ConsoleColors {

    /* A collection of public static Strings fields used to colour text on the console. (for ANSI-supported consoles) */

    // Code courtesy of shakram02 [https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println]

    private static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

    // Reset
    public static final String RESET = isWindows ? "" : "\033[0m";  // Text Reset

    // Blank defaults for default colour
    public static final String BOLD = isWindows ? "" : "\033[1m";  // Text Bold
    public static final String ITALICS = isWindows ? "" : "\033[3m";  // Text Bold
    public static final String UNDERLINE = isWindows ? "" : "\033[4m";  // Text Underline
    public static final String REVERSE = isWindows ? "" : "\033[7m";  // Text Underline

    // Regular colors
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

    // High intensity
    public static final String BLACK_BRIGHT = isWindows ? "" : "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = isWindows ? "" : "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = isWindows ? "" : "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = isWindows ? "" : "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = isWindows ? "" : "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = isWindows ? "" : "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = isWindows ? "" : "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = isWindows ? "" : "\033[0;97m";  // WHITE

    // Bold and high intensity
    public static final String BLACK_BOLD_BRIGHT = isWindows ? "" : "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = isWindows ? "" : "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = isWindows ? "" : "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = isWindows ? "" : "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = isWindows ? "" : "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = isWindows ? "" : "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = isWindows ? "" : "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = isWindows ? "" : "\033[1;97m"; // WHITE

    // High intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = isWindows ? "" : "\033[0;107m";   // WHITE
}
