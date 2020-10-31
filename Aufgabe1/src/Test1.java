import sim.engine.SimState;

public class Test1 extends SimState {
    public Test1(long seed) {
        super(seed);
    }
    public static void main(String[] args) {
        doLoop(Test1.class, args);
        System.exit(0);
    }
}
