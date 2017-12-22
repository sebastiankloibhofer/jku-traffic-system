package trafficParticipants.util;

/**
 *
 * @author Christoph Kroell
 */
public class Vec2i {
    public final int x, y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i subtract(Vec2i other) {
        return new Vec2i(x - other.x, y - other.y);
    }

    public int length() {
        return (int) Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Vec2i{" + "x=" + x + ", y=" + y + '}';
    }
}
