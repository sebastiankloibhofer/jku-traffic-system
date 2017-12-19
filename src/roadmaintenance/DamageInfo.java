public class DamageInfo {

    public final int damageReport_id;
    public final int priority;
    public final int destination_id;
    public final DamageType type;

    public DamageInfo(int damageReport_id, int priority, int destination_id, DamageType type){
        this.damageReport_id = damageReport_id;
        this.priority = priority;
        this.destination_id = destination_id;
        this.type = type;
    }
}
