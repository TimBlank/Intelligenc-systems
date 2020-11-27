import java.util.ArrayList;

/**
 * A Field is a 2D Array with singular Characters signifying "solved" content.
 * The Array 0,0 point is at the TOP LEFT, because thats where the CSV Files 0,0 is,
 * giving us a great y is down, x is right coordinate system
 * Tent T
 * Tree (N, S, E, W) when connected to it's Tent, t when unsolved. According to rules, each tree has one attached.
 * Grass G , something that is never a tent/tree
 * Unspecified " " or pretty much whatever
 */
public class State {

    ArrayList<ArrayList<String>> field;


    //The required Tents in the column
    int[] columnDemand;
    //required Tents in the row
    int[] rowDemand;





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
     * If a square has no adjacent tree, it cannot contain a Tent, so this Method is useful
     * @param x
     * @param y
     * @return
     */
    public boolean hasAdjacentTree(int x, int y) {
        if(x > 0 && isConfirmed(x-1,y)) {
            return true;
        }
        if(isConfirmed(x+1, y)) {
            return true;
        }
        if(y > 0 && isConfirmed(x, y-1)) {
            return true;
        }
        if(isConfirmed(x, y+1)) {
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
        return c.equals("N") || c.equals("S") || c.equals("E") || c.equals("W") || c.equals("t") || c.equals("T");
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
        return c.equals("N") || c.equals("S") || c.equals("E") || c.equals("W") || c.equals("t") ;
    }


    public void set(int x, int y, String value) {
        field.get(x).set(y, value);
    }

    public void setTent(int x, int y, int treeX, int treeY) {
        field.get(x).set(y, "T");
        //Tree is left of Tent
        if(x > treeX) {
            this.set(treeX, treeY, "E");
        }
        if(x < treeX) {
            this.set(treeX, treeY, "W");
        }
        if(y > treeY) {
            this.set(treeX, treeY, "N");
        }
        if(y < treeY) {
            this.set(treeX, treeY, "S");
        }

    }

}
