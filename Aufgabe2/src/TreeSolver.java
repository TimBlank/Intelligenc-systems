import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TreeSolver {


    public static String FILE  = "Aufgabe2/tents_trees_0.csv";

    public static void main(String[] args) {
        TreeSolver tS = new TreeSolver();
        State state = tS.readFile(FILE);
        state.showField();
        tS.markImpossibleFieldsAsGrass(state);
        state.showField();
    }

    /**
     * Reads given CSV File. Root directory is the one OVER Aufgabe2, so a path could be Aufgabe2/tents_trees_0.csv
     * @param filePath
     * @return
     */
    public State readFile(String filePath) {
        try {
            //Open File, make new Field
            State treeState = new State();
            BufferedReader bR = new BufferedReader(new FileReader(filePath));
            String line = bR.readLine();

            //Parse first line as demands for column
            String[] yDemand = line.split(",", -1);
            int[] actualYNumbers = new int[yDemand.length-1];
            for(int i=1; i< yDemand.length; i++) {
                actualYNumbers[i-1] = Integer.parseInt(yDemand[i]);
                treeState.field.add(new ArrayList<>());
            }
            treeState.columnDemand = actualYNumbers;

            //Parse each other line for 1st the demand for the row and trees
            ArrayList<Integer> xDemand = new ArrayList<>();
            line = bR.readLine();
            int y = 0; //Counter for row we are going down
            while(line != null) {
                String[] lineTokens = line.split(",", -1);
                //Save the demand for this row
                xDemand.add(Integer.parseInt(lineTokens[0]));
                for(int i=1; i< lineTokens.length; i++) {
                    //Add a coordinate for this
                    if(lineTokens[i].equals("t")) {
                        treeState.field.get(i-1).add("t");
                    } else {
                        treeState.field.get(i-1).add(" ");
                    }
                }
                System.out.println();
                line = bR.readLine();
                y++;
            }
            //Save row Demands
            treeState.rowDemand = new int[xDemand.size()];
            for(int i=0; i< xDemand.size(); i++) {
                treeState.rowDemand[i] = xDemand.get(i);
            }

            return treeState;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Impossible cells can't have tents, it would be
     * *impossible*
     * @param state
     */
    public void markImpossibleFieldsAsGrass(State state) {

        //Go through columns
        for(int i=0; i<state.field.size(); i++) {
            if(state.columnDemand[i] < 1) { //This column CAN NOT have trees

                for(int j=0; j< state.field.get(i).size(); j++) {
                    if(!state.isConfirmed(i,j)) {
                        state.set(i,j, "G");
                    }
                }
            }
        }
        //Go through rows
        for(int i=0; i<state.field.get(0).size(); i++) {
            if(state.rowDemand[i] < 1) { //This column CAN NOT have trees

                for(int j=0; j< state.field.size(); j++) {
                    if(!state.isConfirmed(j,i)) {
                        state.set(j,i, "G");
                    }
                }
            }
        }
        //Since each Tree has one tent near and there are as many trees as tents, each tent HAS to be next to a tree
        //This means that each place not near a tree is Grass
        for(int i=0; i< state.field.size(); i++) {
            for(int j=0; j< state.field.get(i).size(); j++) {
                if(!state.isTree(i,j)) {
                    boolean hasTree = false;
                    hasTree = hasTree || state.isTree(i-1,j);
                    hasTree = hasTree || state.isTree(i,j-1);
                    hasTree = hasTree || state.isTree(i+1,j);
                    hasTree = hasTree || state.isTree(i,j+1);
                    if(!hasTree) {
                        state.set(i,j, "G");
                    }
                }
            }

        }



    }




}
