public class Operation {
    public int index;
    public int duration;
    public int resource;

    @Override
    public String toString() {
        return "[ index: " + index + ", duration: " + duration + ", resource: " + resource + " ]";
    }
}
