/*
    PokeParts.java
    Syed Safwaan
    Used to handle battle interactions to avoid clutter in the main file.
*/

class PokeBattle {
    private Trainer a, b;

    public PokeBattle(Trainer a, Trainer b) {
        this.a = a; this.b = b;
        main();
    }

    private void main() {
        determineOrder();
        while (a.canFight() && b.canFight()) {
            Trainer
                p = (Math.random() < .5 ? a : b),
                q = (p == b ? a : b);
            execTurn(p, q);
            execTurn(q, p);
        }
    }

    private void determineOrder() {
        double randNum = Math.random();
    }

    private void execTurn(Trainer playing, Trainer other) {
        int action = playing.chooseAction();
        switch (action) {
            case 1:
                playing.attack(other);
            case 2:
                playing.retreat();
            case 3:
                playing.pass();
        }

        if (!other.canFight()) {
            // yay
        }
    }

    class PokeBattleText {

    }
}
