import java.util.ArrayList;
import java.util.List;

public class Job {
    public int id;
    public List<Operation> operations;
    public List<Resource> resources;

    public Job() {
        this.resources = new ArrayList<>();
    }

    void addResources(List<Resource> resources) {
        this.resources = resources;
    }

    Operation getNextOperation() {
        for (Operation operation : operations) {
            if (operation.startTime < 0) {
                return operation;
            }
        }
        return null;
    }

    Resource getResource(int id) {
        for (Resource resource : resources) {
            if (resource.id == id) return resource;
        }
        return null;
    }

    int lastOperationEnd() {
        int lastOperationEnd = 0;
        for (Operation operation : operations) {
            if (operation.startTime > -1) {
                lastOperationEnd = operation.startTime + operation.duration;
            }
        }
        return lastOperationEnd;
    }

    boolean ready() {
        for (Operation operation : operations) {
            if (operation.startTime == -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "[ id: " + id + ", operations: " + operations + " ]";
    }
}
