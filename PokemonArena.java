/*
    PokeParts.java
    Syed Safwaan
    Main file of the program, where the game is run from.
*/

import java.io.*;
import java.util.ArrayList;

public class PokemonArena {

    static Trainer player;
    static Trainer opponent;

    public static void main(String[] args) throws IOException {

        // intro sequence
        introSequence();

        // professor pecan coming in
        // PokeFormatter makes text boxes out of PP's speech

        // PP wants to get player name
        // - need Player subclass

        // PP also wants his grandchild's name (can't assume genders)
        // - need Enemy subclass

        // player picks pokemon
        // - arena handles this (read file, use PokeFormatter)

        // enemy picks pokemon
        // - after player, this is ez

        // ^^^^ All in here
        prepTrainers();

        // start battle
        // - player picks starting pokemon, enemy picks their starting pokemon

        // BATTLE
        // random starting trainer

        // option menu for pokemon (attack, retreat, pass)
        // - methods for each, ezpz

        // after every trainer finishes an action, check if trainers are still able to fight
        // - if one is not, declare winner

        // :)
    }

    private static void introSequence() {
        String pokelogo =
                "\n                                .::.\n" +
                        "                             .;:**'            AMC\n" +
                        "                              `                  0\n" +
                        "  .:XHHHHk.              db.   .;;.     dH  MX   0\n" +
                        "oMMMMMMMMMMM       ~MM  dMMP :MMMMMR   MMM  MR      ~MRMN\n" +
                        "QMMMMMb  \"MMX       MMMMMMP !MX' :M~   MMM MMM  .oo. XMMM 'MMM\n" +
                        "  `MMMM.  )M> :X!Hk. MMMM   XMM.o\"  .  MMMMMMM X?XMMM MMM>!MMP\n" +
                        "   'MMMb.dM! XM M'?M MMMMMX.`MMMMMMMM~ MM MMM XM `\" MX MMXXMM\n" +
                        "    ~MMMMM~ XMM. .XM XM`\"MMMb.~*?**~ .MMX M t MMbooMM XMMMMMP\n" +
                        "     ?MMM>  YMMMMMM! MM   `?MMRb.    `\"\"\"   !L\"MMMMM XM IMMM\n" +
                        "      MMMX   \"MMMM\"  MM       ~%:           !Mh.\"\"\" dMI IMMP\n" +
                        "      'MMM.                                             IMX\n" +
                        "       ~M!M                                             IMP\n";

        String arenalogo =
                "        _      _______    ________ ____  _____      _       \n" +
                        "       / \\    |_   __ \\  |_   __  |_   \\|_   _|    / \\      \n" +
                        "      / _ \\     | |__) |   | |_ \\_| |   \\ | |     / _ \\     \n" +
                        "     / ___ \\    |  __ /    |  _| _  | |\\ \\| |    / ___ \\    \n" +
                        "   _/ /   \\ \\_ _| |  \\ \\_ _| |__/ |_| |_\\   |_ _/ /   \\ \\_  \n" +
                        "  |____| |____|____| |___|________|_____|\\____|____| |____| \n";

        String demologo =
                "                        _               \n" +
                        "                      _| |___ _____ ___ \n" +
                        "                     | . | -_|     | . |\n" +
                        "                     |___|___|_|_|_|___|\n\n";

        PokeConsole.clearConsole();
        PokeConsole.pokePrint("                     ", ConsoleColors.CYAN_BACKGROUND_BRIGHT, 20);
        PokeConsole.pokePrint(" THE PULSE PRESENTS ", ConsoleColors.CYAN_BOLD_BRIGHT, 20);
        PokeConsole.pokePrint("                     \n", ConsoleColors.CYAN_BACKGROUND_BRIGHT, 20);
        PokeConsole.pokePrint(pokelogo, ConsoleColors.YELLOW_BOLD_BRIGHT, 5);
        PokeConsole.pokePrint(arenalogo, ConsoleColors.CYAN_BOLD, 5);
        PokeConsole.pokePrint(demologo, ConsoleColors.BLACK_BOLD, 5);

        System.out.print("                 ");
        PokePrompt.cnPrompt();
    }

    private static void prepTrainers() {
        PokeTextFormatter.speechBox("Hello! My name is Professor Pecan, and I'm your demo host- err, your local Pokemon professor!", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("How about we start by getting your name?", "Professor Pecan", ConsoleColors.BLACK_BOLD, 2);

        String playerName = PokePrompt.qPrompt("What's your name?", "n");

        PokeTextFormatter.speechBox(String.format("%s? Nice to meet you!", playerName), "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("This is the world of Pokemon, weird animal things that have magic properties or some garbage- uh, are really interesting! I have spent my life working with these Pokemon, and to see you here marks your beginnings as a Pokemon Master!", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("Along with you, we have my grandchild here. To create some drama in this lackluster demo, he (or she, can't assume anymore really) will be your rival. Since we're too low-budget to actually generate a name for him (or her), you can give him (or her) his (or her) name. Think of it like he's (or she's) your own evil tamagotchi come to rebel.", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);

        String opponentName = PokePrompt.qPrompt("What's your rival's name?", "n");

        PokeTextFormatter.speechBox(String.format("%s? Seems rather bland... Oh well, on to the fun part, I presume.", opponentName), "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("Let's get to the Pokemon already!", opponentName, ConsoleColors.RED, 15);
        PokeTextFormatter.speechBox(String.format("Shut up, %s- I mean, sure buddy! %s, since you're our main character, you can pick the Pokemon first. 4 only.", opponentName.substring(0, 1), playerName), "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);

        ArrayList<String> pokeStrings = loadPokeData();
        player = new Player(playerName, pokeStrings);
        PokeTextFormatter.speechBox("My turn!", opponentName, ConsoleColors.RED, 15);
        opponent = new Opponent(opponentName, pokeStrings);
    }

    private static ArrayList<String> loadPokeData() {
        ArrayList<String> pokeStrings = new ArrayList<>();

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
}