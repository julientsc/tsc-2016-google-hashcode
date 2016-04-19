package model;

public class SimulationParameter {
    
    private final int mapWidth;
    private final int mapHeight;
    private final int droneCount;
    private final int maxRounds;
    private final int maxPayload;
    
	public SimulationParameter(String[] split) {
		mapWidth = Integer.parseInt(split[0]);
		mapHeight = Integer.parseInt(split[1]);
		droneCount = Integer.parseInt(split[2]);
		maxRounds = Integer.parseInt(split[3]);
		maxPayload = Integer.parseInt(split[4]);
	}

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getDroneCount() {
        return droneCount;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public int getMaxPayload() {
        return maxPayload;
    }

}
