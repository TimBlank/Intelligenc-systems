package jari;

public class Solver {

    public static String FILE = "Aufgabe2/tents_trees_0.csv";

    public static void main(String[] args) {
        Board board = new Board(FILE);
        board.printBoard();
        board.markImpossibleFieldsAsGrass();
        board.printBoard();
        board.markFullGroupsAsGrass();
        board.printBoard();
        board.markSingleUnknownAsTent();
        board.printBoard();
        board.calcTentProbability();
        board.printBoard();
        board.markZeroTentAbleAsGrass();
        board.printBoard();
    }

}
