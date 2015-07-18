package cambrian.world;

public class Timer {

    public static double t = 0.0;

    public static double tao = 0.01;

    public static void step() {
        t += tao;
    }

}
