package Aufgabe1.src;
import java.util.List;

public class Field {
    public int fieldId;
    public Field nextField;
    private Speciality speciality = null;
    // Jedes Spezielle Feld hat nur einen
    private Player specialPlayer = null;
    // if(goal != null) dann specialPlayer == actualPlayer testen
    public Field goal = null;
    public List<Stone> stones;

    // non special field
    public Field(int fieldId, List<Stone> stones) {
        this.fieldId = fieldId;
        this.stones = stones;
        System.out.printf("%02d. FIELD%n", fieldId);
    }

    // special fields
    public Field(int fieldId, Speciality speciality, Player specialPlayer, List<Stone> stones, int playerStones) {
        this.fieldId = fieldId;
        this.stones = stones;
        this.specialPlayer = specialPlayer;
        this.speciality = speciality;
        if (speciality == Speciality.GOAL_ENTRY) {
            System.out.printf("%02d. GOAL_ENTRY %s%n", fieldId, specialPlayer.name);
            goal = new Field(fieldId + 1, Speciality.GOAL_FIELD, specialPlayer, stones, playerStones);
        } else if (speciality == Speciality.GOAL_FIELD) {
            System.out.printf("%02d. Goal%d %s%n", fieldId, playerStones, specialPlayer.name);
            specialPlayer.goals.add(this);
            this.specialPlayer = specialPlayer;
            if (playerStones > 1) {
                nextField = new Field(fieldId + 1, Speciality.GOAL_FIELD, specialPlayer, stones, playerStones - 1);
            }
        } else if (speciality == Speciality.HOUSE_EXIT) {
            System.out.printf("%02d. HOUSE_EXIT %s%n", fieldId, specialPlayer.name);
        }
    }

    public void setNextField(Field nextField) {
        if (nextField != null) {
            this.nextField = nextField;
        }
    }

    public Player getOccupation() {
        for (Stone stone : this.stones) {
            if (stone.field == this) {
                return stone.player;
            }
        }
        return null;
    }

    public String getChar() {
        return (getOccupation() != null ? getOccupation().color : Game.ANSI_RESET) + "_" + Game.ANSI_RESET;
    }

    public String getColor() {
        if (specialPlayer == null) {
            return "#fff";
        } else {
            return specialPlayer.color;
        }
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldId=" + fieldId +
                ", nextField=" + nextField.fieldId +
                ", occupation=" + getOccupation() +
                ", speciality=" + speciality +
                ", specialPlayer=" + (specialPlayer == null ? "\"\"" : specialPlayer.name) +
                ", goal=" + (goal == null ? "\"\"" : String.valueOf(goal.fieldId)) +
                '}';
    }
}
