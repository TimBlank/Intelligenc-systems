package src;


import java.util.*;

import sim.engine.SimState;
import sim.engine.Steppable;

/**
 * State of the Game.
 * main handles setup and replay things
 */
public class Game extends SimState {

    //Got to start a double linked list (which is a ring) somewhere
    public Field playingFieldStart;

    //Convenience Set of all Fields
    public Set<Field> setOfAllFields = new HashSet<>();

    //Was used to give players colored figures and display the actual game running
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
    /**
     * Schlagzwang?
     */
    public static final boolean MUST_KILL = true;

    /**
     * The list of Players playing each game. This doesn't change, but could be made to change when setting up
     * each game if wanted. (Between new Game and game.start)
     */
    public Player[] players = {
            new LastStoneMover(ANSI_RED_BACKGROUND, "LastStoneFirst", 1),
            new Player(ANSI_YELLOW_BACKGROUND, "Random", 2),
            new Player(ANSI_BLUE_BACKGROUND, "Random2", 3),
            new FirstStoneMover(ANSI_GREEN_BACKGROUND, "BestStoneFirst", 4)
    };

    /**
     * Ordered List of winners. First in list finished first.
     */
    List<Player> winners = new ArrayList<>();

    public Game(long seed) {
        super(seed);
    }

    public void start() {
        super.start();
        setupPlayingField(DISTANCE, STONES);

        //Put each player into a queue to do a turn once
        for (int i = 0; i < players.length; i++) {
            schedule.scheduleOnce(players[i], i + 1);
        }
        // This is just a step that happens after all players did things.
        // For debugging, but unused now
        schedule.scheduleOnce(new Steppable() {
            public void step(SimState state) {
                if (winners.size() == 0) {
                    schedule.scheduleOnce(this, players.length);
                }
            }
        }, players.length);
    }

    public static void main(String[] args) {

        /**
         * How many runs you want
         */
        int amountOfRuns = 100000;
        Statistics statistics = new Statistics();
        System.out.println("I'm not dead, I'm just doing things.");
        for (int i = 0; i < amountOfRuns; i++) {
//        for (int i = 0; i < amountOfRuns; i++) {
            if (i>0 && i % 1000 == 0) {
                System.out.println(statistics);
            }
            //Start 1 new game, run it until it terminates, add it's stats, quit
            Game game = new Game(System.currentTimeMillis());
            game.start();
            while (true) {
                if (!game.schedule.step(game) || game.schedule.getSteps() > 250_000) break;
            }
            game.finish(statistics);

        }
        System.out.println(statistics.rankings.size() + " games were played");
        //Print some stats
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

    /**
     * Sets up the double linked ring of fields, complete with markers for house exists, goal entries and goals
     * @param distance
     * @param playerStones
     */
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
     * Finds a Players House exit
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
     * Removes stones that are as far as they can go from supplied list
     * Mainly for finding out that a player has put all stones on the field as far up their goal as they can for rolling
     * 3 times when getting another one out of the house
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
     * Foor strategies by players that care about such things.
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

}
