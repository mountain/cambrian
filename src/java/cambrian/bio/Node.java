package cambrian.bio;

import cambrian.world.Context;
import cambrian.world.Fluid;
import cambrian.world.Timer;
import cambrian.world.World;

import static cambrian.world.World.distance;

public class Node {

    public static enum Mode {
        Dead, Dysfunctional, Absorbing, Generating, Releasing, Sensing;
    };

    public static final double hook = 1.0;
    public static final double length = 3.0;
    public static final double energyDiff = 0.1;

    public Mode mode;

    public byte r;
    public byte g;
    public byte b;
    public String color;

    public double energy;

    public double[] position;
    public double[] direction;
    public double[] left;
    public double[] right;

    public double[] force;
    public double[] speed;

    public Context context;
    public Creature creature;
    public Node prev;
    public Node next;

    // Sensor variables
    public double[] sensor;
    public double[] vision;
    public double[] audition;
    public double[] tactition;

    // Control variables
    public double[] control;
    public double[] movement;
    public double[] sounds;

    private int min(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    private int max(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    protected void geometryStep() {

    }

    protected void mechanicsStep() {
        double[] prevF = World.force(length, hook, this.position, this.prev.position);
        double[] nextF = World.force(length, hook, this.position, this.next.position);
        force[0] = prevF[0] + nextF[0] + movement[0];
        force[1] = prevF[1] + nextF[1] + movement[1];
        speed[0] = speed[0] + force[0] * Timer.tao;
        speed[1] = speed[1] + force[1] * Timer.tao;
        position[0] = position[0] + speed[0] * Timer.tao;
        position[1] = position[1] + speed[1] * Timer.tao;
    }

    protected void sensorStep() {
        if (this.mode == Mode.Sensing) {
            this.vision = context.sightAt(this.position);
            this.audition = context.hearingAt(this.position);
        } else {
            this.vision = new double[256];
            this.audition = new double[256];
        }
        this.tactition = new double[2];
        this.tactition[0] = distance(Fluid.velocityAt(this.left), this.speed);
        this.tactition[1] = distance(Fluid.velocityAt(this.right), this.speed);
    }

    protected void controlStep() {
    }

    protected void physiologicStep() {
        double prevDrift = 0, nextDrift = 0;
        if (this.prev != null) {
            prevDrift = energyDiff * this.prev.energy;
            this.prev.energy = this.prev.energy - prevDrift;
        }
        if (this.next != null) {
            nextDrift = energyDiff * this.next.energy;
            this.next.energy = this.next.energy - nextDrift;
        }
        this.energy += (prevDrift + nextDrift);
    }

    protected void interactionStep() {

    }

    protected void balanceStep() {
    }

    protected void resetMode() {
        if (energy < 0) {
            b = 0;
        } else if (energy > 256) {
            b = (byte)255;
        } else {
            b = (byte) energy;
        }

        if ((int)b == 0) {
            mode = Mode.Dead;
        } else if (b < 4) {
            mode = Mode.Dysfunctional;
        } else if (max(r, g, b) - min(r, g, b) < 4) {
            mode = Mode.Sensing;
        } else if ((int)r > (int)g + (int)b) {
            mode = Mode.Absorbing;
        } else if ((int)g > (int)r + (int)b) {
            mode = Mode.Generating;
        } else if ((int)b > (int)r + (int)g) {
            mode = Mode.Releasing;
        }
    }

    public void step() {
        geometryStep();
        mechanicsStep();
        sensorStep();
        controlStep();
        physiologicStep();
        interactionStep();
        balanceStep();
        resetMode();
    }

}
