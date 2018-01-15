package trafficParticipants.participant;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import trafficParticipants.street.Lane;
import trafficParticipants.util.Updateable;

/**
 *
 * @author Christoph Kroell
 */
public class TPManager {
    
    private final Map<Integer, Lane> lanes;
    private final Queue<Integer> accidents;

    public TPManager(Map<Integer, Lane> lanes) {
        this.lanes = lanes;
        this.accidents = new ArrayDeque<>();
    }
    
    public void update() {
        for(Lane lane : lanes.values()) {
            lane.getTrafficParticipants().update();
            for(TrafficParticipant tp : lane.getTrafficParticipants()) {
                if(tp instanceof Updateable) {
                    ((Updateable)tp).update();
                }
                // Turn vehicles, which are also emergency cars to emergency cars
                if(tp instanceof Vehicle) {
                    Vehicle v = (Vehicle) tp;
                    if(!accidents.isEmpty() && v.getInactiveTypes().contains(Vehicle.InactiveVehicleTypes.EMERGENCY_CAR)) {
                        lane.getTrafficParticipants().replace(tp, new EmergencyCar((Vehicle) tp, accidents.poll()));
                    }
                }
            }
            // Add emergency cars if there are too many accidents
            while(!accidents.isEmpty()) {
                if(lanes.get((int)(Math.random() * lanes.size())).getTrafficParticipants().add(new EmergencyCar(2, accidents.peek()), 0)) {
                    accidents.poll();
                }
            }
        }
    }
    
    /**
     * Puts the id of a lane where and accident occurened into a queue, which
     * is proccessed every update.
     * 
     * @param laneId The id of the lane where the accident has occured.
     */
    public void sendEmergencyCarTo(int laneId) {
        accidents.add(laneId);
    }

    public Lane getLane(int laneId) {
        return lanes.get(laneId);
    }
}

