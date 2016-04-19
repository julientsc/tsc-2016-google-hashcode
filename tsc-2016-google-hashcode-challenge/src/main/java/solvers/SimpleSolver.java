package solvers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import commands.Command;
import commands.Deliver;
import commands.Load;
import model.Data;
import model.Drone;
import model.Order;
import model.ProductPile;
import model.Warehouse;
import solution.SolutionEncoder;

public class SimpleSolver {

    public void solve() throws IOException{
        String dataFile = Data.SIMPLE;
        Data data = Data.getInstance(dataFile);
        
        Drone myDrone = data.getDrones().get(0);
        
        List<Command> commands = new ArrayList<Command>();
        
        for(Order order: data.getOrders().values()){
            commands.addAll(solveOrder(myDrone, order));
            
            System.out.println("Solved "+order.toString());
        }

        System.out.println("Commands "+commands.size());
        
        SolutionEncoder encoder = new SolutionEncoder("busy.sol");
        encoder.writeCommands(commands);
        encoder.close();
    }
    
    private List<Command> solveOrder(Drone drone, Order order){
        List<Command> commands = new ArrayList<Command>();
        
        List<ProductPile> products = order.getProducts();
        
        for(ProductPile product: products){
            
            while(product.getCount() > 0){
                commands.addAll(solveProduct(drone, order, product));
            }
        }
        
        return commands;
    }
 
    private List<Command> solveProduct(Drone drone, Order order, ProductPile pile){
        List<Command> commands = new ArrayList<Command>();
        
        //We have at least some of the items needed, deliver
        if(drone.getProductCount(pile.getProduct().getID()) > 0){
            int unloadCount = Math.min(pile.getCount(), drone.getProductCount(pile.getProduct().getID()));
            
            Command command = new Deliver(drone.getID(), order.getID(), pile.getProduct().getID(), unloadCount);
            commands.add(command);
            System.out.println(command.encode());
            
            drone.setX(order.getX());
            drone.setY(order.getY());
            
            drone.unloadProduct(pile.getProduct().getID(), unloadCount);
            
            pile.setCount(pile.getCount() - unloadCount);            
        }else{//Get a warehouse that has it
            Warehouse warehouse = drone.getClosestWarehouse(pile.getProduct());
            
            int loadCount = Math.min(pile.getCount(), drone.getMaxCount(pile.getProduct()));
            loadCount = Math.min(loadCount, warehouse.getProductQuantity(pile.getProduct().getID()));
            
            Command command = new Load(drone.getID(), warehouse.getId(),
                    pile.getProduct().getID(), loadCount);
            commands.add(command);
            System.out.println(command.encode());
            
            drone.setX(order.getX());
            drone.setY(order.getY());
            
            drone.loadProduct(pile.getProduct(), loadCount);
            
            warehouse.removeProduct(pile.getProduct().getID(), loadCount);
        }
        
        return commands;
    }
    
}
