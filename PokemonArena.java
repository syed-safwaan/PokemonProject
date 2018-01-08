/*
    PokeParts.java
    Syed Safwaan
    Main file of the program, where the game is run from.
*/

public class PokemonArena {

    /* The main class of the entire program. */

    // Trainer fields, which are accessed through the other methods in this file
    private static Trainer player;
    private static Trainer opponent;

    public static void main(String[] args) {

        /* Entry point of the program. */

        // Call the introductory stuffs
        introSequence();
        introDialogue();

        // Loop to replay the game
        do {
            // New battle instance
            new PokeBattle(player, opponent);

            // After battle is over, ask if they want to replay
            PokeTextFormatter.speechBox("Well, that was fun!, Want to play again?", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        } while (PokePrompt.ynPrompt("another game", "n"));
    }

    private static void introSequence() {

        /* The introductory cinematic that plays at the start of the program. */

        // Just some strings

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

        // Output those strings
        PokeConsole.clear();
        PokeConsole.print("                     ", ConsoleColors.CYAN_BACKGROUND_BRIGHT, 20);
        PokeConsole.print(" THE PULSE PRESENTS ", ConsoleColors.CYAN_BOLD_BRIGHT, 20);
        PokeConsole.print("                     \n", ConsoleColors.CYAN_BACKGROUND_BRIGHT, 20);
        PokeConsole.print(pokelogo, ConsoleColors.YELLOW_BOLD_BRIGHT, 5);
        PokeConsole.print(arenalogo, ConsoleColors.RED_BOLD_BRIGHT, 5);
        PokeConsole.print(demologo, ConsoleColors.BLACK_BOLD, 5);

        // Credits
        PokeConsole.print("     Developed By Syed Safwaan [github.com/syed-safwaan]\n", ConsoleColors.CYAN_BOLD_BRIGHT, 20);
        PokeConsole.print("     Data file courtesy of Aaron Li [github.com/dumfing]\n\n", ConsoleColors.PURPLE_BOLD_BRIGHT, 20);

        // Prompt continue
        System.out.print("                  ");
        PokePrompt.cnPrompt();
    }

    private static void introDialogue() {

        /* THe introductory dialogue that takes place before constructing battle. */

        PokeTextFormatter.speechBox("Hello! My name is Professor Pecan, and I'm your demo host- err, your local Pokemon professor!", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("How about we start by getting your name?", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);

        // Make player from a given name
        player = new Player(PokePrompt.qPrompt("What's your name?", "n"));
        PokeConsole.clear();

        PokeTextFormatter.speechBox(String.format("%s? Nice to meet you!", player.getName()), "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("This is the world of Pokemon, weird animal things that have magic properties or some garbage- uh, are really interesting! I have spent my life working with these Pokemon, and to see you here marks your beginnings as a Pokemon Master!", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("Along with you, we have my grandchild here. To create some drama in this lackluster demo, he (or she, can't assume anymore really) will be your rival. Since we're too low-budget to actually generate a name for him (or her), you can give him (or her) his (or her) name. Think of it like he's (or she's) your own evil tamagotchi come to rebel.", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);

        // Make opponent from a given name
        opponent = new Opponent(PokePrompt.qPrompt("What's your rival's name?", "n"));
        PokeConsole.clear();

        PokeTextFormatter.speechBox(String.format("%s? Seems rather bland... Oh well, on to the fun part, I presume.", opponent.getName()), "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("Let's get to the Pokemon already!", opponent.getName(), ConsoleColors.RED_BRIGHT, 15);
        PokeTextFormatter.speechBox(String.format("Shut up, %s- I mean, sure buddy! %s, since you're our main character, you can pick the Pokemon first.", opponent.getName().substring(0, 1), player.getName()), "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("What, you didn't know we don't have an actual story-based game here? We're pitting you two head to head in a Pokemon showdown.", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
        PokeTextFormatter.speechBox("Enough talk, my script's done- I mean, no time to wait! Let's get battling!", "Professor Pecan", ConsoleColors.BLACK_BOLD, 20);
    }
}