import java.util.List;

public class Data {
    public List<Resource> resources;
    public List<Job> jobs;

    @Override
    public String toString() {
        return "[ resources: " + resources + ", operations: " + jobs + " ]";
    }
}
