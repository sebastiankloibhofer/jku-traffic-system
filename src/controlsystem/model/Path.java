package controlsystem.model;

import controlsystem.trafficparticipants.street.Crossing;
import controlsystem.trafficparticipants.street.Lane;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a calculated route between two crossings.
 */
public class Path {
    public final Crossing start;
    public final Crossing end;
    public final List<Lane> lanes;

    public Path(Crossing start, Crossing end, List<Lane> lanes) {
        this.start = start;
        this.end = end;
        this.lanes = Collections.unmodifiableList(lanes);
    }

    public Path(Crossing start, Crossing end, Lane... lanes) {
        this(start, end, Arrays.asList(lanes));
    }

    public static Path of(List<Path> paths) {
        if (paths.isEmpty())
            return null;

        if (paths.size() == 1)
            return paths.get(0);

        Crossing start = paths.get(0).start;
        Crossing end = paths.get(1).start;
        List<Lane> lanes = paths.stream().flatMap(p -> p.lanes.stream()).collect(Collectors.toList());

        return new Path(start, end, lanes);
    }
}
