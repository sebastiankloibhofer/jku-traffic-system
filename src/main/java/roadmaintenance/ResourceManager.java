package main.java.roadmaintenance;

import java.util.HashMap;

public class ResourceManager {

    private HashMap<String,Resources> resources;

    public final static String concrete_name = "concrete_name";
    public final static String steel_name = "steel_name";

    public ResourceManager(Resources[] resources){
        this.resources = new HashMap<>();

        for(Resources r: resources)
            this.resources.put(r.name, r);
    }

    private boolean resourcesAvailable = true;

    public Resources[] prepareResources(DamageInfo dmgInfo){
        Resources[] resources = null;

        switch(dmgInfo.type){
            case SubsystemDamage:{
                resources = new Resources[2];
                resources[0] = this.resources.get(concrete_name).aquire(10);
                resources[1] = this.resources.get(steel_name).aquire(5);

               if(resources[0]==null || resources[1]==null){
                   resourcesAvailable = false;
                   resources = null;

                   StringBuilder sb = new StringBuilder("Out of: ");

                   if(resources[0]==null){
                        sb.append(resources[0].name);
                        sb.append("; replenishment from: ");
                        sb.append(resources[0].backupSrc);
                        sb.append("\n");
                   }
                   else{
                       sb.append(resources[1].name);
                       sb.append("; replenishment from: ");
                       sb.append(resources[1].backupSrc);
                       sb.append("\n");
                   }

                   System.out.println(sb.toString());
               }
            }break;

            case StreetDamage:{
                resources = new Resources[1];
                resources[0] = this.resources.get(concrete_name).aquire(7);

                if(resources[0]==null){
                    resourcesAvailable = false;
                    resources = null;

                    StringBuilder sb = new StringBuilder("Out of: ");

                    sb.append(resources[0].name);
                    sb.append("; replenishment from: ");
                    sb.append(resources[0].backupSrc);
                    sb.append("\n");

                    System.out.println(sb.toString());
                }
            }break;
        }

        return resources;
    }

    public boolean resourcesAvailable(){
        return resourcesAvailable;
    }

    public void replenish(Resources[] resources){
        for(Resources r: resources){
            Resources x = this.resources.get(r.name);

            if(x == null)
                this.resources.put(r.name, r);
            else
                x.replenish(r.quantity());
        }

        resourcesAvailable = true;
    }
}
