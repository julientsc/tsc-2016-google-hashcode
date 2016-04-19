package command;

import java.util.ArrayList;

import model.Drone;

public class Wait implements ICommand {

	private Drone drone;
	private String TAG = "W";
	private int nbTurn;
	
	public Wait(Drone drone, int nbTurn) {
		this.drone = drone;
		this.nbTurn = nbTurn;
	}
	
	public void exec(ArrayList<ICommand> commands) {
		commands.add(this);
		System.out.println(toString());
	}
	
	public String toString() {
		return drone.getId() + TAG + nbTurn;
	}
}
