package tsc;

import java.io.IOException;

import model.Data;

public class TestLoader {

	public static void main(String [] args) {
		try {
			Data data = Data.getInstance("./data/simple.in");
			
			data.getOrders().get(0).getWarehouseSortedByDistance();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
