package simulation;
import java.util.ArrayList;
import java.util.LinkedList;

public class Drone extends Robot {
    
    protected final int droneReservoir = 1000000; // The drone's reservoir capacity in L.
    protected final long fillingTime = 30*60;     // The time it takes for a drone to fill its reservoir in sec.
    protected final long interventionTime = 30;   // The time it takes for a drone to make on intervention in sec.
    protected double speed;                       // The Robot's speed.                                         
 
    /* Constructor if using the default speed */
    public Drone(Case position) {
        super(position, 10000);
        this.speed = 100;
    }

    /* Constructor if using the file's speed */
    public Drone(Case position, double speed) {
        super(position, 10000);
        this.speed = speed;
    }

    /* Getters */
    public int getInterventionVol() {
        return this.droneReservoir;
    }

    public long getFillingTime() {
        return this.fillingTime;
    }
    
    public long getInterventionTime() {
        return this.interventionTime;
    }

    public double getSpeed(LandNature nature) {
        return this.speed;
    }

    /* Setters */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /* Useful Methods */

    public void spillWater(Fire pos) { //Active for _interventionTime_ only. Manage timer..
        if (this.position.getIsOnFire()) {
            int neededVol = pos.getNeededWater();
            if (droneReservoir < neededVol) {
                pos.setNeededWater(neededVol - droneReservoir);
            }
            else { // Reservoir contains enough to exctinguish the fire
                pos.setNeededWater(0);
                this.position.setIsOnFire(false); // Fire is extinguished - Changing its status
            }
            this.setReservoir(0);
            this.available = false;
        } 
        else { 
            throw new IllegalAccessError("Drone is not on fire case");
        }
    }

    public long timeToCase(Case src, Direction dir, Map map) { // No need to average the speed for Drone. Same speed
        return (long) ((map.getSize() * 3.6)/ this.speed);
    }

    public void fillReservoir() { 
        if (this.position.getNature() == LandNature.WATER) { // Check if drone on water case
            this.setReservoir(droneReservoir);
            this.available = true;  // Make drone available
        }
        else {
            throw new IllegalAccessError("Can't fill reservoir in a case that doesn't contain water");
        }
    }

    public void goToCase(Direction dir, Map map) {
        Case dest = map.getNeighbor(this.position, dir);
        if (dest == null) throw new IllegalAccessError("The drone is out of the map"); 
        this.position = dest;
    }

    @Override
    //For drone we check if we are on a water case to fill the reservoir
    public Case findNearestWater(Map map) {
        PathToCase path = new PathToCase(this, map);
        ArrayList<Case> waterCases = map.getWaterCases();
        long minTimeToCase = (long) Math.pow(2 , 63) - 1;
        Case nearestWater = waterCases.get(0);
        for( Case waterCase : waterCases ){
            Case src = this.getPosition();  
            long timetoCase = 0;
            LinkedList<Direction> directions = path.Dijkstra(src, waterCase, map);
            for ( Direction dir : directions){
                timetoCase += this.timeToCase(src, dir, map);
                if (src != null) src = map.getNeighbor(src, dir);
            }
            if ( timetoCase <= minTimeToCase ){
                minTimeToCase = timetoCase;
                nearestWater = waterCase;
            }
        }
        return nearestWater;
    }

}
