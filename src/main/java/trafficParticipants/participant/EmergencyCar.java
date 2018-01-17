package trafficParticipants.participant;

import trafficParticipants.street.Lane;
import trafficParticipants.util.Updateable;

import java.util.EnumSet;

/**
 *
 * @author Christoph Kroell
 */
public class EmergencyCar extends Vehicle implements Updateable {

    public static final int FINISHED = 100;

    private int workState;

    public EmergencyCar(int speed, Lane start, Lane goal) {
        super(speed, EnumSet.noneOf(Vehicle.InactiveVehicleTypes.class), start, goal);
        this.workState = 0;
    }

    public EmergencyCar(Vehicle vehicle, Lane start, Lane goal) {
        super(vehicle.getSpeed(), EnumSet.noneOf(Vehicle.InactiveVehicleTypes.class), start, goal);
        this.workState = 0;
    }

    @Override
    public TPAction getAction() {
        if(workState == 100) {
            return TPAction.STAY;
        }
        return super.getAction();
    }

    @Override
    public void update() {
        if(getNextLane() == null) {
            if(workState < 100) {
                workState++;
            } else {
                workState = FINISHED;
            }
        }
    }
}
