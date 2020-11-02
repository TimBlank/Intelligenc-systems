import java.util.ArrayList;
import java.util.List;

public class Game {

    public Field playingFieldStart;
    public List<Stone> stones = new ArrayList<>();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    Player[] players = {
            new Player(ANSI_RED_BACKGROUND, "EINS"),
            new Player(ANSI_GREEN_BACKGROUND, "ZWEI"),
            new Player(ANSI_BLUE_BACKGROUND, "DREI"),
            new Player(ANSI_YELLOW_BACKGROUND, "VIER"),
    };

    public static void main(String[] args) {
        Game game = new Game();
        game.setupPlayingField(10, 4);
        System.exit(0);
    }

    public void setupPlayingField(int distance, int playerStones) {
        for (Player player : players) {
            for (int i = 0; i < playerStones; i++) {
                Stone newStone = new Stone(player, null);
                this.stones.add(newStone);
                player.stones.add(newStone);
            }
        }
        Field lastField = null;
        for (int playerId = 0; playerId < this.players.length; playerId++) {
            Field field = new Field(lastField == null ? 1 : lastField.fieldId + playerStones + 1, Speciality.HOUSE_EXIT, this.players[playerId], stones, playerStones);
            this.players[playerId].fields.add(field);
            if (lastField != null) {
                lastField.setNextField(field);
            }
            lastField = field;
            if (this.playingFieldStart == null) {
                this.playingFieldStart = field;
            }
            for (int i = 0; i < distance - 2; i++) {
                field = new Field(lastField.fieldId + 1, stones);
                this.players[playerId].fields.add(field);
                lastField.setNextField(field);
                lastField = field;
            }
            field = new Field(lastField.fieldId + 1, Speciality.GOAL_ENTRY, this.players[(this.players.length + playerId + 1) % this.players.length], stones, playerStones);
            lastField.setNextField(field);
            lastField = field;
        }
        assert lastField != null;
        lastField.setNextField(this.playingFieldStart);

        System.out.println(this.playingFieldStart);
        Field actualField = this.playingFieldStart.nextField;
        while (actualField != this.playingFieldStart) {
            System.out.println(actualField);
            if (actualField.goal != null) {
                Field actualGoalField = actualField.goal;
                while (actualGoalField.nextField != null) {
                    System.out.println(actualGoalField);
                    actualGoalField = actualGoalField.nextField;
                }
            }
            actualField = actualField.nextField;
        }
        System.out.println(this.getBoard());
    }

    public String getBoard() {
        this.stones.get(0).field = this.playingFieldStart;
        StringBuilder board = new StringBuilder();
        for (Player player : this.players) {
            board.append("\n").append(player.getBoard());
        }
        return "Board:" + board;
    }
}
