package src;

public class Field {
    public int fieldId;
    public Field nextField;
    public Speciality speciality = null;
    // Jedes Spezielle Feld hat nur einen
    public Player specialPlayer = null;
    // if(goal != null) dann specialPlayer == actualPlayer testen
    public Field goal = null;

    //Actual player who has his shit on this field
    public Player occupation;

    // non special field
    public Field(int fieldId, Game game) {
        this.fieldId = fieldId;
        game.setOfAllFields.add(this);
        //System.out.printf("%02d. FIELD%n", fieldId);
    }

    // special fields
    public Field(int fieldId, Speciality speciality, Player specialPlayer, int playerStones, Game game) {
        this.fieldId = fieldId;
        game.setOfAllFields.add(this);
        this.specialPlayer = specialPlayer;
        this.speciality = speciality;
        if (speciality == Speciality.GOAL_ENTRY) {
            //System.out.printf("%02d. GOAL_ENTRY %s%n", fieldId, specialPlayer.name);
            goal = new Field(fieldId, Speciality.GOAL_FIELD, specialPlayer, playerStones, game);
        } else if (speciality == Speciality.GOAL_FIELD) {
            //System.out.printf("%02d. Goal%d %s%n", fieldId, playerStones, specialPlayer.name);

            this.specialPlayer = specialPlayer;
            specialPlayer.goals.add(this);
            if (playerStones > 1) {
                nextField = new Field(fieldId + 1, Speciality.GOAL_FIELD, specialPlayer, playerStones - 1, game);
            }
        } else if (speciality == Speciality.HOUSE_EXIT) {
            //System.out.printf("%02d. HOUSE_EXIT %s%n", fieldId, specialPlayer.name);
        }
    }

    public void setNextField(Field nextField) {
        if (nextField != null) {
            this.nextField = nextField;
        }
    }


    public String getColor() {
        if (occupation == null) {
            return Game.ANSI_RESET;
        } else {
            return this.occupation.color;
        }
    }

    public String getChar() {
        return getColor() + "_" + Game.ANSI_RESET;
    }

    public Field getNFurtherField(int n, Player player) {
        Field f = this;
        for (int i = 0; i < n; i++) {
            //Enter Goal if passing own Goal_Entry
            if (specialPlayer == player && speciality == Speciality.GOAL_ENTRY) {
                f = f.goal;
            } else {
                f = f.nextField;
            }
            //When there is no field in the number further (goal end)
            if (f == null) {
                return null;
            }
        }
        return f;
    }

    @Override
    public String toString() {

        return "Field{" +
                "fieldId=" + fieldId +
                ", nextField=" + (nextField == null ? "\"\"" : nextField.fieldId) +
                ", occupation=" + occupation +
                ", speciality=" + speciality +
                ", specialPlayer=" + (specialPlayer == null ? "\"\"" : specialPlayer.name) +
                ", goal=" + (goal == null ? "\"\"" : String.valueOf(goal.fieldId)) +
                '}';
    }
}
