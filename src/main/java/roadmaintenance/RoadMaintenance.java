package roadmaintenance;

import controlsystem.model.Route;
import trafficParticipants.street.Lane;

public class RoadMaintenance {

    // Author: Nimrod Varga
    // Matr.Nr.: 01555232
    // e-mail: nimrodvarga@hotmail.com

    public static RoadMaintenance roadMaintenance;

    public final Lane headquarters;

    private Scheduler scheduler = new Scheduler(4);

    public RoadMaintenance(Lane headquarters){
        this.headquarters = headquarters;
    }

    public void notifyOfDamage(DamageInfo dmgInfo){
        scheduler.reschedule(dmgInfo);
    }

    public int[] getRoute(int currentLocation_id, int destination_id){
        //TODO interface with control system here

        Route r = null;//getRoute(currentLocation_id, destination_id);

        int[] route_ids = new int[r.getLanes().size()];

        int i=0;
        for(Lane l : r.getLanes())
            route_ids[i++]=l.getId();

        return route_ids;
    }

    public Lane getLane(int id){
        return null;
    }

    public void reportFinishedTask(int taskId){
        scheduler.reportFinishedTask(taskId);
    }
}
