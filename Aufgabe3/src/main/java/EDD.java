import java.util.ArrayList;
import java.util.List;

public class EDD implements Algorithm {
    Data data;
    List<Resource> resources;
    List<Job> jobs;
    double[] dueDate;
    int itterations = 0;

    public EDD(Data data, double[] weight) {
        this.data = data;
        this.resources = data.resources;
        this.jobs = data.jobs;
        this.dueDate = weight;
    }

    @Override
    public void calculate() {
        boolean ready = false;
        while (!ready) {
            // getOperations
            List<Operation> operations = new ArrayList<>();
            for (Job job : jobs) {
                Operation nextOp = job.getNextOperation();
                if (nextOp != null) {
                    operations.add(job.getNextOperation());
                }
            }
            //TODO EDD Sort
            Operation nextOperation = operations.get(0);
            for (Operation operation : operations) {
//                System.out.println("Job: "+weights[operation.job]+"| NextJob: "+weights[nextOperation.job]);
                if (dueDate[operation.job] > dueDate[nextOperation.job]) {
                    itterations = itterations + 1;
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
            ready = true;
            for (Job job : jobs) {
                if (!job.ready()) {
                    ready = false;

                    break;
                }
            }
        }
    }

    @Override
    public List<Resource> getResources() {
        return this.resources;
    }

    @Override
    public int getItterations() {
        return this.itterations;
    }

    @Override
    public int getFinishTime() {
        return data.getFinishTime(this);
    }
}