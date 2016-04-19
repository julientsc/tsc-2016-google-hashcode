package simulator;

import java.util.HashMap;

import model.Warehouse;

public class Map {
    
    private final HashMap<Integer, Warehouse> warehouses;
    
    public Map(HashMap<Integer, Warehouse> warehouses){
        this.warehouses = warehouses;
    }
    

}
