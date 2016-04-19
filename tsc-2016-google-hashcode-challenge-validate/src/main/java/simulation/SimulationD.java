package simulation;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import command.Deliver;
import command.ICommand;
import command.Load;
import model.Drone;
import model.Order;
import model.ProductType;
import model.Warehouse;
import tools.DataLoader;

public class SimulationD {

	DataLoader dataLoader = null;
	private String output;
	
	Random r = new Random();

	public SimulationD(DataLoader dataLoader, String output) {
		this.dataLoader = dataLoader;
		this.output = output;
	}
	

	private Warehouse getClosestWarehouse(Drone drone, HashMap<Drone,Warehouse> dronesRepartition) {
		Double dist = Double.MAX_VALUE;
		Warehouse closed = null;
		for (Warehouse w : dataLoader.getWarehouses()) {
			if (w.getDistance(drone.getLocation()) < dist) {
				dist = w.getDistance(drone.getLocation());
				closed = w;
			}
		}
		return closed;
	}

	public void exec() throws IOException {
		
		ArrayList<ICommand> commands = new ArrayList<>();
		
		HashMap<Drone, Warehouse> dronesRepartition = createDroneRepartion();
		
		int minCurrentCost = 0;
		boolean justOne = true;
		do {
			for (Drone drone : dronesRepartition.keySet()) {
				if(drone.getCostOptimized()==minCurrentCost) {
					justOne = true;
					Warehouse currentDroneWarehouse = dronesRepartition.get(drone);
					ArrayList<Order> orders = new ArrayList<>();
					
					
					orders = getOrderInOneStep(currentDroneWarehouse);
					
					
					if(orders.size() == 0) {
						orders = getOrderSortedByDistanceFrom(currentDroneWarehouse);
						justOne = false;
					} 
					
					ArrayList<ICommand> cmds = loadDroneAndDelivery(drone, currentDroneWarehouse, orders, justOne);
					if(cmds.size() == 0) {
						int j = 0;
						do {
							dronesRepartition.put(drone, dataLoader.getWarehouses().get(j));
							cmds = loadDroneAndDelivery(drone, dronesRepartition.get(drone), orders, justOne);
							j++;
						}while(cmds.size() == 0 && j < dataLoader.getWarehouses().size());
					} else {
						dronesRepartition.put(drone, getClosestWarehouse(drone, dronesRepartition));
					}
					commands.addAll(cmds);
					
					
					dronesRepartition.put(drone, getClosestWarehouse(drone, dronesRepartition));
				} 
			}
			
			int maxCurrentCost = Integer.MAX_VALUE;
			for (Drone drone : dronesRepartition.keySet()) {
				maxCurrentCost = Math.min(maxCurrentCost, drone.getCostOptimized());
			}
			if(minCurrentCost == maxCurrentCost)
				break;
			minCurrentCost = maxCurrentCost;
		}while(true);
		
		PrintWriter pw = new PrintWriter(new FileWriter(this.output));
		pw.println(commands.size());
		for (ICommand iCommand : commands) {
			pw.println(iCommand.toString());
		}
		pw.close();
		
		for(Drone d : dataLoader.getDrones())
			System.out.println(d.getCostOptimized());
	}
	
	private ArrayList<Order> getOrderInOneStep(Warehouse currentDroneWarehouse) {
		ArrayList<Order> orders = new  ArrayList<>();
		for(Order order : dataLoader.getOrders()) {
			if(currentDroneWarehouse.contains(order) && order.getProductWeightTotal() <= dataLoader.getWordSimulation().getPayload()) {
				orders.add(order);
			}
		}
		
		Collections.sort(orders, new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				// TODO Auto-generated method stub
//				return (int) (o1.getProductWeightTotal() - o2.getProductWeightTotal());
				return (int) (o1.getDistance(currentDroneWarehouse) - o2.getDistance(currentDroneWarehouse));
			}
		});
		
		return orders;
	}


	private ArrayList<Order> getOrderSortedByDistanceFrom(Warehouse warehouse) {
		ArrayList<Order> specificOrder = (ArrayList<Order>) dataLoader.getOrders().clone();
		
		Collections.sort(specificOrder, new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				// TODO Auto-generated method stub
//				return (int) (o1.getProductWeightTotal() - o2.getProductWeightTotal());
				return (int) (o1.getDistance(warehouse) - o2.getDistance(warehouse));
			}
		});
		
		return specificOrder;
	}


	static int instuction = 0;

	private ArrayList<ICommand> loadDroneAndDelivery(Drone drone, Warehouse currentDroneWarehouse, ArrayList<Order> orders, boolean justOne) {
		ArrayList<ICommand> loadCommands = new ArrayList<>();
		
			boolean mustRestart = false;
			do {
				mustRestart = false;
				Collections.sort(orders, new Comparator<Order>() {
					@Override
					public int compare(Order o1, Order o2) {
						return (int)(o1.getDistance(currentDroneWarehouse) - o2.getDistance(currentDroneWarehouse));
					}
				}); 
				for (Order order : orders) {
					ArrayList<ProductType> availiableProducts = order.getAvailiableProductsByWeight();
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
								
								if(!justOne)
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
		
		
//		if(loadCommands.size()>0)
//		System.out.println(drone.getRestPayload());
		
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
				defaultWarehouse.put(dataLoader.getDrones().get(--nbDrone), dataLoader.getWarehouses().get(0));
				droneToPlace--;
			}
			j--;
		}while(droneToPlace > 0);
		return defaultWarehouse;
	}

}
