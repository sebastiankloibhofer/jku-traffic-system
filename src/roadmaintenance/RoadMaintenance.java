package roadmaintenance;

import trafficparticipants.street.Lane;

public class RoadMaintenance {

    // Author: Nimrod Varga
    // Matr.Nr.: 01555232
    //e-mail: nimrodvarga@hotmail.com

    public static RoadMaintenance roadMaintenance;

    public final Lane headquarters;

    private Scheduler scheduler = new Scheduler(4);

    public RoadMaintenance(Lane headquarters){
        this.headquarters = headquarters;
    }

    public void notifyOfDamage(DamageInfo dmgInfo){
        scheduler.reschedule(dmgInfo);
    }

    public Lane[] getRoute(int currentLocation_id, int destination_id){
        //TODO interface with control system here
        return null;
    }

    public void reportFinishedTask(int taskId){
        scheduler.reportFinishedTask(taskId);
    }
}
