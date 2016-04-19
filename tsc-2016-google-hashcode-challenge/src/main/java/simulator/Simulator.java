package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commands.Command;

public class Simulator {
    private final Map<Integer, List<Command>> droneCommands;
    
    private final int maxSteps;
    
    public Simulator(List<Command> commands, int maxSteps){
        droneCommands = getCommandsPerDrone(commands);
        this.maxSteps = maxSteps;
    }
    
    public void simulate(){
    
        int stepCounter = 0;
        
        do{
            step();
            stepCounter++;
        }while(stepCounter < maxSteps || allOrdersCompleted());
    
    }
    
    private Map<Integer, List<Command>> getCommandsPerDrone(List<Command> commands){
        Map<Integer, List<Command>> droneCommands = new HashMap<Integer, List<Command>>();
        
        for(Command command: commands){
            if(!droneCommands.containsKey(command.getDroneId())){
                droneCommands.put(command.getDroneId(), new ArrayList<Command>());
            }
            
            droneCommands.get(command.getDroneId()).add(command);
        }   
        
        return droneCommands;
    }
    
    private void step(){
        
    }
    
    private boolean allOrdersCompleted(){
        return false;
    }
}
