package controlsystem.ui;

import controlsystem.trafficparticipants.street.Crossing;

import java.awt.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A simple decorator for crossings to draw them.
 */
class Node {

    public static final int R_OFFSET = 100;
    public static final int G_OFFSET = 100;
    public static final int B_OFFSET = 200;

    /** Estimator about the maximum number of lanes. */
    public static final int MAX_LANE_EST = 20;

    public static final int MAX_RADIUS = 10;
    public static final int MIN_RADIUS = 1;

    private final Crossing component;

    Node(Crossing component) {
        this.component = component;
    }

    final int x() {
        return component.getPosition().x;
    }

    final int y() {
        return component.getPosition().y;
    }

    final int r() {
        final double factor = 1 - sizeFactor();

        return (int) max(MAX_RADIUS * factor, MIN_RADIUS);
    }

    final Color color() {
        // calculate a color factor based on the transition size
        final double factor = 1 - sizeFactor();

        final int r = (int) (R_OFFSET * factor);
        final int g = (int) (G_OFFSET * factor);
        final int b = B_OFFSET;

        return new Color(r, g, b);
    }

    private double sizeFactor() {
        return min(1, component.getNrOfLanes() / MAX_LANE_EST);
    }
}
