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

    }

    private void prepTrainers() {
        ArrayList<String> pokeStrings = PokeMore.loadPokeData();

        PokeConsole.print("How many Pokemon would you like in your party?\n", ConsoleColors.BLUE, 10);
        int partySize = PokePrompt.numPrompt(pokeStrings.size() / 2);

        player.makeParty(pokeStrings, partySize);
        opponent.makeParty(pokeStrings, partySize);
    }
}
