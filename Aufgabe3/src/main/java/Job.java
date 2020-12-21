import java.util.List;

public class Job {
    public int id;
    public List<Operation> operations;

    Operation getNextOperation() {
        for (Operation operation : operations) {
            if (operation.startTime < 0) {
                return operation;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "[ id: " + id + ", operations: " + operations + " ]";
    }
}
