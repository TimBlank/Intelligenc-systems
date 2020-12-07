import java.util.List;

public class Job {
    public int id;
    public List<Operation> operations;

    @Override
    public String toString() {
        return "[ id: " + id + ", operations: " + operations + " ]";
    }
}
