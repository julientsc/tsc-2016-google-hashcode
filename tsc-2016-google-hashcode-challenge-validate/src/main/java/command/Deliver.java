package command;

import java.util.ArrayList;

import model.Drone;
import model.Order;
import model.ProductType;

public class Deliver implements ICommand {

	private Drone drone;
	private String TAG = "D";
	private Order order;
	private ProductType productType;
	private int quantity;
	
	public Deliver(Drone drone, Order order, ProductType productType, int quantity) {
		super();
		
		this.drone = drone;
		this.order = order;
		this.productType = productType;
		this.quantity = quantity;
	}
	
	public void exec(ArrayList<ICommand> commands) {
		drone.addCostOptimized((int) Math.ceil(drone.getLocation().getDistance(order)));
		drone.addCostOptimized(1);
		drone.setLocation(order);
		
		for(int i = 0 ; i < quantity ; i++) {
			drone.removeProduct(productType);
		}
		
		commands.add(this);
//		System.out.println(toString());
	}
	
	public String toString() {
		return drone.getId() +" " + TAG +" " +  order.getId() + " " + productType.getId() +" " +  quantity;
	}	
	
}
