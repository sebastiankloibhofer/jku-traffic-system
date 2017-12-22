package roadmaintenance;

import trafficParticipants.participant.TrafficParticipant;

import java.util.PriorityQueue;

import static roadmaintenance.RoadMaintenance.roadMaintenance;

public class Scheduler {

    private class MaintenanceTask{
        final MaintenanceCar car;
        DamageInfo dmgInfo;

        MaintenanceTask(){
            car = new MaintenanceCar();
        }
    }

    private MaintenanceTask[] maintenanceTasks;

    private PriorityQueue<DamageInfo> toBeMaintained = new PriorityQueue<>();


    public Scheduler(int availableMaintenanceCars){
        maintenanceTasks = new MaintenanceTask[availableMaintenanceCars];

        TrafficParticipant[] cars = new TrafficParticipant[availableMaintenanceCars];
        for(int i =0; i<maintenanceTasks.length; i++) {
            maintenanceTasks[i] = new MaintenanceTask();
            maintenanceTasks[i].car.updateTarget(new int[]{roadMaintenance.headquarters.getId()}, null,i);
            cars[i] = maintenanceTasks[i].car;
        }

        roadMaintenance.headquarters.getTrafficParticipants().addAll(cars);
    }

    private void issueMaintenanceSession(MaintenanceTask mt, DamageInfo dmgInfo, int dest_id, DamageType type, int taskId){
        mt.dmgInfo = dmgInfo;
        mt.car.updateTarget(roadMaintenance.getRoute(mt.car.currentLane(), dest_id), type, taskId);
    }

    public void reschedule(DamageInfo dmgInfo){
        for(int i=0; i<maintenanceTasks.length; i++){
            MaintenanceTask mt = maintenanceTasks[i];

            if(mt.dmgInfo == null){
                issueMaintenanceSession(mt, dmgInfo, dmgInfo.destination_id, dmgInfo.type, i);

                return;
            }
        }

        toBeMaintained.add(dmgInfo);
    }

    public void reportFinishedTask(int taskId){
        if(toBeMaintained.isEmpty()){
            issueMaintenanceSession(maintenanceTasks[taskId], null, roadMaintenance.headquarters.getId(), null, taskId);
        }
        else {
            DamageInfo dmgInfo = toBeMaintained.poll();
            issueMaintenanceSession(maintenanceTasks[taskId], dmgInfo, dmgInfo.destination_id, dmgInfo.type, taskId);
        }
    }
}
