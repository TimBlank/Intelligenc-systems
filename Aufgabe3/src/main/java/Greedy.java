import java.util.ArrayList;
import java.util.List;

public class Greedy implements Algorithm {
    List<Resource> resources;
    List<Job> jobs;

    public Greedy(Data data) {
        this.resources = data.resources;
        this.jobs = data.jobs;
    }

    @Override
    public void calculate() {
        while (true) {
            // getOperations
            List<Operation> operations = new ArrayList<>();
            for (Job job : jobs) {
                Operation nextOp = job.getNextOperation();
                if (nextOp != null) {
                    operations.add(job.getNextOperation());
                }
            }
            // getNextOperation (max duration)
            Operation nextOperation = operations.get(0);
            for (Operation operation : operations) {
                if (operation.duration > nextOperation.duration) {
                    nextOperation = operation;
                }
            }
            // getResource from ResourceId
            Resource nextResource = resources.get(0);
            for (Resource resource : resources) {
                if (resource.id == nextOperation.resource) {
                    nextResource = resource;
                    break;
                }
            }
            // get actual LastOperationEnd of the job
            int jobEnd = 0;
            for (Job job : jobs) {
                if (job.id == nextOperation.job) {
                    jobEnd = job.lastOperationEnd();
                    break;
                }
            }
            // calc starttime out of Resource (min Starttime combined with duration)
            int starttime = nextResource.getOptimalTime(jobEnd, nextOperation.duration);
            nextResource.addOperation(nextOperation, starttime);
            // Check ready
            boolean ready = true;
            for (Job job : jobs) {
                if (!job.ready()) {
                    ready = false;
                    break;
                }
            }
            if (ready) return;
        }
    }
}