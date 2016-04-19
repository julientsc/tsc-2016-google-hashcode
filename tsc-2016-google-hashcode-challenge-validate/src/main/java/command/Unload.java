package command;

import java.util.ArrayList;

import model.Drone;
import model.Order;
import model.ProductType;
import model.Warehouse;

public class Unload implements ICommand {

	private Drone drone;
	private String TAG = "U";
	private ProductType productType;
	private int quantity;
	private Warehouse warehouse;
	
	
	
	public Unload(Drone drone, Warehouse warehouse, ProductType productType, int quantity) {
		this.drone = drone;
		this.warehouse = warehouse;
		this.productType = productType;
		this.quantity = quantity;
	}
	
	public void exec(ArrayList<ICommand> commands) {
		drone.addCostOptimized((int) Math.round(drone.getLocation().getDistance(warehouse)));
		drone.addCostOptimized(1);
		drone.setLocation(warehouse);
		
		for(int i = 0 ; i < quantity ; i++) {
			drone.removeProduct(productType);
			warehouse.addProdct(productType);
		}
		
		commands.add(this);
		System.out.println(toString());
	}
	
	public String toString() {
		return drone.getId() + TAG + warehouse.getId() + productType.getId() + quantity;
	}	
	
}
