package commands;

import java.util.ArrayList;
import java.util.List;

import model.Drone;
import model.Order;
import model.Product;
import model.ProductPile;
import model.Warehouse;

public abstract class Command {

    private final int droneId;
    
    public Command(int droneId){
        this.droneId = droneId;
    }
    
    public int getDroneId(){
        return droneId;
    }
    
    public abstract String encode();
    
    @Override
    public String toString(){
        return encode();
    }
    
    public static Command createLoadCommand(Warehouse warehouse, Drone drone, Product product, int maxCount){
        int count = Math.min(maxCount, warehouse.getProductQuantity(product.getID()));
                
        if(count == 0){
            return null;
        }
        
        warehouse.removeProduct(product.getID(), count);
        drone.loadProduct(product, count);
        
        drone.setLocation(warehouse);
        
        return new Load(drone.getID(), warehouse.getId(), product.getID(), count);
    }
    
    public static List<Command> createDeliver(Drone drone, Order order){
        List<Command> commands = new ArrayList<Command>();
        for(ProductPile pile: order.getProducts()){
            int count = Math.min(pile.getCount(), drone.getProductCount(pile.getProduct().getID()));
            
            if(count > 0){
                commands.add(new Deliver(drone.getID(), order.getID(), pile.getProduct().getID(), count));
            }
        }
        
        return commands;
    }
}
