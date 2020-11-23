package src;


import java.util.*;

import sim.engine.SimState;
import sim.engine.Steppable;

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

    public Player[] players = {
            new Player(ANSI_RED_BACKGROUND, "AGRESSIVE", 1, new AgentType[]{AgentType.AGRESSIVE}),
            new Player(ANSI_YELLOW_BACKGROUND, "WorstStoneFirst", 2, new AgentType[]{AgentType.WORSTSTONE}),
            new Player(ANSI_BLUE_BACKGROUND, "DEFENSIVE", 3, new AgentType[]{AgentType.DEFENSIVE}),
            new Player(ANSI_GREEN_BACKGROUND, "BestStoneFirst", 4, new AgentType[]{AgentType.BESTSTONE}),
    };

    List<Player> winners = new ArrayList<>();

    public Game(long seed) {
        super(seed);
    }

    public void start() {
        super.start();
        setupPlayingField(DISTANCE, STONES);

        for (int i = 0; i < players.length; i++) {
            schedule.scheduleOnce(players[i], i + 1);
        }
        schedule.scheduleOnce(new Steppable() {
            public void step(SimState state) {
//                System.out.println(((Game) state).getBoard());
                if (winners.size() == 0) {
                    schedule.scheduleOnce(this, players.length);
                }
            }
        }, players.length);
    }

    public static void main(String[] args) {

        /**
         * How many runs you want of this configuration
         */
        int amountOfRuns = 10000;
        Statistics statistics = new Statistics();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//        for (int i = 0; i < amountOfRuns; i++) {
            if (i>0 && i % 1000 == 0) {
                System.out.println(statistics);
            }
            Game game = new Game(System.currentTimeMillis());
            game.start();
            while (true) {
                if (!game.schedule.step(game) || game.schedule.getSteps() > 250_000) break;
            }
            game.finish(statistics);
        }
        System.out.println(statistics.rankings.size() + " games were played");
        int amountOfFinishers = 0;
        for (HashMap<Player, Integer> game : statistics.rankings) {
            amountOfFinishers += game.size();
        }
        System.out.println(amountOfFinishers + " total Players finished!");
        // Print complete statistic
        System.out.println(statistics);

        System.exit(0);
    }

    public void finish(Statistics statistics) {
        // Add to statistics
        statistics.add(this);
        //Actually finish
        this.finish();
    }

    public void setupPlayingField(int distance, int playerStones) {
        Field lastField = null;
        for (int playerId = 0; playerId < this.players.length; playerId++) {
            Field field = new Field(lastField == null ? 1 : lastField.fieldId + playerStones, Speciality.HOUSE_EXIT, this.players[playerId], playerStones, this, lastField != null ? lastField.formerField : null);
            this.players[playerId].fields.add(field);

            if (lastField != null) {
                lastField.setNextField(field);
            }
            lastField = field;
            if (this.playingFieldStart == null) {
                this.playingFieldStart = field;
            }
            for (int i = 0; i < distance - 1; i++) {
                field = new Field(lastField.fieldId + 1, this, lastField);
                this.players[playerId].fields.add(field);
                ;
                lastField.setNextField(field);
                field.setFormerField(lastField);
                lastField = field;
            }
            field = new Field(lastField.fieldId + 1, Speciality.GOAL_ENTRY, this.players[(this.players.length + playerId + 1) % this.players.length], playerStones, this, lastField);

            lastField.setNextField(field);
            lastField = field;
        }
        assert lastField != null;
        lastField.setNextField(this.playingFieldStart);
        this.playingFieldStart.setFormerField(lastField);

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
        for (Field f : setOfAllFields) {
            // System.out.println("Field " + f.fieldId + " is " + f.specialPlayer + "'s " + f.speciality);
        }
    }

    /**
     * Returns list with all occupied places BY player
     *
     * @param player
     * @return
     */
    public List<Field> findAllStones(Player player) {
        List<Field> fields = new ArrayList<>();
        for (Field field : setOfAllFields) {
            if (field.occupation == player) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * Finds Players House exit
     *
     * @param player
     * @return
     */
    public Field findHouseExit(Player player) {
        for (Field f : setOfAllFields) {
            if (f.specialPlayer == player && f.speciality == Speciality.HOUSE_EXIT) {
                return f;
            }
        }
        return null;
    }

    /**
     * Moves a stone forward by roll
     * Should maybe check rules too in the future
     * Returns true when moved successfully
     *
     * @param roll
     * @param field
     * @param player
     * @return
     */
    public boolean moveStone(int roll, Field field, Player player) {
//        System.out.println(roll + player.color + " " + this.ANSI_RESET + " " +  this.getBoard());
        if (validMove(roll, field, player)) {
            Field newField = field.getNFurtherField(roll, player);
            field.occupation = null;
            newField.occupation = player;
            return true;
        }
        return false;
    }

    /**
     * Checks if you can move a stone *roll* further
     *
     * @param roll
     * @param stone
     * @param player
     * @return
     */
    public boolean validMove(int roll, Field stone, Player player) {
        if (stone.getNFurtherField(roll, player) != null) {
            if (stone.getNFurtherField(roll, player).occupation != player) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes stones that are as far as they can go
     *
     * @param player
     * @param fields
     */
    public List<Field> removeDoneStones(Player player, List<Field> fields) {
        for (int i = player.goals.size() - 1; i > -1; i--) {
            if (player.goals.get(i).occupation == player) {
                fields.remove(fields.indexOf(player.goals.get(i)));
            } else {
                //Once there is an empty field, everything behind isn't done yet
                break;
            }
        }
        return fields;
    }


    /**
     * Gives you a list of Stones, ORDERED by how far they've come from your house exit
     * The larger the index, the farther the Stone has travelled
     *
     * @param player
     * @return
     */
    public ArrayList<Field> getOrderedStonesList(Player player) {
        Field from = findHouseExit(player);
        ArrayList<Field> listOfStones = new ArrayList<>(Game.STONES);
        int distanceTravelled = 0;
        Field origin = from;
        do {
            if (from.occupation == player) {
                listOfStones.add(from);
            }
            from = from.nextField;
        } while (from != origin);

        for (Field g : player.goals) {
            if (g.occupation == player) {
                listOfStones.add(g);
            }
        }

        return listOfStones;
    }

    public String getBoard() {
        StringBuilder board = new StringBuilder();
        for (Player player : this.players) {
            board.append("\n").append(player.getBoard(this));
        }
        return "Board:" + board;
    }


    //Implements Distance for each Player Stone to all other Stones
    public List<Field> enemyDistance(List<Field> possibleMoves, Game game) {
        List<Field> enemyPosition = new ArrayList<>();
        //TODO alle gegnerischen Spieler auswählen
        Player Enemy = new Player("\u001B[30m", "Enemy", 100, null);
        for (Field f : game.setOfAllFields) {
            if (f.occupation == Enemy) {
                enemyPosition.add(f);
            }
        }

        // All Stones which could theoretically move
        int movableStones = STONES;


        for (int i = 0; i < movableStones; i--) {
            //game.
            //TODO alle Steine mit der Distance zum nächsten gegner abgleichen

            //TODO nur wert mit geringster Disantz zum gegner speichern

        }
        //possibleMoves has only the most aggressiv move
        return possibleMoves;
    }

}
