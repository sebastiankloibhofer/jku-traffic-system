package roadmaintenance;

import trafficparticipants.participant.TPAction;
import trafficparticipants.participant.TrafficParticipant;
import trafficparticipants.street.Lane;

import static roadmaintenance.RoadMaintenance.roadMaintenance;

public class MaintenanceCar implements TrafficParticipant {

    private Lane[] route;
    private int currentLane_index;
    private Lane destination;
    private DamageType dmgType;
    private int taskId;
    private int currentStreetUnit_index;
    private int damagedStreetUnits_count;
    private int threshold;

    private boolean damageEvaluated = false;
    private boolean repairingCriticalDmg = true;

    public int getSpeed(){
        //maximum speed limit for cars within the city of Linz
        return 50;
    }

    private void repair(){
        int[] toRepair = destination.streetUnitState;

        if(dmgType == DamageType.SubsystemDamage){
            if(! damageEvaluated){
                if(toRepair[0] == 0)
                    damagedStreetUnits_count++;

                if(toRepair[toRepair.length-1] == 0)
                    damagedStreetUnits_count++;

                threshold = 100/damagedStreetUnits_count;

                damageEvaluated = true;
            }

            if(repairingCriticalDmg){
                if(toRepair[0] < threshold)
                    toRepair[0]++;
                else if(toRepair[toRepair.length-1] < threshold)
                    toRepair[toRepair.length-1]++;
                else
                    repairingCriticalDmg = false;
            }
            else{
                if(toRepair[0] < 100)
                    toRepair[0]++;
                else if(toRepair[toRepair.length-1] < 100)
                    toRepair[toRepair.length-1]++;
                else {
                    damageEvaluated = false;
                    dmgType = DamageType.StreetDamage;
                }
            }
        }
        else{
            if(! damageEvaluated){
                damagedStreetUnits_count = 0;

                for(currentStreetUnit_index = 1; currentStreetUnit_index < toRepair.length-1; currentStreetUnit_index++)
                    if(toRepair[currentStreetUnit_index] == 0)
                        damagedStreetUnits_count++;

                damageEvaluated = true;
                threshold = 100/damagedStreetUnits_count;
            }

            if(repairingCriticalDmg){
                if(toRepair[currentStreetUnit_index] < threshold){
                    toRepair[currentStreetUnit_index]++;
                }

                while(currentStreetUnit_index >= 1 && toRepair[currentStreetUnit_index--] >= threshold);

                if(currentStreetUnit_index < 1){
                    repairingCriticalDmg = false;
                }
            }else{
                if(toRepair[currentStreetUnit_index] < 100)
                    toRepair[currentStreetUnit_index]++;
                else if(currentStreetUnit_index == toRepair.length - 2)
                    roadMaintenance.reportFinishedTask(taskId);
                else
                    currentStreetUnit_index++;
            }
        }
    }

    public TPAction getAction(){
        if(route[currentLane_index] == destination){
            repair();

            return TPAction.STAY;
        }

        return TPAction.MOVE;
    }

    public Lane getNextLane(){
        if(route[currentLane_index] == destination){
            return null;
        }

        return route[currentLane_index++];
    }

    public void updateTarget(Lane[] newRoute, DamageType dmgType, int taskId){
        this.route = newRoute;
        destination = this.route[this.route.length - 1];
        currentLane_index = 0;
        this.dmgType = dmgType;
        this.taskId = taskId;

        damageEvaluated = false;
        repairingCriticalDmg = true;
    }

    public Lane currentLane(){
        return route[currentLane_index];
    }
}