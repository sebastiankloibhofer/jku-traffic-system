package controlsystem.ui;

import controlsystem.trafficparticipants.street.Lane;

import java.awt.*;

import static controlsystem.trafficparticipants.street.Lane.CONGESTION_LVL;

/**
 * Decorator to draw lanes.
 */
class Edge {
    public static final int R_OFFSET = 100;
    public static final int G_OFFSET = 100;
    public static final int B_OFFSET = 100;

    private final Lane component;

    Edge(Lane component) {
        this.component = component;
    }

    final int fromX() {
        return component.getStart().getPosition().x;
    }

    final int fromY() {
        return component.getStart().getPosition().y;
    }

    final int toX() {
        return component.getEnd().getPosition().x;
    }

    final int toY() {
        return component.getEnd().getPosition().y;
    }

    final Color color() {
        // black if the road is blocked
        if (component.isBlocked())
            return new Color(0, 0, 0);

        final double heuristic = component.getUsageLevel();

        final double factor = Math.max(0d, 1 - heuristic / CONGESTION_LVL);

        final int r = R_OFFSET;
        final int g = (int) (G_OFFSET * factor);
        final int b = (int) (B_OFFSET * factor);

        return new Color(r, g, b);
    }
}
