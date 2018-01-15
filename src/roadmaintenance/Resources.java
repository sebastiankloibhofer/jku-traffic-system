package roadmaintenance;

public class Resources {

    public final String backupSrc;
    public final String name;
    private int quantity;

    public Resources(String backupSrc, String name, int initialQuantity){
        this.backupSrc = backupSrc;
        this.name = name;
    }

    public Resources aquire(int quantity){
       if(quantity > this.quantity)
           return null;

       this.quantity -= quantity;

       return new Resources(this.backupSrc, this.name, quantity);
    }

    public void replenish(int quantity){
        this.quantity+=quantity;
    }

    public int quantity(){
        return quantity;
    }
}
