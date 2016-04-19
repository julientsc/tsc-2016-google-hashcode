package model;

public class Location {

    private int x;
    private int y;
        
    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns distance betwen two locations
     * @param location
     * @return
     */
    public double getDistance(Location location){
        return Math.sqrt(Math.pow(getX() - location.getX(), 2) + Math.pow(getY() - location.getY(), 2));
    }
    
    public int getFlightTime(Location location){
        return (int) Math.ceil(getDistance(location));
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
}
