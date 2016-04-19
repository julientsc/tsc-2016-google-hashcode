package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Drone;
import model.Order;
import model.ProductType;
import model.Warehouse;
import model.WordSimulation;

public class DataLoader {

	private HashMap<Integer, Order> orders = new HashMap<>();
	private HashMap<Integer, Warehouse> warehouses = new HashMap<>();
	private WordSimulation wordSimulation;
	private ArrayList<Drone> drones = new ArrayList<>();

	public ArrayList<Order> getOrders() {
		ArrayList<Order> r = new ArrayList<>();
		for (Order order : orders.values())
			r.add(order);
		return r;
	}

	public ArrayList<Drone> getDrones() {
		return drones;
	}

	public ArrayList<Warehouse> getWarehouses() {
		ArrayList<Warehouse> r = new ArrayList<>();
		for (Warehouse warehouse : warehouses.values())
			r.add(warehouse);
		return r;
	}

	public WordSimulation getWordSimulation() {
		return wordSimulation;
	}

	public int[] splitInteger(String line) {
		String[] splitString = line.split(" ");
		int[] splitInteger = new int[splitString.length];
		for (int i = 0; i < splitString.length; i++)
			splitInteger[i] = Integer.parseInt(splitString[i]);
		return splitInteger;
	}

	public DataLoader(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));

		String worldSimulationString = br.readLine();
		int[] worldSimulation = splitInteger(worldSimulationString);
		this.wordSimulation = new WordSimulation(worldSimulation[0], worldSimulation[1], worldSimulation[2],
				worldSimulation[3], worldSimulation[4]);

		int weightCount = Integer.parseInt(br.readLine());
		int[] productsWeight = splitInteger(br.readLine());
		HashMap<Integer, ProductType> productType = new HashMap<>();
		for (int productTypeId = 0; productTypeId < weightCount; productTypeId++) {
			productType.put(productTypeId, new ProductType(productTypeId, productsWeight[productTypeId]));
		}

		int warehouseCount = Integer.parseInt(br.readLine());
		for (int warehouseId = 0; warehouseId < warehouseCount; warehouseId++) {
			int[] coords = splitInteger(br.readLine());
			Warehouse warehouse = new Warehouse(warehouseId, coords[0], coords[1]);

			int[] productQty = splitInteger(br.readLine());
			for (int productId = 0; productId < productQty.length; productId++) {
				for (int i = 0; i < productQty[productId]; i++) {
					warehouse.addProdct(productType.get(productId));
				}
			}
			warehouses.put(warehouseId, warehouse);
		}

		int orderCount = Integer.parseInt(br.readLine());
		for (int orderId = 0; orderId < orderCount; orderId++) {
			int[] coords = splitInteger(br.readLine());
			Order order = new Order(orderId, coords[0], coords[1]);

			int productCount = Integer.parseInt(br.readLine());
			int[] productToAdd = splitInteger(br.readLine());
			for (int i = 0; i < productCount; i++) {
				order.addProdct(productType.get(productToAdd[i]));
			}
			orders.put(orderId, order);
		}

		for (int i = 0; i < wordSimulation.getDrones(); i++) {
			drones.add(new Drone(i, wordSimulation.getPayload(), warehouses.get(0)));
		}
		br.close();
	}

}
