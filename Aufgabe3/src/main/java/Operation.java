public class Operation {
    public int index;
    public int duration;
    public int resource;
    public int job;
    public int startTime;

    public Operation() {
        this.index = -1;
        this.resource = -1;
        this.job = -1;
        this.startTime = -1;
    }

    public Operation(int duration) {
        this.duration = duration;
    }

    /**
     * Kopie erstellen
     *
     * @param operation
     */
    public Operation(Operation operation) {
        this.index = operation.index;
        this.duration = operation.duration;
        this.resource = operation.resource;
        this.job = operation.job;
        this.startTime = -1;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "[ index: " + index + ", duration: " + duration + ", startTime: " + startTime + ", resource: " + resource + ", job: " + job + " ]";
    }
}
