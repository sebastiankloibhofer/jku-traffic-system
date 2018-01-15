package roadmaintenance;

import trafficParticipants.participant.TPAction;
import trafficParticipants.participant.TrafficParticipant;
import trafficParticipants.street.Lane;
import trafficParticipants.util.Updateable;

import static roadmaintenance.RoadMaintenance.roadMaintenance;

public class MaintenanceCar implements TrafficParticipant, Updateable {

    private int[] route;
    private int currentLane_index;
    private int destination;
    private roadmaintenance.DamageType dmgType;
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
        int[] toRepair = roadMaintenance.getLane(destination).streetUnitState;

        if(dmgType == roadmaintenance.DamageType.SubsystemDamage){
            //routine for subsystem damage

            if(! damageEvaluated){
                //evaluate damage
                //lane fringes are only needed to be considered

                if(toRepair[0] == 0)
                    damagedStreetUnits_count++;

                if(toRepair[toRepair.length-1] == 0)
                    damagedStreetUnits_count++;

                threshold = 100/damagedStreetUnits_count;

                damageEvaluated = true;
            }

            if(repairingCriticalDmg){
                //damage remains still below acceptable threshold

                if(toRepair[0] < threshold)
                    toRepair[0]++;
                else if(toRepair[toRepair.length-1] < threshold)
                    toRepair[toRepair.length-1]++;
                else
                    repairingCriticalDmg = false;
            }
            else{
                //if nothing else to do, try to repair to maximum

                if(toRepair[0] < 100)
                    toRepair[0]++;
                else if(toRepair[toRepair.length-1] < 100)
                    toRepair[toRepair.length-1]++;
                else {
                    damageEvaluated = false;

                    //if nothing else to do, try to repair entire lane
                    dmgType = roadmaintenance.DamageType.StreetDamage;
                }
            }
        }
        else{
            //routine for street damage

            if(! damageEvaluated){
                //evaluate damage
                //scan entire lane
                damagedStreetUnits_count = 0;

                for(currentStreetUnit_index = 1; currentStreetUnit_index < toRepair.length-1; currentStreetUnit_index++)
                    if(toRepair[currentStreetUnit_index] == 0)
                        damagedStreetUnits_count++;

                damageEvaluated = true;
                threshold = 100/damagedStreetUnits_count;
            }

            if(repairingCriticalDmg){
                //damage remains still below acceptable threshold
                if(toRepair[currentStreetUnit_index] < threshold){
                    toRepair[currentStreetUnit_index]++;
                }

                //look for next critical damage
                while(currentStreetUnit_index >= 1 && toRepair[currentStreetUnit_index--] >= threshold);

                if(currentStreetUnit_index < 1){
                    repairingCriticalDmg = false;
                }
            }else{
                //if nothing else to do, try to repair to maximum

                if(toRepair[currentStreetUnit_index] < 100)
                    toRepair[currentStreetUnit_index]++;
                else if(currentStreetUnit_index == toRepair.length - 2)
                    roadMaintenance.reportFinishedTask(taskId); //routine is finished!
                else
                    currentStreetUnit_index++;
            }
        }
    }

    public TPAction getAction(){
        if(route[currentLane_index] == destination){
            return TPAction.STAY;
        }

        return TPAction.MOVE;
    }

    public Lane getNextLane(){
        if(route[currentLane_index] == destination){
            return null;
        }

        return roadMaintenance.getLane(route[currentLane_index++]);
    }

    public void updateTarget(int[] newRoute, roadmaintenance.DamageType dmgType, int taskId){
        this.route = newRoute;
        destination = this.route[this.route.length - 1];
        currentLane_index = 0;
        this.dmgType = dmgType;
        this.taskId = taskId;

        damageEvaluated = false;
        repairingCriticalDmg = true;
    }

    public int currentLane(){
        return route[currentLane_index];
    }

    @Override
    public void update() {
        repair();
    }
}
