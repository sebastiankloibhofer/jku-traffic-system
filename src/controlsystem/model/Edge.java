package controlsystem.model;

import controlsystem.trafficparticipants.street.Lane;

import java.awt.*;

public class Edge extends Lane {
    public static final int R_OFFSET = 100;
    public static final int G_OFFSET = 100;
    public static final int B_OFFSET = 100;
    public static final int A_OFFSET = 255;

    public Edge(long id, Node start, Node end) {
        super(id, start, end);
    }

    public final int fromX() {
        return getStart().getPosition().x;
    }

    public final int fromY() {
        return getStart().getPosition().y;
    }

    public final int toX() {
        return getEnd().getPosition().x;
    }

    public final int toY() {
        return getEnd().getPosition().y;
    }

    public final Color color() {
        // black if the road is blocked
        if (isBlocked())
            return new Color(0, 0, 0);

        final double heuristic = getUsageLevel();

        final double factor = Math.max(0d, 1 - heuristic / CONGESTION_LVL);

        final int r = R_OFFSET;
        final int g = (int) (G_OFFSET * factor);
        final int b = (int) (B_OFFSET * factor);
        final int a = (int) (A_OFFSET * (1 - factor));

        return new Color(r, g, b, 100);
    }
}
