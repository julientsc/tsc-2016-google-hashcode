package exec;

import java.io.IOException;

import simulation.SimulationA;
import simulation.SimulationB;
import simulation.SimulationC;
import simulation.SimulationD;
import simulation.SimulationE;
import simulation.SimulationF;
import simulation.SimulationMotherOptimizer;
import tools.DataLoader;

public class Main {

	public static void main(String[] args) throws IOException {

//		new SimulationF(new DataLoader("data/busy_day.in"), "busy_day.out").exec();
//		new SimulationF(new DataLoader("data/redundancy.in"), "redundancy.out").exec();
		new SimulationMotherOptimizer(new DataLoader("data/mother_of_all_warehouses.in"), "mother_of_all_warehouses.out").exec();
	}

}
