package src;

import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.*;

public class Player implements Steppable {
    public String color;
    public String name;
    public int order;
    public AgentType[] agentTypes;
    /**
     * Players goal Fields.
     */
    public List<Field> goals = new ArrayList<>();
    /**
     * These are the fields on this player side or ?
     */
    public List<Field> fields = new ArrayList<>();
    public int homeStones = 0;

    public boolean hasWon = false;

    public Player(String color, String name, int order, AgentType[] agentTypes) {
        this.color = color;
        this.name = name;
        this.order = order;
        this.agentTypes = agentTypes;
    }

    @Override
    public String toString() {
        return "Player{" +
                "color='" + color + " " + Game.ANSI_RESET + '\'' +
                ", name='" + name + '\'' +
                ", homeStones='" + homeStones + '\'' +
                '}';
    }

    public void afterSetup(int playerStones) {
        this.homeStones = playerStones;
    }

    public String getBoard() {
        StringBuilder board = new StringBuilder();
        for (int i = this.goals.size() - 1; i >= 0; i--) {
            board.append(this.goals.get(i).getChar()).append("|");
//            board.append(this.goals.get(i).getChar()).append(this.goals.get(i).formerField.fieldId + ":" + this.goals.get(i).fieldId).append("|");
        }
        board.append(color).append(homeStones).append(Game.ANSI_RESET).append("|");
        for (Field field : this.fields) {
            board.append(field.getChar()).append("|");
//            board.append(field.getChar()).append(field.formerField.fieldId + ":" + field.fieldId).append("|");
        }
        return board.toString();
    }

    @Override
    public void step(SimState state) {
        //A Player who has Won doesn't get to play anymore FeelsBadMan
        if (this.hasWon) {
            return;
        }

        Game game = (Game) state;
        boolean itsYourTurn = true;
        int rollsThisTurn = 0;

        //Since the player wasn't done, we get to go again next time!
        game.schedule.scheduleOnce(this, this.order);
        while (itsYourTurn) {
            List<Field> possibleMoves = game.findAllStones(this);

            possibleMoves = game.removeDoneStones(this, possibleMoves);

            int roll = state.random.nextInt(6) + 1;
            System.out.print(color + " " + Game.ANSI_RESET + roll);
            List<Field> occupiedFields = game.findAllStones(this);
            rollsThisTurn++;

            // All Stones outside house, but all are done means we have won!
            if (game.findAllStones(this).size() == Game.STONES && possibleMoves.size() == 0) {
                this.hasWon = true;
                game.winners.add(this);
                return;
            }

            //No pieces outside house OR all stones outside are done
            if (game.findAllStones(this).size() == 0 || possibleMoves.size() == 0) {
                //Then you can roll twice again
                if (roll != 6 && rollsThisTurn < 3) {
                    continue; //Roll again!
                }
            }

            //Has one in the house, rolled a 6 and doesn't block itself
            if (occupiedFields.size() < Game.STONES && roll == 6 && game.findHouseExit(this).occupation != this) {
                game.findHouseExit(this).occupation = this;
                continue; //Do another roll!
            }

            //Has a stone on the House Exit which needs to be moved
            if (game.findHouseExit(this).occupation == this) {
                Field f = game.findHouseExit(this);
                while (!game.validMove(roll, f, this)) {
                    f = f.getNFurtherField(roll, this); //Find the next piece we have to move to clear house
                }
                game.moveStone(roll, f, this);
                itsYourTurn = false;
                break;
            }

            //Find all ways to kill another stone of another player
            List<Field> killMoves = new ArrayList<>();
            for (int i = 0; i < occupiedFields.size(); i++) {
                if (occupiedFields.get(i).getNFurtherField(roll, this) != null && occupiedFields.get(i).getNFurtherField(roll, this).occupation != this) {
                    killMoves.add(occupiedFields.get(i));
                }
            }
            if (killMoves.size() > 0) {
                possibleMoves = killMoves;
            }

            for (int i = 0; i < possibleMoves.size(); i++) {
                if (!game.validMove(roll, possibleMoves.get(i), this)) {
                    possibleMoves.remove(i); //Remove invalid moves
                    i--;
                }
            }

            //No turn possible
            if (possibleMoves.size() == 0) {
                itsYourTurn = false;
                break;
            }

            Field chosenMove = chooseMove(roll, possibleMoves, game);
            game.moveStone(roll, chosenMove, this);
            if (roll != 6) {
                itsYourTurn = false;
            }


            //Has at least one thing out of house


        }
//        System.out.println(game.findAllStones(this));

    }

    public Field chooseMove(int roll, List<Field> possibleMoves, Game game) {
        HashMap<Field, Integer> moveStrength = new HashMap<>();
        for (Field possibleMove : possibleMoves) {
            moveStrength.put(possibleMove, 0);
        }
        for (AgentType agentType : this.agentTypes) {
            moveStrength = multiplyStrength(moveStrength);
            moveStrength = switch (agentType) {
                case AGRESSIVE -> agressiveMove(game, moveStrength, roll);
                case DEFENSIVE -> defensiveMove(game, moveStrength, roll);
            };
        }
        return maxMoveStrength(moveStrength);
    }

    public Field maxMoveStrength(HashMap<Field, Integer> moveStrength) {
        if(moveStrength.size()==1) {
            return moveStrength.keySet().iterator().next();
        }
        int maxMoveStrength = 0;
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            maxMoveStrength = maxMoveStrength > entry.getValue() ? maxMoveStrength : entry.getValue();
        }
        HashMap<Field, Integer> newMoveStrength = new HashMap<>();
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            if (entry.getValue() == maxMoveStrength){
                newMoveStrength.put(entry.getKey(), entry.getValue());
            }
        }
        List<Field> fields = new ArrayList();
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            fields.add(entry.getKey());
        }
        return fields.get(new Random().nextInt(fields.size()));
    }

    private HashMap<Field, Integer> multiplyStrength(HashMap<Field, Integer> moveStrength) {
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            entry.setValue(entry.getValue() * 10);
        }
        return moveStrength;
    }

    private HashMap<Field, Integer> agressiveMove(Game game, HashMap<Field, Integer> moveStrength, int roll) {
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            int furtherPlayers = 0;
            for (int i = 1;i<=6;i++) {
                Field furtherField = entry.getKey().getNFurtherField(roll+i, this);
//                Player occupation = entry.getKey().getNFurtherField(roll+i, this).occupation;
                if (furtherField != null && furtherField.occupation != null && furtherField.occupation != this) {
                    furtherPlayers++;
                }
            }
            entry.setValue(entry.getValue() + furtherPlayers);
        }
        return moveStrength;
    }

    private HashMap<Field, Integer> defensiveMove(Game game, HashMap<Field, Integer> moveStrength, int roll) {
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            int formerPlayers = 0;
            for (int i = 1;i<=6;i++) {
                Player occupation = entry.getKey().getNFurtherField(roll-i, this).occupation;
                if (occupation != null && occupation != this) {
                    formerPlayers++;
                }
            }
            entry.setValue(entry.getValue() + 6 - formerPlayers);
        }
        return moveStrength;
    }
}
