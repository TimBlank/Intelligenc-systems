import java.util.List;

public class Random implements Algorithm {
    List<Resource> resources;
    List<Job> jobs;

    public void Random(Data data) {
        this.resources = data.resources;
        this.jobs = data.jobs;
    }

    @Override
    public String output() {
        String sting = "";

        return null;
    }
}
