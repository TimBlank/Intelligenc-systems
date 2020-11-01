package Aufgabe1.src;

public class Field {
    public int fieldId;
    public Field nextField;
    private Player occupation = null;
    private Speciality speciality = null;
    // Jedes Spezielle Feld hat nur einen
    private Player specialPlayer = null;
    // if(goal != null) dann specialPlayer == actualPlayer testen
    public Field goal = null;

    // non special field
    public Field(int fieldId) {
        this.fieldId = fieldId;
        System.out.printf("%02d. FIELD%n", fieldId);
    }

    // special fields
    public Field(int fieldId, Speciality speciality, Player specialPlayer, int stones) {
        this.fieldId = fieldId;
        this.specialPlayer = specialPlayer;
        this.speciality = speciality;
        if (speciality == Speciality.GOAL_ENTRY) {
            System.out.printf("%02d. GOAL_ENTRY %s%n", fieldId, specialPlayer.name);
            goal = new Field(fieldId + 1, Speciality.GOAL_FIELD, specialPlayer, stones);
        } else if (speciality == Speciality.GOAL_FIELD) {
            System.out.printf("%02d. Goal%d %s%n", fieldId, stones, specialPlayer.name);
            this.specialPlayer = specialPlayer;
            if (stones > 1) {
                nextField = new Field(fieldId + 1, Speciality.GOAL_FIELD, specialPlayer, stones - 1);
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
        return occupation;
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
                ", occupation=" + occupation +
                ", speciality=" + speciality +
                ", specialPlayer=" + (specialPlayer == null ? "\"\"" : specialPlayer.name) +
                ", goal=" + (goal == null ? "\"\"" : String.valueOf(goal.fieldId)) +
                '}';
    }
}
