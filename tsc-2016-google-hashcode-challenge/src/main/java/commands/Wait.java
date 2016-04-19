package commands;

public class Wait extends Command{
    
    public static char COMMAND_ABREV = 'W';
    
    private final int time;
    
    public Wait(int droneId, int waitTime) {
        super(droneId);
        this.time = waitTime;
    }

    @Override
    public String encode() {
        return getDroneId() +" "+COMMAND_ABREV+" "+time;
    }

}
