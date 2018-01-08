package trafficParticipants.participant;

import trafficParticipants.street.Lane;

import java.util.EnumSet;

/**
 *
 * @author Christoph Kroell
 */
public class Vehicle implements TrafficParticipant {

    private final int speed;
    private final EnumSet<InactiveVehicleTypes> inactiveTypes;

    public Vehicle(int speed, EnumSet<InactiveVehicleTypes> inactiveTypes) {
        this.speed = speed;
        this.inactiveTypes = inactiveTypes;
    }

    @Override
    public TPAction getAction() {
        if(getNextLane() == null) {
            return TPAction.FIN;
        }
        return TPAction.MOVE;
    }

    @Override
    public Lane getNextLane() {
        // TODO: get next lane of the route
        return null;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public EnumSet<InactiveVehicleTypes> getInactiveTypes() {
        return inactiveTypes;
    }

    public enum InactiveVehicleTypes {
        EMERGENCY_CAR;
    }
}
