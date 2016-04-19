package simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import command.Deliver;
import command.ICommand;
import command.Load;
import model.Drone;
import model.Location;
import model.Order;
import model.ProductType;
import model.Warehouse;
import tools.DataLoader;

public class SimulationC {

	DataLoader dataLoader = null;

	public SimulationC(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}
	

	public void exec() throws IOException {
		
		ArrayList<Warehouse> warehouses = dataLoader.getWarehouses();

		Drone d = dataLoader.getDrones().get(0);
		ArrayList<Order> orders = getOrderSortedByDistance(d.getLocation());
		for (Order order : orders) {
			
		}
		
		
//		orders.get(0);
//		Drone d = dataLoader.getDrones().get(0);
		
		
		
		
//		PrintWriter pw = new PrintWriter(new FileWriter("out.out"));
//		pw.println(commands.size());
//		for (ICommand iCommand : commands) {
//			pw.println(iCommand.toString());
//		}
//		pw.close();
		
//		for(Drone d : dataLoader.getDrones())
//			System.out.println(d.getCostOptimized());
	}
	
	private Warehouse getClosestFullWarehouse(Order order) {
		Double dist = Double.MAX_VALUE;
		Warehouse closed = null;
		for (Warehouse w : dataLoader.getWarehouses()) {
			if (w.getDistance(order) < dist && w.contains(order)) {
				dist = w.getDistance(order);
				closed = w;
			}
		}
		return closed;
	}

	private ArrayList<Order> getOrderSortedByDistance(Location location) {
		ArrayList<Order> specificOrder = (ArrayList<Order>) dataLoader.getOrders().clone();
		
		Collections.sort(specificOrder, new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				return (int) (o1.getDistance(location) - o2.getDistance(location));
			}
		});
		
		return specificOrder;
	}

	private ArrayList<Order> getOrderSortedByWeight() {
		ArrayList<Order> specificOrder = (ArrayList<Order>) dataLoader.getOrders().clone();
		
		Collections.sort(specificOrder, new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				// TODO Auto-generated method stub
				return (int) (o1.getProductWeightTotal() - o2.getProductWeightTotal());
			}
		});
		
		return specificOrder;
	}



	private ArrayList<ICommand> loadDroneAndDelivery(Drone drone, Warehouse currentDroneWarehouse, ArrayList<Order> orders) {
		ArrayList<ICommand> loadCommands = new ArrayList<>();
		
		boolean mustRestart = false;
		do {
			mustRestart = false;
			for (Order order : orders) {
//				int distMax = Math.max(dataLoader.getWordSimulation().getCols(), dataLoader.getWordSimulation().getRows());
//				if(currentDroneWarehouse.getDistance(order) > distMax / 3)
//					break;
				Set<ProductType> availiableProducts = order.getAvailiableProducts();
				for (ProductType productType : availiableProducts) {
					int qty = order.getProductQty(productType);
					for(int quantity = qty ; quantity >=1 ; quantity--) {
						
						int totalWeight = quantity * productType.getWeight();
						if(drone.getRestPayload() >= totalWeight
								&& currentDroneWarehouse.getProductQty(productType) > 0) {
							
							Load cmd = null;
							if(currentDroneWarehouse.getProductQty(productType)>=quantity) {
								cmd = new Load(drone, currentDroneWarehouse, productType, quantity, order);
							}
							else {
								cmd = new Load(drone, currentDroneWarehouse, productType, currentDroneWarehouse.getProductQty(productType), order);
							}
							cmd.exec(loadCommands);		
							mustRestart = true;
							break;
						}
					}
					if(mustRestart)
						break;
				}

				if(mustRestart)
					break;
			}
		}while(mustRestart);		
		
		ArrayList<ICommand> unloadCommands = new ArrayList<>();
		for (ICommand command : loadCommands) {
			Load loadCmd = (Load) command;
			Deliver cmd = new Deliver(drone, loadCmd.getOrder(), loadCmd.getProductType(), loadCmd.getQuantity());
			cmd.exec(unloadCommands);
		}
		
		ArrayList<ICommand> fullCommand = new ArrayList<>();
		fullCommand.addAll(loadCommands);
		fullCommand.addAll(unloadCommands);
		
		return fullCommand;
	}



	public HashMap<Drone, Warehouse> createDroneRepartion() {
		int nbWarehouse = dataLoader.getWarehouses().size();
		int nbDrone = dataLoader.getDrones().size();
		int nbDronePerWarehouse = nbDrone / nbWarehouse;
		
		HashMap<Drone, Warehouse> defaultWarehouse = new HashMap<>();
		int droneToPlace = nbDrone;
		int j = nbWarehouse - 1;
		do {
			for(int i = 0 ; i < nbDronePerWarehouse ; i++) {
				defaultWarehouse.put(dataLoader.getDrones().get(--nbDrone), dataLoader.getWarehouses().get(j));
				droneToPlace--;
			}
			j--;
		}while(droneToPlace > 0);
		return defaultWarehouse;
	}

}
