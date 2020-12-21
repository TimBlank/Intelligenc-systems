public class Operation {
    public int index = -1;
    public int duration;
    public int resource = -1;
    public int job = -1;
    public int startTime = -1;

    public Operation(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "[ index: " + index + ", duration: " + duration + ", resource: " + resource + ", job: " + job + " ]";
    }
}
