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
import model.Order;
import model.ProductType;
import model.Warehouse;
import tools.DataLoader;

public class SimulationB {

	DataLoader dataLoader = null;

	public SimulationB(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}
	
	public class Planif{
		private Warehouse warehouse;
		private Order order;

		public Planif(Order order, Warehouse warehouse) {
			this.order = order;
			this.warehouse = warehouse;
		}

		public Warehouse getWarehouse() {
			return warehouse;
		}

		public Order getOrder() {
			return order;
		}
	}
	public HashMap<Warehouse, ArrayList<Order>> getCommandReadyToDelivery() {
		HashMap<Warehouse, ArrayList<Order>> planifs = new HashMap<>();
		for(Order order : getOrderSortedByWeightFrom()) {
			double distance = Double.MAX_VALUE;
			Warehouse w = null;
			for(Warehouse warehouse : dataLoader.getWarehouses()) {
				if(warehouse.contains(order)) {
					double d = order.getDistance(warehouse);
					if(d<distance) {
						distance = d;
						w = warehouse;
					}
				}
			}
			if(w!=null) {
				if(!planifs.containsKey(w))
					planifs.put(w, new ArrayList<>());
				planifs.get(w).add(order);
			}
		}
		return planifs;
	}

	public void exec() throws IOException {
		ArrayList<ICommand> commands = new ArrayList<>();
		
		HashMap<Warehouse, ArrayList<Order>> firstPart = getCommandReadyToDelivery();
		HashMap<Drone, Warehouse> dronesRepartition = createDroneRepartion();
		int order = 0;
		do {
			order = commands.size();
			for (Drone drone : dronesRepartition.keySet()) {
				Warehouse currentDroneWarehouse = dronesRepartition.get(drone);
	
				ArrayList<ICommand> cmds = loadDroneAndDelivery(drone, currentDroneWarehouse, firstPart.get(currentDroneWarehouse));
				commands.addAll(cmds);
			}
		}
		while(hasNextOrder(firstPart) && order != commands.size());
		
		
//		HashMap<Drone, Warehouse> dronesRepartition = createDroneRepartion();
		
		for(int i = 0 ; i < 300 ; i++) {
			for (Drone drone : dronesRepartition.keySet()) {
				Warehouse currentDroneWarehouse = dronesRepartition.get(drone);
				ArrayList<Order> orders = getOrderSortedByDistanceFrom(currentDroneWarehouse);
				
				ArrayList<ICommand> cmds = loadDroneAndDelivery(drone, currentDroneWarehouse, orders);
				if(cmds.size() == 0) {
					int j = 0;
					do {
						dronesRepartition.put(drone, dataLoader.getWarehouses().get(j));
						cmds = loadDroneAndDelivery(drone, dataLoader.getWarehouses().get(j), orders);
						j++;
					}while(cmds.size() == 0 && j < dataLoader.getWarehouses().size());
				}
				commands.addAll(cmds);
				
			}
		}
		
		PrintWriter pw = new PrintWriter(new FileWriter("out.out"));
		pw.println(commands.size());
		for (ICommand iCommand : commands) {
			pw.println(iCommand.toString());
		}
		pw.close();
		
	}
	
	private boolean hasNextOrder(HashMap<Warehouse, ArrayList<Order>> orders) {
		for (Warehouse warehouse : orders.keySet()) {
			for(Order order : orders.get(warehouse)) {
				if(order.getProductWeightTotal() != 0)
					return true;
			}
		}
		return false;
	}

	private ArrayList<Order> getOrderSortedByWeightFrom() {
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
	
	private ArrayList<Order> getOrderSortedByDistanceFrom(Warehouse warehouse) {
		ArrayList<Order> specificOrder = (ArrayList<Order>) dataLoader.getOrders().clone();
		
		Collections.sort(specificOrder, new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				// TODO Auto-generated method stub
				return (int) (o1.getProductWeightTotal() - o2.getProductWeightTotal());
//				return (int) (o1.getDistance(warehouse) - o2.getDistance(warehouse));
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
		try {
		do {
			for(int i = 0 ; i < nbDronePerWarehouse ; i++) {
				defaultWarehouse.put(dataLoader.getDrones().get(--nbDrone), dataLoader.getWarehouses().get(j));
				droneToPlace--;
			}
			j--;
		}while(droneToPlace > 0);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return defaultWarehouse;
	}

}
