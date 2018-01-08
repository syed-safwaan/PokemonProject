/*
    PokeParts.java
    Syed Safwaan
    Used to handle battle interactions to avoid clutter in the main file.
*/

import java.util.ArrayList;

class PokeBattle {
    private Trainer player, opponent;

    public PokeBattle(Trainer a, Trainer b) {
        this.player = a; this.opponent = b;
        prepTrainers();
        main();
    }

    private void main() {
        while (true) {
            player.energyRegain();
            opponent.energyRegain();

            player.chooseAction();
            player.prepAction();

            opponent.chooseAction();
            opponent.prepAction();


            Trainer p = (Math.random() < .5 ? player : opponent);
            Trainer q = (p == opponent ? player : opponent);

            PokeConsole.clear();

            if (execTurn(p, q)) {
                callWinner(p);
                break;
            }

            if (execTurn(q, p)) {
                callWinner(q);
                break;
            }

            System.out.println("turn over");
            PokePrompt.cnPrompt();
        }
    }

    private boolean execTurn(Trainer a, Trainer b) {
        PokeConsole.print(String.format("%s's TURN!\n", a.getName()), ConsoleColors.REVERSE, 10);

        if (a.hasActive() && a.active.isStunned()) {
            PokeConsole.print(String.format("%s is stunned!\n", a.active.getName()), ConsoleColors.PURPLE_BOLD, 10);
            a.pass();
        } else {
            if (a.hasActive() && a.active.isDisabled()) PokeConsole.print(String.format("%s is disabled!\n", a.active.getName()), ConsoleColors.PURPLE_BOLD, 10);
            a.act(b);
            if (b.active.fainted()) {
                PokeConsole.print(String.format("%s fainted!\n", b.active.getName()), ConsoleColors.RED_BOLD, 10);
                b.removePoke(b.active);
                if (b.canRetreat() && b.getChoice() != Trainer.Choices.RETREAT) {
                    b.prepAfterFaint();
                }
                else if (!b.canFight()) return true;
            }
        }

        if (a.hasActive() && a.active.isStunned()) a.active.unstun();

        return false;
    }

    private void prepTrainers() {
        ArrayList<String> pokeStrings = PokeMore.loadPokeData();

        PokeConsole.print("How many Pokemon would you like in your party?\n", ConsoleColors.BLUE, 10);
        int partySize = PokePrompt.numPrompt(pokeStrings.size() / 2);
        PokeConsole.clear();

        player.makeParty(pokeStrings, partySize);
        opponent.makeParty(pokeStrings, partySize);

        player.retreat();
        opponent.retreat();

        player.setActive();
        opponent.setActive();

        PokeConsole.print("THE BATTLE BEGINS!\n", ConsoleColors.YELLOW_BOLD_BRIGHT, 10);
        PokePrompt.cnPrompt();
    }

    private void callWinner(Trainer trainer) {
        PokeConsole.print(String.format("%s WINS!\n", trainer.getName()), ConsoleColors.YELLOW_BOLD_BRIGHT, 100);
        PokePrompt.cnPrompt();
    }
}
