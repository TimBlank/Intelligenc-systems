import java.util.List;

public interface Algorithm {
    void calculate();
    List<Resource> getResources();

    int getItterations();
}
