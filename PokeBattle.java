/*
    PokeBattle.java
    Syed Safwaan
    Used to handle battle interactions to avoid clutter in the main file.
*/

import java.util.ArrayList;

class PokeBattle {

    // Fields //

    // THe two trainers that will go head to head
    private Trainer player, opponent;

    // Constructor //

    public PokeBattle(Trainer a, Trainer b) {

        /* Constructs and returns a PokeBattle object (although return value is never used). */

        // Set player and opponent, prepare trainers, then engage in main battle
        this.player = a; this.opponent = b;
        prepTrainers();
        main();
    }

    private void main() {

        // Loop to run battle
        while (true) {

            // Regain energy for both sides
            player.energyRegain();
            opponent.energyRegain();

            // Allow both sides to pick their action for the round
            player.actionSequence();
            opponent.actionSequence();

            // Pick random order (better than alternating imo, and just as simple to implement)
            Trainer p = (Math.random() < .5 ? player : opponent);
            Trainer q = (p == opponent ? player : opponent);

            PokeConsole.clear();

            // Execute each player's turn, and see if they win the battle in the round

            if (execTurn(p, q, 1)) {
                callWinner(p, q);
                break;
            }

            if (execTurn(q, p, 2)) {
                callWinner(q, p);
                break;
            }

            PokePrompt.cnPrompt();
        }
    }

    private boolean execTurn(Trainer a, Trainer b, int turnInRound) {

        // Output whose turn it is
        PokeConsole.print(String.format("%s's TURN!\n", a.getName()), ConsoleColors.REVERSE, 2);

        // if the Trainer has an active Pokemon and it is stunned, force a pass (if this person goes second after being stunned by turn 1 user)
        if (a.hasActive() && a.active.isStunned()) {
            PokeConsole.print(String.format("%s is stunned!\n", a.active.getName()), ConsoleColors.PURPLE_BOLD, 2);
            a.pass();
        } else {  // if the Pokemon is not stunned

            // Output whether the Pokemon is disabled or not
            if (a.hasActive() && a.active.isDisabled()) PokeConsole.print(String.format("%s is disabled!\n", a.active.getName()), ConsoleColors.PURPLE_BOLD, 2);

            // Perform the current turn Trainer's chosen action
            a.act(b);

            // If the opposition's Pokemon fainted
            if (b.active.fainted()) {

                // Output their fate and remove them from the party
                PokeConsole.print(String.format("%s fainted!\n", b.active.getName()), ConsoleColors.RED_BOLD, 2);
                b.removePoke(b.active);

                // if the opposition can still retreat and they weren't initially planning to retreat
                if (b.canRetreat() && b.getChoice() != Trainer.Choices.RETREAT) {

                    // Let the current side get an HP boost
                    PokeConsole.print(String.format("%s's team regains 20 HP for each Pokemon!\n", a.getName()), ConsoleColors.RED_BOLD, 2);
                    a.HPRegain();

                    // Prep the opposition for their next turn
                    b.prepAfterFaint();

                    // if it's still the first turn, let the opposition reset their choice
                    if (turnInRound == 1) b.actionSequence();
                }

                // if the opposition cannot retreat and has no active Pokemon, they lose
                else return true;
            }
        }

        // Set stun to false after a trun if they were stunned
        if (a.hasActive() && a.active.isStunned()) a.active.unstun();

        // If all actions have been processed, the battle has not ended
        return false;
    }

    private void prepTrainers() {

        /* Prepares the party-less Trainers for the battle. */

        // Pokemon string data
        ArrayList<String> pokeStrings = PokeMore.loadPokeData();

        // Get the size party the user wants
        PokeConsole.print("How many Pokemon would you like in your party?\n", ConsoleColors.BLUE, 2);
        int partySize = PokePrompt.numPrompt(pokeStrings.size() / 2);
        PokeConsole.clear();

        // Make the Trainer parties
        player.makeParty(pokeStrings, partySize);
        opponent.makeParty(pokeStrings, partySize);

        // Make each Trainer pick their active Pokemon
        player.retreat();
        opponent.retreat();

        // Set each Trainer's active Pokemon so they can engage
        player.setActive();
        opponent.setActive();

        PokeConsole.print("THE BATTLE BEGINS!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 2);
        PokePrompt.cnPrompt();
    }

    private void callWinner(Trainer winner, Trainer loser) {

        /* Outputs the winner of the Pokemon battle. */
        PokePrompt.cnPrompt();
        PokeConsole.print(String.format("%s is all out of Pokemon!\n", loser.getName()), ConsoleColors.BLUE_BOLD_BRIGHT, 100);
        PokeConsole.print(String.format("%s WINS!\n", winner.getName()), ConsoleColors.YELLOW_BOLD_BRIGHT, 100);
        PokePrompt.cnPrompt();
    }
}
