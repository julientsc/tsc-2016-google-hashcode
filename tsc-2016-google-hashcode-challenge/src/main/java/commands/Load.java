package commands;

public class Load extends Command{

    public static char COMMAND_ABREV = 'L';
    
    private final int warehouseID;
    private final int productType;
    private final int productCount;
    
    public Load(int droneId, int warehouseID, int productType, int productCount) {
        super(droneId);
        this.warehouseID = warehouseID;
        this.productType = productType;
        this.productCount = productCount;
    }

    @Override
    public String encode() {
        return getDroneId() +" "+COMMAND_ABREV+" "+warehouseID+" "+productType+" "+productCount;
    }

}
