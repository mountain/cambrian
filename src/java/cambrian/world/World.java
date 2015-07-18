package cambrian.world;

import cambrian.bio.Creature;
import cambrian.util.Images;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static final int width = 1024;
    public static final int height = 1024;

    public static Timer timer = new Timer();
    public static List<Creature> creatures = new ArrayList<Creature>();

    public static double dist(int i, int j) {
        i = (i - 1) % 1024;
        j = (j - 1) % 1024;
        double mn = Math.min(i, j);
        double mx = Math.max(i , j);
        double d = Math.min(mx - mn, 1024.0 - (mx - mn));
        if (i < j) {
            return d;
        } else {
            return -d;
        }
    }

    public static double dist(double i, double j) {
        double mn = Math.min(i, j);
        double mx = Math.max(i , j);
        double d = Math.min(mx - mn, 1024.0 - (mx - mn));
        if (i < j) {
            return d;
        } else {
            return -d;
        }
    }

    public static double[] vector(double[] a, double[] b) {
        return new double[]{dist(a[0], b[0]), dist(a[1], b[1])};
    }

    public static double distance(double[] a, double[] b) {
        double[] v = vector(a, b);
        return Math.sqrt(v[0] * v[0] + v[1] * v[1]);
    }

    public static double[] direction(double[] a, double[] b) {
        double d1 = dist(a[0], b[0]);
        double d2 = dist(a[1], b[1]);
        double d = Math.sqrt(d1 * d1 + d2 * d2);
        return new double[]{d1 / d, d2 / d};
    }

    public static double[] force(double length, double hook, double[] a, double[] b) {
        double d1 = dist(a[0], b[0]);
        double d2 = dist(a[1], b[1]);
        double d = Math.sqrt(d1 * d1 + d2 * d2);
        if (d > length ) {
            double f = d / length * hook;
            return new double[]{ - f * d1 / d, - f * d2 / d };
        } else if (d < length) {
            double f = length / d * hook;
            return new double[]{ f * d1 / d, f * d2 / d };
        } else {
            return new double[]{ 0, 0 };
        }
    }

    public static void main(String[] args) {
        Fluid.init();
        int counter = 0;
        while(true) {
            if (counter == 0) {
                String nd = String.format("images/d.%03d.png", (int) (10 * Timer.t));
                String nu = String.format("images/u.%03d.png", (int) (10 * Timer.t));
                String nv = String.format("images/v.%03d.png", (int) (10 * Timer.t));

                Images.genPNG("scalar", nd, Fluid.D.asDoubleArray());
                Images.genPNG("vector", nu, Fluid.U.asDoubleArray());
                Images.genPNG("vector", nv, Fluid.V.asDoubleArray());
            }
            counter = (counter + 1) % 10;

            Fluid.step();
        }
    }
}
