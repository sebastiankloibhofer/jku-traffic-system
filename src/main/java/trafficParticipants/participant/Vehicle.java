package trafficParticipants.participant;

import trafficParticipants.street.Lane;

import java.util.EnumSet;
import java.util.Queue;

/**
 *
 * @author Christoph Kroell
 */
public class Vehicle implements TrafficParticipant {

    private static long idCount = 0;

    private final long id;
    private final int speed;
    private Queue<Lane> path;
    private final EnumSet<InactiveVehicleTypes> inactiveTypes;
    private Lane cur;

    public Vehicle(int speed, Lane start, Lane goal) {
        this(speed, EnumSet.noneOf(InactiveVehicleTypes.class), start, goal);
    }

    public Vehicle(int speed, EnumSet<InactiveVehicleTypes> inactiveTypes, Lane start, Lane goal) {
        id = idCount++;
        this.speed = speed;
        this.path = TPPath.get(start, goal);
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
        if(cur == null) {
            if(path.peek().getTrafficParticipants().contains(this)) {
                cur = path.peek();
            } else {
                return null;
            }
        }
        while(!cur.getTrafficParticipants().contains(this)) {
            if(path.isEmpty()) {
                return null;
            } else {
                cur = path.poll();
            }
        }
        return cur;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vehicle other = (Vehicle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "id=" + id + ", speed=" + speed + '}';
    }

    public Lane getGoal() {
        if(path.isEmpty()) {
            return null;
        } else {
            Lane[] temp = path.toArray(new Lane[0]);
            return temp[temp.length - 1];
        }
    }

    public EnumSet<InactiveVehicleTypes> getInactiveTypes() {
        return inactiveTypes;
    }

    public enum InactiveVehicleTypes {
        EMERGENCY_CAR;
    }
}
