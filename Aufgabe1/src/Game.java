package src;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import sim.engine.SimState;

public class Game extends SimState {

    //Someones House exit
    public Field playingFieldStart;

    //Convenience Set of all Fields
    public Set<Field> setOfAllFields = new HashSet<>();

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

    public static final int DISTANCE = 10;
    public static final int STONES = 4;

    Player[] players = {
            new Player(ANSI_RED_BACKGROUND, "EINS"),
            new Player(ANSI_GREEN_BACKGROUND, "ZWEI"),
            new Player(ANSI_BLUE_BACKGROUND, "DREI"),
            new Player(ANSI_YELLOW_BACKGROUND, "VIER"),
    };

    public Game(long seed) {
        super(seed);

    }

    public void start() {
        super.start();
        setupPlayingField(DISTANCE, STONES);
        this.playingFieldStart.occupation = players[0];

        schedule.scheduleRepeating(players[0]);
    }

    public static void main(String[] args) {
        Game game = new Game(12);

        doLoop(Game.class, args);
        System.exit(0);
    }

    public void setupPlayingField(int distance, int playerStones)  {
        Field lastField = null;
        for (int playerId = 0; playerId < this.players.length; playerId++) {
            Field field = new Field(lastField == null ? 1 : lastField.fieldId + playerStones, Speciality.HOUSE_EXIT, this.players[playerId], playerStones, this);

            if (lastField != null) {
                lastField.setNextField(field);
            }
            lastField = field;
            if (this.playingFieldStart == null) {
                this.playingFieldStart = field;
            }
            for (int i = 0; i < distance - 1; i++) {
                field = new Field(lastField.fieldId + 1, this);
               ;
                lastField.setNextField(field);
                lastField = field;
            }
            field = new Field(lastField.fieldId + 1, Speciality.GOAL_ENTRY, this.players[(this.players.length + playerId + 1) % this.players.length], playerStones, this);

            lastField.setNextField(field);
            lastField = field;
        }
        assert lastField != null;
        lastField.setNextField(this.playingFieldStart);

        //System.out.println(this.playingFieldStart);
        Field actualField = this.playingFieldStart.nextField;
        while (actualField != this.playingFieldStart) {
            //System.out.println(actualField);
            if (actualField.goal != null) {
                Field actualGoalField = actualField.goal;
                while (actualGoalField.nextField != null) {
                    //System.out.println(actualGoalField);
                    actualGoalField = actualGoalField.nextField;
                }
            }
            actualField = actualField.nextField;
        }
        //System.out.println(this.getBoard());
        //this.play();
       for(Field f: setOfAllFields) {
           System.out.println("Field "+f.fieldId + " is "+f.specialPlayer + "'s "+f.speciality);
       }
    }

    /**
     * Returns list with all occupied places BY player
     * @param player
     * @return
     */
    public List<Field> findAllStones(Player player) {
        List<Field> a = new ArrayList<>();
        for(Field f : setOfAllFields) {
            if(f.occupation == player) {
                a.add(f);
            }
        }
        return a;
    }

    /**
     * Finds Players House exit
     * @param player
     * @return
     */
    public Field findHouseExit(Player player) {
        for(Field f : setOfAllFields) {
            if(f.specialPlayer == player && f.speciality == Speciality.HOUSE_EXIT) {
               return f;
            }
        }
        return null;
    }

    /**
     * Moves a stone forward by roll
     * Should maybe check rules too in the future
     * Returns true when moved successfully
     * @param roll
     * @param stone
     * @param player
     * @return
     */
    public boolean moveStone(int roll, Field stone, Player player) {
        if(validMove(roll, stone, player)) {
            Field newField = stone.getNFurtherField(roll, player);
            stone.occupation = null;
            newField.occupation = player;
            return true;
        }
        return false;
    }

    /**
     * Checks if you can move a stone *roll* further
     * @param roll
     * @param stone
     * @param player
     * @return
     */
    public boolean validMove(int roll, Field stone, Player player) {
        if(stone.getNFurtherField(roll, player) != null) {
            if(stone.getNFurtherField(roll, player).occupation != player) {
                return true;
            }
        }

        return false;
    }

    public String getBoard() {
        Field f = playingFieldStart.nextField;
        while(f != playingFieldStart) {
            System.out.print(f.fieldId + "|"+f.occupation.color + " ");
        }


        return "Board:";
    }

    public static int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }
}
