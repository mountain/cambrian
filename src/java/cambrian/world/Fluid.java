package cambrian.world;

import mikera.arrayz.Arrayz;
import mikera.arrayz.INDArray;

public class Fluid {

    public static final double diffusion = 0.3;
    public static final double viscous = 0.03;

    public static INDArray D = Arrayz.newArray(World.width + 2, World.height + 2);
    public static INDArray U = Arrayz.newArray(World.width + 2, World.height + 2);
    public static INDArray V = Arrayz.newArray(World.width + 2, World.height + 2);
    public static INDArray S = Arrayz.newArray(World.width + 2, World.height + 2);

    public static INDArray dx(INDArray f) {
        INDArray d = Arrayz.newArray(World.width + 2, World.height + 2);
        int i, j;
        for (i = 1; i <= World.width; i++) {
            int prev = i - 1;
            int next = i + 1;
            for (j = 1; j <= World.height; j++) {
                double val = 0.5 * f.get(prev, j) - 0.5 * f.get(next, j);
                d.set(i, j, val);
            }
        }
        boundary(d);
        return d;
    }

    public static INDArray dy(INDArray f) {
        INDArray d = Arrayz.newArray(World.width + 2, World.height + 2);
        int i, j;
        for (i = 1; i <= World.width; i++) {
            for (j = 1; j <= World.height; j++) {
                int prev = j - 1;
                int next = j + 1;
                double val = 0.5 * f.get(i, prev) - 0.5 * f.get(i, next);
                d.set(i, j, val);
            }
        }
        boundary(d);
        return d;
    }

    public static INDArray scatter(int x, int y, double value) {
        INDArray s = Arrayz.newArray(World.width + 2, World.height + 2);
        int i, j;
        for (i = 1; i <= World.width; i++) {
            for (j = 1; j <= World.height; j++) {
                double distance = Math.sqrt(World.dist(i, x) * World.dist(i, x) + World.dist(j, y) * World.dist(j, y));
                double val = value / Math.exp(distance);
                if (Math.abs(val) > 1) {
                    s.set(i, j, val);
                } else {
                    s.set(i, j, 0);
                }
            }
        }
        return s;
    }

    public static void init() {
        int i, j;
        for (i = 1; i <= World.width; i++) {
            for (j = 1; j <= World.height; j++) {
                D.set(i, j, 90.0);
                U.set(i, j, 6.0);
                V.set(i, j, 5.0);
                S.set(i, j, 0.0);
            }
        }
        S.add(scatter(256, 256, 1024.0));
        S.add(scatter(256, 768, -256.0));
        S.add(scatter(768, 256, -256.0));
        S.add(scatter(768, 768, -523.199115));
        System.out.println(String.format("S - %f", S.elementSum()));
        boundary(D);
        boundary(U);
        boundary(V);
        boundary(S);
        System.out.println(String.format("after init - dmax: %f dmin: %f", D.elementMax(), D.elementMin()));
    }

    public static void boundary(INDArray f) {
        int i, j;
        for (i = 1; i <= World.width; i++) {
            f.set(i, 0, f.get(i, World.height));
            f.set(i, World.height + 1, f.get(i, 1));
        }
        for (j = 1; j <= World.height; j++) {
            f.set(0, j, f.get(World.width, j));
            f.set(World.width + 1, j, f.get(1, j));
        }
        f.set(0, 0, f.get(1, 1));
        f.set(0, World.height + 1, f.get(1, World.height));
        f.set(World.width + 1, 0, f.get(World.width, 1));
        f.set(World.width + 1, World.height + 1, f.get(World.width, World.height));
    }

    public static void source(INDArray f, INDArray s) {
        f.add(s.multiplyCopy(Timer.tao));
    }

    public static void diffuse(double diff, INDArray f1, INDArray f0) {
        int i, j, k;
        double a = World.timer.tao * diff;
        for (k = 0; k < 20; k++) {
            for (i = 1; i <= World.width; i++) {
                for (j = 1; j <= World.height; j++) {
                    double value = (f1.get(i - 1, j) + f1.get(i + 1, j) +
                            f1.get(i, j - 1) + f1.get(i, j + 1));
                    value = (f0.get(i, j) + value * a) / (1 + 4 * a);
                    f1.set(i, j, value);
                }
            }
            boundary(f1);
        }
    }

    public static void advect(INDArray f1, INDArray f0, INDArray u, INDArray v) {
        int i, j, i0, j0, i1, j1;
        double x, y, s0, t0, s1, t1;
        for (i = 1; i <= World.width; i++) {
            for (j = 1; j <= World.height; j++) {
                x = i - World.timer.tao * u.get(i, j);
                if (x < 0) x += World.width;
                if (x > World.width + 1) x -= World.width;
                if (0 < x && x < 0.5) x = 0.5;
                if (x > World.width + 0.5 && x < World.width + 1) x = World.width + 0.5;
                i0 = (int)x;
                i1 = i0 + 1;

                y = j - World.timer.tao * v.get(i, j);
                if (y < 0) x += World.height;
                if (y > World.height + 1) x -= World.height;
                if (0 < y && y < 0.5) y = 0.5;
                if (y > World.height + 0.5 && y < World.height + 1) y = World.height + 0.5;
                j0 = (int)y;
                j1 = j0 + 1;

                //System.out.println(String.format("original coord - x: %d y: %d", i, j));
                //System.out.println(String.format("velocity - u: %f v: %f", u.get(i, j), v.get(i, j)));
                //System.out.println(String.format("backtrace coord - x: %f y: %f", x, y));

                s1 = x - i0;
                s0 = 1 - s1;
                t1 = y - j0;
                t0 = 1 - t1;

                //System.out.println(String.format("coeff - s0: %f s1: %f t0: %f t1: %f", s0, s1, t0, t1));

                double value = s0 * (t0 * f0.get(i0,j0) + t1 * f0.get(i0,j1)) +
                               s1 * (t0 * f0.get(i1,j0) + t1 * f0.get(i1, j1));

                //System.out.println(String.format("%d, %d, %f, %f", i, j, f0.get(i,j), value));

                f1.set(i, j, value);
            }
        }
        boundary(f1);
    }

    public static void project(INDArray u, INDArray v, INDArray p, INDArray div) {
        int i, j, k;
        double h = 1.0 / World.width;
        for (i = 1; i <= World.width; i++) {
            for (j = 1; j <= World.height; j++) {
                double value = -0.5 * (
                          (u.get(i+1, j) - u.get(i-1, j)) / World.width +
                          (v.get(i, j+1) - v.get(i, j-1)) / World.height);
                div.set(i, j, value);
                p.set(i, j, 0);
            }
        }
        boundary(div);
        boundary(p);

        for (k = 0; k < 20; k++) {
            for (i = 1; i <= World.width; i++) {
                for (j = 1; j <= World.height; j++) {
                    double value = (div.get(i, j) +
                            p.get(i-1, j) + p.get(i+1, j) +
                            p.get(i, j-1) + p.get(i, j+1)) / 4;
                    p.set(i, j, value);
                }
            }
            boundary(p);
        }

        for (i = 1; i <= World.width; i++) {
            for (j = 1; j <= World.height; j++) {
                double uvalue = u.get(i,j) - 0.5 * (p.get(i+1, j) - p.get(i-1, j)) * World.width;
                u.set(i, j, uvalue);
                double vvalue = v.get(i,j) - 0.5 * (p.get(i, j+1) - p.get(i, j-1)) * World.height;
                v.set(i, j, vvalue);
            }
        }
        boundary(u);
        boundary(v);
    }

    public static void dstep(INDArray d, INDArray s, INDArray u, INDArray v) {
        //System.out.println(String.format("before source - dmax: %f dmin: %f", d.elementMax(), d.elementMin()));
        source(d, s);
        //System.out.println(String.format("before diff - dmax: %f dmin: %f", d.elementMax(), d.elementMin()));
        diffuse(diffusion, s, d);
        //System.out.println(String.format("after diff - dmax: %f dmin: %f", s.elementMax(), s.elementMin()));
        advect(d, s, u, v);
        //System.out.println(String.format("after advect - dmax: %f dmin: %f", d.elementMax(), d.elementMin()));
    }

    public static void vstep(INDArray u, INDArray v, INDArray fu, INDArray fv) {
        source(u, fu);
        source(v, fv);
        diffuse(viscous, fu, u);
        diffuse(viscous, fv, v);
        project(fu, fv, u, v);
        advect(u, fu, fu, fv);
        advect(v, fv, fu, fv);
        project(u, v, fu, fv);
    }

    public static void step() {
        INDArray Fu = dx(D);
        INDArray Fv = dy(D);
        INDArray s = S.copy();
        vstep(U, V, Fu, Fv);
        dstep(D, s, U, V);

        System.out.println(String.format("dmax: %f, dmin: %f", D.elementMax(), D.elementMin()));
        System.out.println(String.format("u: %f, v: %f", U.absCopy().elementMax(), V.absCopy().elementMax()));

        Timer.step();
    }
}
