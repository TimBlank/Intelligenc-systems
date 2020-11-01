package Aufgabe1.src;

public class Game {

    public Field playingFieldStart;
    Player[] players = {
            new Player("#f00", "EINS"),
            new Player("#0f0", "ZWEI"),
            new Player("#00f", "DREI"),
            new Player("#ff0", "VIER"),
    };

    public static void main(String[] args) {
        Game game = new Game();
        game.setupPlayingField(10, 4);
        System.exit(0);
    }

    public void setupPlayingField(int distance, int stones) {
        Field lastField = null;
        for (int playerId = 0; playerId < this.players.length; playerId++) {
            Field field = new Field(lastField == null ? 1 : lastField.fieldId + stones + 1, Speciality.HOUSE_EXIT, this.players[playerId], stones);
            if (lastField != null) {
                lastField.setNextField(field);
            }
            lastField = field;
            if (this.playingFieldStart == null) {
                this.playingFieldStart = field;
            }
            for (int i = 0; i < distance-2; i++) {
                field = new Field(lastField.fieldId + 1);
                lastField.setNextField(field);
                lastField = field;
            }
            field = new Field(lastField.fieldId + 1, Speciality.GOAL_ENTRY, this.players[(this.players.length + playerId + 1) % this.players.length], stones);
            lastField.setNextField(field);
            lastField = field;
        }
        assert lastField != null;
        lastField.setNextField(this.playingFieldStart);

        Field actualField = this.playingFieldStart;
        while(actualField.nextField != this.playingFieldStart){
            System.out.println(actualField);
            if (actualField.goal != null) {
                Field actualGoalField = actualField.goal;
                while(actualGoalField.nextField != null) {
                    System.out.println(actualGoalField);
                    actualGoalField = actualGoalField.nextField;
                }
            }
            actualField = actualField.nextField;
        }
        System.out.println(actualField);
    }
}
