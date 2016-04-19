package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    
    public static final String SIMPLE = "data/simple.in";
    public static final String BUSY_DAY = "data/busy_day.in";

	private SimulationParameter simulationParameter = null;
	
	private static Data instance = null;
	
	public static Data getInstance() {
		return instance;
	}
	
	public static Data getInstance(String filename) throws IOException {
		if(instance == null)
			instance = new Data(filename);
		
		return instance;
	}
	
	
	public SimulationParameter getSimulationParameter() {
		return simulationParameter;
	}

	public ArrayList<Order> getOrdersByWeightDesc() {
		ArrayList<Order>  orders = new ArrayList<Order>();
		for (Order order : this.orders.values()) {
			orders.add(order);
		}
		
		Collections.sort(orders, new Comparator<Order>() {

			public int compare(Order o1, Order o2) {
				return o2.getTotalWeight() - o1.getTotalWeight();
			}

			
		});
		return orders;
	}

	private HashMap<Integer, Warehouse> warehouses = new HashMap<Integer, Warehouse>();
	private HashMap<Integer, Product> product = new HashMap<Integer, Product>();
	private HashMap<Integer, Order> orders = new HashMap<Integer, Order>();
	private Map<Integer, Drone> drones = new HashMap<Integer, Drone>();

	public HashMap<Integer, Warehouse> getWarehouses() {
		return warehouses;
	}

	public HashMap<Integer, Product> getProduct() {
		return product;
	}
	
	public Map<Integer, Drone> getDrones(){
	    return drones;
	}

	public HashMap<Integer, Order> getOrders() {
		return orders;
	}

	private Data(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));

		this.simulationParameter = new SimulationParameter(br.readLine().split(" "));

		String weightCount = br.readLine();
		System.out.println("Load weight " + weightCount);
		String productsWeight = br.readLine();
		String[] weights = productsWeight.split(" ");
		for (int productId = 0; productId < weights.length; productId++) {
			product.put(productId, new Product(productId, Integer.parseInt(weights[productId])));
		}

		int warehouseCount = Integer.parseInt(br.readLine());
		System.out.println("Load warehouse " + warehouseCount);
		for (int warehouseId = 0; warehouseId < warehouseCount; warehouseId++) {
			String[] wareHouseCoords = br.readLine().split(" ");
			Warehouse warehouse = new Warehouse(warehouseId, Integer.parseInt(wareHouseCoords[0]),
					Integer.parseInt(wareHouseCoords[1]), br.readLine().split(" "));
			warehouses.put(warehouseId, warehouse);

		}

		int ordersCount = Integer.parseInt(br.readLine());
		System.out.println("Load orders " + ordersCount);
		for (int orderId = 0; orderId < ordersCount; orderId++) {
			String[] orderCoords = br.readLine().split(" ");
			br.readLine();
			
			String [] prodTemp = br.readLine().split(" ");
			List<Product> productTemp = new ArrayList<Product>();
	        for (int i = 0; i < prodTemp.length; i++) {
	            int productid = Integer.parseInt(prodTemp[i]);
	            productTemp.add(getProduct().get(productid));
	        }
			
			Order order = new Order(orderId, Integer.parseInt(orderCoords[0]), Integer.parseInt(orderCoords[1]),
			        productTemp);
			orders.put(orderId, order);
		}

		br.close();
		
		Warehouse warehouseZero = warehouses.get(0);
		
		for(int i = 0; i < getSimulationParameter().getDroneCount(); i++){
		    Drone drone = new Drone(i, getSimulationParameter().getMaxPayload(), warehouseZero);
		    
		    drones.put(i, drone);
		}
	}

	public Order getOrdersFromWarehouse(Warehouse w) {
		double i = Double.MAX_VALUE;
		Order currentMinOrder = null;
		for(Order order : Data.getInstance().getOrdersByWeightDesc()) {
			if(order.getTotalWeight()==0)
				continue;
			
			if(order.getDistance(w)< i) {
				i = order.getDistance(w);
				currentMinOrder = order;
			}
		}
		return currentMinOrder;
	}

	public boolean hasOrder() {
		for(Order order : orders.values()) {
			if(order.getTotalWeight() > 0)
				return true;
		}
		return false;
	}
}
