package src;

/**
 * Represents a field on the Board
 */
public class Field {
    public int fieldId;
    // Double links
    public Field nextField;
    public Field formerField;
    // If this field is a special thing, house Exit or such
    public Speciality speciality = null;
    // Every speciality refers to a player
    public Player specialPlayer = null;
    // This is a link to the first goal field, should this field lead into a goal
    public Field goal = null;

    //Actual player who has his shit on this field
    public Player occupation;

    // non special field
    public Field(int fieldId, Game game, Field formerField) {
        this.fieldId = fieldId;
        this.formerField = formerField;
        game.setOfAllFields.add(this);
        //System.out.printf("%02d. FIELD%n", fieldId);
    }

    // special fields
    public Field(int fieldId, Speciality speciality, Player specialPlayer, int playerStones, Game game, Field formerField) {
        this.fieldId = fieldId;
        game.setOfAllFields.add(this);
        this.specialPlayer = specialPlayer;
        this.speciality = speciality;
        this.formerField = formerField;
        if (speciality == Speciality.GOAL_ENTRY) {
            //System.out.printf("%02d. GOAL_ENTRY %s%n", fieldId, specialPlayer.name);
            goal = new Field(fieldId, Speciality.GOAL_FIELD, specialPlayer, playerStones, game, formerField);
        } else if (speciality == Speciality.GOAL_FIELD) {
            //System.out.printf("%02d. Goal%d %s%n", fieldId, playerStones, specialPlayer.name);

            this.specialPlayer = specialPlayer;
            specialPlayer.goals.add(this);
            if (playerStones > 1) {
                nextField = new Field(fieldId + 1, Speciality.GOAL_FIELD, specialPlayer, playerStones - 1, game, this);
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

    public void setFormerField(Field formerField) {
        if (formerField != null) {
            this.formerField = formerField;
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
        return getColor() + (fieldId < 10 ? "0" + fieldId : fieldId) + Game.ANSI_RESET;
//        return getColor() + "_" + Game.ANSI_RESET;
    }

    /**
     * Returns the field that you'd end up at if you rolled a "n" from this field
     * @param n
     * @param player
     * @return
     */
    public Field getNFurtherField(int n, Player player) {
        Field field = this;
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                //Enter Goal if passing own Goal_Entry
                if (specialPlayer == player && speciality == Speciality.GOAL_ENTRY) {
                    field = field.goal;
                } else {
                    field = field.nextField;
                }
                //When there is no field in the number further (goal end)
                if (field == null) {
                    return null;
                }
            }
        } else if (n < 0) {
            for (int i = 0; i > n; i--) {
                field = field.formerField;
            }
        }
        return field;
    }

    /**
     * Returns what you'd need to roll to enter your first goal field
     * @param player
     * @return
     */
    public int getDistanceToGoal(Player player) {
        if (speciality == Speciality.GOAL_FIELD) {
            return 0;
        }
        int distance = 1;
        Field field = getNFurtherField(1, player);
        while (field.speciality != Speciality.GOAL_FIELD) {
            distance++;
            field = field.getNFurtherField(1, player);
        }
        return distance;
    }

    @Override
    public String toString() {

        return "Field{" +
                "fieldId=" + fieldId +
                ", nextField=" + (nextField == null ? "\"\"" : nextField.fieldId) +
                ", formerField=" + (formerField == null ? "\"\"" : formerField.fieldId) +
                ", occupation=" + occupation +
                ", speciality=" + speciality +
                ", specialPlayer=" + (specialPlayer == null ? "\"\"" : specialPlayer.name) +
                ", goal=" + (goal == null ? "\"\"" : String.valueOf(goal.fieldId)) +
                '}';
    }
}
