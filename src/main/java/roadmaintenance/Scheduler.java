package roadmaintenance;

import main.java.roadmaintenance.ResourceManager;
import main.java.roadmaintenance.Resources;
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

    private ResourceManager resourceManager;


    public Scheduler(int availableMaintenanceCars){
        maintenanceTasks = new MaintenanceTask[availableMaintenanceCars];

        TrafficParticipant[] cars = new TrafficParticipant[availableMaintenanceCars];
        for(int i =0; i<maintenanceTasks.length; i++) {
            maintenanceTasks[i] = new MaintenanceTask();
            maintenanceTasks[i].car.updateTarget(new int[]{roadMaintenance.headquarters.getId()}, null,i);
            cars[i] = maintenanceTasks[i].car;
        }

        roadMaintenance.headquarters.getTrafficParticipants().addAll(cars);

        Resources[] resources = new Resources[2];

        resources[0] = new Resources("concreteFactory", ResourceManager.concrete_name, 1000);
        resources[1] = new Resources("steelFactory(you know which one)", ResourceManager.steel_name, 1000);

        resourceManager = new ResourceManager(resources);
    }

    private void issueMaintenanceSession(MaintenanceTask mt, DamageInfo dmgInfo, int dest_id, DamageType type, int taskId){
        mt.dmgInfo = dmgInfo;
        mt.car.updateTarget(roadMaintenance.getRoute(mt.car.currentLane(), dest_id), type, taskId);
    }

    public void reschedule(DamageInfo dmgInfo){
        Resources[] resources = resourceManager.prepareResources(dmgInfo);

        if(resources != null){
            for(int i=0; i<maintenanceTasks.length; i++){
                MaintenanceTask mt = maintenanceTasks[i];

                if(mt.dmgInfo == null){
                    issueMaintenanceSession(mt, dmgInfo, dmgInfo.destination_id, dmgInfo.type, i);

                    return;
                }
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
