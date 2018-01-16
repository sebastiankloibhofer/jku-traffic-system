package tptests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import street.Crossing;
import street.Lane;
import util.Vec2i;

/**
 * A premade street network.
 */
public class StreetNetwork {
    
    private static final Crossing[] CROSSINGS = new Crossing[10];
    private static final Lane[] LANES = new Lane[21];
    
    static {
        initCrossings();
        initLanes();
    }
    
    private static void initCrossings() {
        CROSSINGS[0] = new Crossing(new Vec2i(0, 0));
        CROSSINGS[1] = new Crossing(new Vec2i(0, 10));
        CROSSINGS[2] = new Crossing(new Vec2i(10, 10));
        CROSSINGS[3] = new Crossing(new Vec2i(10, 0));
        CROSSINGS[4] = new Crossing(new Vec2i(0, -10));
        CROSSINGS[5] = new Crossing(new Vec2i(-10, -10));
        CROSSINGS[6] = new Crossing(new Vec2i(-10, 0));
        CROSSINGS[7] = new Crossing(new Vec2i(-10, 10));
        CROSSINGS[8] = new Crossing(new Vec2i(10, -10));
        CROSSINGS[9] = new Crossing(new Vec2i(30, 0));
    }
    
    private static void initLanes() {
        LANES[0] = new Lane(CROSSINGS[0], CROSSINGS[1]);
        LANES[1] = new Lane(CROSSINGS[1], CROSSINGS[2]);
        LANES[2] = new Lane(CROSSINGS[2], CROSSINGS[3]);
        LANES[3] = new Lane(CROSSINGS[3], CROSSINGS[0]);
        LANES[4] = new Lane(CROSSINGS[1], CROSSINGS[0]);
        LANES[5] = new Lane(CROSSINGS[2], CROSSINGS[1]);
        LANES[6] = new Lane(CROSSINGS[3], CROSSINGS[2]);
        LANES[7] = new Lane(CROSSINGS[0], CROSSINGS[3]);
        
        LANES[8] = new Lane(CROSSINGS[0], CROSSINGS[4]);
        LANES[9] = new Lane(CROSSINGS[4], CROSSINGS[5]);
        LANES[10] = new Lane(CROSSINGS[5], CROSSINGS[3]);
        LANES[11] = new Lane(CROSSINGS[4], CROSSINGS[0]);
        LANES[12] = new Lane(CROSSINGS[5], CROSSINGS[4]);
        LANES[13] = new Lane(CROSSINGS[3], CROSSINGS[5]);
        
        LANES[14] = new Lane(CROSSINGS[4], CROSSINGS[8]);
        LANES[15] = new Lane(CROSSINGS[8], CROSSINGS[6]);
        
        LANES[16] = new Lane(CROSSINGS[1], CROSSINGS[7]);
        LANES[17] = new Lane(CROSSINGS[7], CROSSINGS[6]);
        
        LANES[18] = new Lane(CROSSINGS[6], CROSSINGS[0]);
        
        LANES[19] = new Lane(CROSSINGS[3], CROSSINGS[9]);
        LANES[20] = new Lane(CROSSINGS[9], CROSSINGS[3]);
    }
    
    private final Map<Integer, Lane> lanes;
    private final Map<Integer, Crossing> crossings;
    
    public StreetNetwork() {
        this.lanes = new HashMap<>(LANES.length);
        this.crossings = new HashMap<>(CROSSINGS.length);
        init();
    }
    
    private void init() {
        for(Lane lane : LANES) {
            if(lane != null) {
                lanes.put(lane.getId(), lane);
            }
        }
        for(Crossing crossing : CROSSINGS) {
            if(crossing != null) {
                crossings.put(crossing.getId(), crossing);
            }
        }
    }
    
    public Map<Integer, Crossing> getCrossings() {
        return crossings;
    }
    
    public Lane getLane(int id) {
        return lanes.get(id);
    }

    public Map<Integer, Lane> getLanes() {
        return lanes;
    }

    @Override
    public String toString() {
        return "StreetNetwork{" + "crossings=" + Arrays.toString(CROSSINGS) + ", lanes=" + Arrays.toString(LANES) + '}';
    }
}