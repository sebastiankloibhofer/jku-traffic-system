package trafficsystem.controlsystem.trafficparticipants.street;

import trafficsystem.controlsystem.trafficparticipants.util.Vec2i;

import java.util.Arrays;

/**
 * A premade street network.
 */
public class StreetNetwork {

    private final Crossing[] crossings = new Crossing[10];
    private final Lane[] lanes = new Lane[22];

    public StreetNetwork() {
        init();
    }

    private void init() {
        this.initCrossings();
        this.initLanes();
    }

    private void initCrossings() {
        crossings[0] = new Crossing(new Vec2i(0, 0));
        crossings[1] = new Crossing(new Vec2i(0, 10));
        crossings[2] = new Crossing(new Vec2i(10, 10));
        crossings[3] = new Crossing(new Vec2i(10, 0));
        crossings[4] = new Crossing(new Vec2i(0, -10));
        crossings[5] = new Crossing(new Vec2i(-10, -10));
        crossings[6] = new Crossing(new Vec2i(-10, 0));
        crossings[7] = new Crossing(new Vec2i(-10, 10));
        crossings[8] = new Crossing(new Vec2i(10, -10));
        crossings[9] = new Crossing(new Vec2i(30, 0));
    }

    private void initLanes() {
        lanes[0] = new Lane(crossings[0], crossings[1]);
        lanes[1] = new Lane(crossings[1], crossings[2]);
        lanes[2] = new Lane(crossings[2], crossings[3]);
        lanes[3] = new Lane(crossings[3], crossings[0]);

        lanes[4] = new Lane(crossings[1], crossings[0]);
        lanes[5] = new Lane(crossings[2], crossings[1]);
        lanes[6] = new Lane(crossings[3], crossings[2]);
        lanes[7] = new Lane(crossings[0], crossings[3]);

        lanes[8] = new Lane(crossings[0], crossings[4]);
        lanes[9] = new Lane(crossings[4], crossings[5]);
        lanes[10] = new Lane(crossings[5], crossings[6]);
        lanes[11] = new Lane(crossings[6], crossings[0]);

        lanes[12] = new Lane(crossings[4], crossings[0]);
        lanes[13] = new Lane(crossings[5], crossings[4]);
        lanes[14] = new Lane(crossings[6], crossings[5]);
        lanes[15] = new Lane(crossings[0], crossings[6]);

        lanes[16] = new Lane(crossings[1], crossings[7]);
        lanes[17] = new Lane(crossings[7], crossings[6]);

        lanes[18] = new Lane(crossings[8], crossings[3]);
        lanes[19] = new Lane(crossings[4], crossings[8]);

        lanes[20] = new Lane(crossings[3], crossings[9]);
        lanes[21] = new Lane(crossings[9], crossings[3]);
    }

    public Crossing[] getCrossings() {
        return crossings;
    }

    public Lane[] getLanes() {
        return lanes;
    }

    @Override
    public String toString() {
        return "StreetNetwork{" + "crossings=" + Arrays.toString(crossings) + ", lanes=" + Arrays.toString(lanes) + '}';
    }
}
