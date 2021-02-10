import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAlgorithm implements Algorithm {
    Data data;
    List<Resource> resources;
    List<Job> jobs;
    int itterations = 0;

    public RandomAlgorithm(Data data) {
        this.data = data;
        this.resources = data.resources;
        this.jobs = data.jobs;
    }

    @Override
    public void calculate() {
        boolean ready =false;
        while (!ready) {
            /* getOperations */
            List<Operation> operations = new ArrayList<>();
            for (Job job : jobs) {
                Operation nextOp = job.getNextOperation();
                if (nextOp != null) {
                    operations.add(job.getNextOperation());
                }
            }
            /* make Randomnumber between 0 an Operations.size */
            int op = operations.size();
            Random random = new Random();
            int nop = random.nextInt(op);
            /* getNextOperation (Random) */
            Operation nextOperation = operations.get(nop);
            /* getResource from ResourceId */
            Resource nextResource = resources.get(0);
            itterations= itterations+1;
            for (Resource resource : resources) {
                if (resource.id == nextOperation.resource) {
                    nextResource = resource;
                    break;
                }
            }
            /* get actual LastOperationEnd of the job */
            int jobEnd = 0;
            for (Job job : jobs) {
                if (job.id == nextOperation.job) {
                    jobEnd = job.lastOperationEnd();
                    break;
                }
            }
            /* calc starttime out of Resource (min Starttime combined with duration) */
            int starttime = nextResource.getOptimalTime(jobEnd, nextOperation.duration);
            nextResource.addOperation(nextOperation, starttime);
            /* Check ready */
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
}
