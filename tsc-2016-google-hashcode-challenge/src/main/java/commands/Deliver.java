package commands;

public class Deliver extends Command{

    public static char COMMAND_ABREV = 'D';
    
    private final int orderID;
    private final int productID;
    private final int productCount;
    
    public Deliver(int droneId, int orderID, int productID, int productCount) {
        super(droneId);
        this.orderID = orderID;
        this.productID = productID;
        this.productCount = productCount;
    }

    @Override
    public String encode() {
        return getDroneId() +" "+COMMAND_ABREV + " "+orderID+" "+productID+" "+productCount;
    }

}
