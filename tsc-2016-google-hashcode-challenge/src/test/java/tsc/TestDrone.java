package tsc;

import java.io.IOException;
import java.util.ArrayList;

import commands.Deliver;
import model.Data;
import model.Drone;
import model.Order;
import model.Product;
import model.Warehouse;

public class TestDrone {

	public static void main(String [] args) {
		try {
			Data data = Data.getInstance("./data/simple.in");
			double cost = 0;
			

			
			Drone d = null;
			Warehouse w = null;;
			Order o = null;
			
			int currentWarehouse = 0;
			int currentDroneId = 0;
			
			ArrayList<Warehouse> ws = null;
			
			w = data.getWarehouses().get(currentWarehouse);
			o = Data.getInstance().getOrdersFromWarehouse(w); 
			
			boolean firstPass = true;
			do{
				if(!firstPass) {
					w = d.getWarehouseSortedByDistance().get(0);
					ws = null;
				}
				
				d = data.getDrones().get(currentDroneId);
				
				if(ws == null)
					ws = o.getWarehouseSortedByDistance();
				
				System.out.println("Drone " + d.getID());
				System.out.println(" is on warehouse " + w.getId());
				
				//
				// checker la position
				for(Product product : o.getProductsList()) {
					if(w.hasProduct(product)) {
						if(d.getCurrentPayload() + product.getWeight() <= d.getMaxPayload()) {
							d.loadProduct(product, 1, w); // charge le drone
							o.removeProduct(product);
							((Warehouse)d.getLocation()).removeProduct(product.getID());
							break;
						}
					}
				}
				
				System.out.println("Delivery");
				d.unload();
				
				if(!Data.getInstance().hasOrder())
					return;
				
				currentDroneId = (currentDroneId + 1);
				if(o.getTotalWeight() == 0) {
//					w = Data.getInstance().getWarehouses().get(0);
					o = Data.getInstance().getOrdersFromWarehouse(w); 
				} 
				
				if(d.getCurrentPayload() == 0)
					o = Data.getInstance().getOrdersFromWarehouse(w);
				
			}while(currentDroneId + 1 == data.getDrones().size());
//						
//					d.deliver(o);
//					
//					if(!Data.getInstance().hasOrder())
//						return;
//					
//					
//				}while(currentDroneId != data.getInstance().getDrones().size() - 1);
//				currentDroneId = 0;
//				firstPass = false;
//			}while(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
