package tsc;

import java.io.IOException;

import model.Data;
import model.Order;

public class TestOrderWeight {

	public static void main(String[] args) {

		try {
			Data data = Data.getInstance("./data/busy_day.in");
			
			for(Order o : data.getOrdersByWeightDesc()) {
				System.out.println(o.getTotalWeight());
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
