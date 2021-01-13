import java.util.List;
import java.util.stream.Collectors;

public class Solver {

    public static String FILE = "tents_trees_0.csv";

    static public void main(String[] args) {
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
        board.calcTentProbability();
        board.printBoard();

        System.out.println(board.fields.get(0).getTentAble());
        List<Field> otherFieldsWithOneNearTree = otherFieldsWithOneNearTree(board);
        while (otherFieldsWithOneNearTree.size() > 0) {
            System.out.println("otherFieldsWithOneNearTree: " + otherFieldsWithOneNearTree.size());
            Field field = otherFieldsWithOneNearTree.get(0);
            field.setTent(field.getNearFreeTrees().get(0));
            board.calcTentProbability();
            board.printBoard();
            otherFieldsWithOneNearTree = otherFieldsWithOneNearTree(board);
        }
    }

    static List<Field> otherFieldsWithOneNearTree(Board board) {
        return board.fields.stream()
                .filter(field -> field.type() == Type.unknown)
                .filter(field -> field.getNearFreeTrees().size() == 1)
                .sorted((field1, field2) -> (int) ((field2.getTentAble() - field1.getTentAble()) * 1000))
                .collect(Collectors.toList());
//                .collect(Collectors.toCollection(ArrayList::new));
    }
}
