import java.util.ArrayList;
import java.util.List;

public class Greedy implements Algorithm, AlgorithmWeights {
    Data data;
    List<Resource> resources;
    List<Job> jobs;
    double[] weights;
    double[] duDates;
    int itterations = 0;

    public Greedy(Data data, double[] weight) {
        this.data = data;
        this.resources = data.resources;
        this.jobs = data.jobs;
        this.weights = weight;
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
            // getNextOperation with weights
            Operation nextOperation = operations.get(0);
            for (Operation operation : operations) {
//                System.out.println("Job: "+weights[operation.job]+"| NextJob: "+weights[nextOperation.job]);
                if (weights[operation.job] > weights[nextOperation.job]) {
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
        return itterations;
    }

    @Override
    public int getFinishTime() {
        return data.getFinishTime(this);
    }

    @Override
    public double[] getWeights() {
        return this.weights;
    }

    @Override
    public double[] getDuDate() {
        return this.duDates;
    }

    @Override
    public Data getData() {
        return this.data;
    }
}