import java.util.ArrayList;

/**
 * A Field is a 2D Array with singular Characters signifying "solved" content.
 * The Array 0,0 point is at the TOP LEFT, because thats where the CSV Files 0,0 is,
 * giving us a great y is down, x is right coordinate system
 * Tent T
 * Tree (N, S, E, W) when connected to it's Tent, t when unsolved. According to rules, each tree has one attached.
 * Grass G , something that is never a tent/tree
 * Unspecified " "
 */
public class State {

    public static final String TREE = "t";
    public static final String GRASS = "_";
    public static final String UNKNOWN = " ";
    public static final String TENT = "X";
    public static final String NORTH_TREE = "N";
    public static final String SOUTH_TREE ="S";
    public static final String WEST_TREE = "W";
    public static final String EAST_TREE = "E";

    ArrayList<ArrayList<String>> field;


    //The required Tents in the column
    int[] columnDemand;
    //required Tents in the row
    int[] rowDemand;

    //If the state got changed due to a thing
    boolean hasChanged = false;




    public State() {
        field = new ArrayList<>();

    }

    public void showField() {
        System.out.println("Field!");
        System.out.print("x  ");
        for(int i = 0; i< columnDemand.length; i++) {
            System.out.print(columnDemand[i]+ " ");
        }
        System.out.println();
        for(int y= 0; y< rowDemand.length; y++) {
            if(rowDemand[y] > 9) {
                System.out.print(rowDemand[y]+ " ");
            } else {
                System.out.print(rowDemand[y]+ "  ");
            }

            for(int x = 0; x <field.size(); x++) {
                System.out.print(field.get(x).get(y)+ " ");
            }
            System.out.println();
        }
    }

    /**
     * If a square has an adjacent tree.
     * @param x
     * @param y
     * @return
     */
    public boolean hasAdjacentTree(int x, int y) {
        if(x > 0 && isConfirmed(x-1,y)) {
            return true;
        }
        if(x+1 < field.size() && isConfirmed(x+1, y)) {
            return true;
        }
        if(y > 0 && isConfirmed(x, y-1)) {
            return true;
        }
        if(y+1 < field.get(x).size() && isConfirmed(x, y+1)) {
            return true;
        }
        return false;
    }



    /**
     * If a given field is already solved. Performs boundary check
     * @param x
     * @param y
     * @return
     */
    public boolean isConfirmed(int x, int y) {
        if(x < 0 || x >= field.size() || y < 0 || y >= field.get(x).size()) {
            return false;
        }
        String c = field.get(x).get(y);
        return c.equals(NORTH_TREE) || c.equals(SOUTH_TREE) || c.equals(EAST_TREE) ||
                c.equals(WEST_TREE) || c.equals(TREE) || c.equals(TENT);
    }

    /**
     * If a given field is a Tree. Performs boundary check
     * @param x
     * @param y
     * @return
     */
    public boolean isTree(int x, int y) {
        if(x < 0 || x >= field.size() || y < 0 || y >= field.get(x).size()) {
            return false;
        }
        String c = field.get(x).get(y);
        return c.equals(NORTH_TREE) || c.equals(SOUTH_TREE) || c.equals(EAST_TREE) || c.equals(WEST_TREE) || c.equals(TREE) ;
    }

    public boolean is(int x, int y, String content) {
        if(x < 0 || x >= field.size() || y < 0 || y >= field.get(x).size()) {
            return false;
        }
        return content.equals(field.get(x).get(y));
    }

    public void set(int x, int y, String value) {
        field.get(x).set(y, value);
    }

    public void setTent(int x, int y, int treeX, int treeY) {
        field.get(x).set(y, TENT);
        hasChanged= true;

        //Tree is left of Tent
        if(x > treeX) {
            this.set(treeX, treeY, EAST_TREE);
        }
        if(x < treeX) {
            this.set(treeX, treeY, WEST_TREE);
        }
        if(y > treeY) {
            this.set(treeX, treeY, SOUTH_TREE);
        }
        if(y < treeY) {
            this.set(treeX, treeY, NORTH_TREE);
        }
        //Tents can't have any other Tent around it
        for(int i= x-1; i<= x+1; i++) {
            for(int j= y-1; j <= y+1; j++) {

                if(i > -1 && i < field.size() && j > -1 && j < field.get(i).size()) {
                    if (field.get(i).get(j).equals(UNKNOWN)) {
                        field.get(i).set(j, GRASS);
                    }
                }
            }
        }

    }
    public void setTent(int x, int y) {
        field.get(x).set(y, TENT);
        hasChanged = true;

        //Tents can't have any other Tent around it
        for(int i= x-1; i<= x+1; i++) {
            for(int j= y-1; j <= y+1; j++) {

                if(i > -1 && i < field.size() && j > -1 && j < field.get(i).size()) {
                    if (field.get(i).get(j).equals(UNKNOWN)) {
                        field.get(i).set(j, GRASS);
                    }
                }
            }
        }
    }

}
