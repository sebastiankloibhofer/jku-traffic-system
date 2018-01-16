package participant;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import street.Lane;
import util.Updateable;

/**
 * Updates all the traffic participants contained within the lanes,
 * keeps track of accidents and sends emergency cars if needed.
 * 
 * @author Christoph Kroell
 */
public class TPManager {
    
    private final Map<Integer, Lane> lanes;
    private final Queue<Lane> accidents;

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
                        lane.getTrafficParticipants().replace(tp, new EmergencyCar((Vehicle) tp, tp.getNextLane(), accidents.poll()));
                    }
                }
            }
            // Add emergency cars if there are too many accidents
            while(!accidents.isEmpty()) {
                Lane ran;
                ran = lanes.get((int)(Math.random() * lanes.size()));
                if(ran.getTrafficParticipants().add(new EmergencyCar(2, ran, accidents.poll()), (int) (ran.getLength() * Math.random()))) {
                    accidents.poll();
                }
            }
        }
    }
    
    /**
     * Puts the id of a lane where and accident occurened into a queue, which
     * is proccessed every update.
     * 
     * @param lane The lane where the accident has occured.
     */
    public void sendEmergencyCarTo(Lane lane) {
        accidents.add(lane);
    }

    public Lane getLane(int laneId) {
        return lanes.get(laneId);
    }
}
