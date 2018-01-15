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

        //setup cars
        TrafficParticipant[] cars = new TrafficParticipant[availableMaintenanceCars];
        for(int i =0; i<maintenanceTasks.length; i++) {
            maintenanceTasks[i] = new MaintenanceTask();
            maintenanceTasks[i].car.updateTarget(new int[]{roadMaintenance.headquarters.getId()}, null,i);
            cars[i] = maintenanceTasks[i].car;
        }

        //register cars in simulation
        roadMaintenance.headquarters.getTrafficParticipants().addAll(cars);

        //just a toy example for how resources would be setup
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
        //check for resources
        Resources[] resources = resourceManager.prepareResources(dmgInfo);

        if(resources != null){
            //if resources available, issue MaintenanceSession to next best car

            for(int i=0; i<maintenanceTasks.length; i++){
                MaintenanceTask mt = maintenanceTasks[i];

                if(mt.dmgInfo == null){
                    issueMaintenanceSession(mt, dmgInfo, dmgInfo.destination_id, dmgInfo.type, i);

                    //maintenance task is taken care of!
                    return;
                }
            }
        }

        //store maintenance task for later
        toBeMaintained.add(dmgInfo);
    }

    public void reportFinishedTask(int taskId){
        if(toBeMaintained.isEmpty()){
            //if nothing to do, order car back
            issueMaintenanceSession(maintenanceTasks[taskId], null, roadMaintenance.headquarters.getId(), null, taskId);
        }
        else {
            DamageInfo dmgInfo = toBeMaintained.poll();
            issueMaintenanceSession(maintenanceTasks[taskId], dmgInfo, dmgInfo.destination_id, dmgInfo.type, taskId);
        }
    }
}
