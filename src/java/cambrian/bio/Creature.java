package cambrian.bio;

/**
 *   Bio-Chemistry
 *
 *   Red: the ability of absorbing energy
 *   Green: the ability of generating energy
 *   Blue: the amount of energy
 *
 *   When R > G + B for a node, the node own the ability to absorb energy
 *   When G > R + B for a node, the node own the ability to generate energy
 *   When B > G + R for a node, the node own the ability to release energy
 *
 *   sensor: R ~ G ~ B
 *           when value towards FF, sensor is sensitive for light, the sensing distance for light is *value* pixel
 *           when value towards 00, sensor is sensitive for sound, the sensing distance for sound is *sqrt(256 - value) * pixel
 *
 *   consume of energy:
 *           the amount to consume is related with R
 *   flow of energy:
 *           blue from neighbor node
 *
 *   the ability of movement:
 *           max power is the total of R
 *           power = speed * length
 *
 *   the ability to trigger reproduce process:
 *           when all B > 128 for two individual
 *           and they are nearby each other
 *
 */

public class Creature {

    public int length;
    public byte[] r;
    public byte[] g;
    public byte[] b;

    public byte[] color;
    public int[] poz;

    public void generate() {
    }

    public void consume() {
    }

    public void absorb() {
    }

    public void sense() {
    }

}
