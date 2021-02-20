import java.util.ArrayList;
import java.util.List;

public class Job {
    public int id;
    public List<Operation> operations;
    public List<Resource> resources;

    public Job() {
        this.resources = new ArrayList<>();
    }

    /**
     * Kopie erstellen
     *
     * @param job
     */
    public Job(Job job) {
        this.id = job.id;
        this.operations = new ArrayList<>();
        job.operations.forEach(operation -> this.operations.add(new Operation(operation)));
        this.resources = new ArrayList<>();
    }

    void addResources(List<Resource> resources) {
        this.resources = resources;
    }

    /**
     * @return nächste noch nicht ausgeführte Operation
     */
    Operation getNextOperation() {
        for (Operation operation : operations) {
            if (operation.startTime < 0) {
                return operation;
            }
        }
        return null;
    }

    /**
     * @return Zeitpunkt der letzten schon gestarteten Operation
     */
    int lastOperationEnd() {
        int lastOperationEnd = 0;
        for (Operation operation : operations) {
            if (operation.startTime > -1) {
                lastOperationEnd = operation.startTime + operation.duration;
            }
        }
        return lastOperationEnd;
    }

    /**
     * @return Ob alle Operationen gestartet wurden
     */
    boolean ready() {
        for (Operation operation : operations) {
            if (operation.startTime == -1) {
                return false;
            }
        }
        return true;
    }

    int getminTime() {
        int time = 0;
        for (Operation operation : operations) {
            time += operation.duration;
        }
        return time;
    }

    int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "[ id: " + id + ", operations: " + operations + " ]";
    }
}
