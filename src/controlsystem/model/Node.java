package controlsystem.model;

import controlsystem.trafficparticipants.street.Crossing;

import java.awt.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Node extends Crossing {

    public static final int R_OFFSET = 100;
    public static final int G_OFFSET = 100;
    public static final int B_OFFSET = 200;

    /** Estimator about the maximum number of lanes. */
    public static final int MAX_LANE_EST = 20;

    public static final int MAX_RADIUS = 10;
    public static final int MIN_RADIUS = 1;

    public Node(long id, int x, int y) {
        super(id, x, y);
    }

    public final int x() {
        return getPosition().x;
    }

    public final int y() {
        return getPosition().y;
    }

    public final int r() {
        final double factor = 1 - sizeFactor();

        return (int) max(MAX_RADIUS * factor, MIN_RADIUS);
    }

    public final Color color() {
        // calculate a color factor based on the transition size
        final double factor = 1 - sizeFactor();

        final int r = (int) (R_OFFSET * factor);
        final int g = (int) (G_OFFSET * factor);
        final int b = B_OFFSET;

        return new Color(r, g, b);
    }

    private double sizeFactor() {
        return min(1, getNrOfLanes() / MAX_LANE_EST);
    }
}
