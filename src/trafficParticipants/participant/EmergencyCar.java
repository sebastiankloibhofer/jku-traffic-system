package trafficParticipants.participant;

import trafficParticipants.util.Updateable;

import java.util.EnumSet;

/**
 *
 * @author Christoph Kroell
 */
public class EmergencyCar extends Vehicle implements Updateable {

    public static final int FINISHED = 100;

    private int goal;
    private int workState;

    public EmergencyCar(int speed, int goal) {
        super(speed, EnumSet.noneOf(Vehicle.InactiveVehicleTypes.class));
        this.goal = goal;
        this.workState = 0;
    }

    public EmergencyCar(Vehicle vehicle, int goal) {
        super(vehicle.getSpeed(), EnumSet.noneOf(Vehicle.InactiveVehicleTypes.class));
        this.goal = goal;
        this.workState = 0;
    }

    @Override
    public participant.TPAction getAction() {
        if(workState == 100) {
            return participant.TPAction.STAY;
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
