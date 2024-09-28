package simulation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import gui.GUISimulator;
import gui.GraphicalElement;
import gui.ImageElement;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;

public abstract class Robot {

    protected Case position;      // The case where the Robot is
    protected int reservoir;      // How much water does the Robot has in his reservoir
    protected boolean available;  // Robot may be unavailable while refilling
    protected ImageElement image; // The image representing the Robot in the Simulation
    protected long lastTimeEvent; // The time when the Robot is going to finish its last task (event)

    /*Constructor */
    protected Robot(Case position, int reservoir) {
        this.position = position;
        this.reservoir = reservoir;
        if (0 < reservoir) this.available = true;
        else this.available = false;
    }

    /* Getters */
    public Case getPosition() {
        return this.position;
    }

    public boolean getAvailable() {
        return this.available;
    }
    
    public long getLastTimeEvent() {
        return this.lastTimeEvent;
    }
    
    public ImageElement getImage() {
        return this.image;
    }

    public int getReservoir() {
        return this.reservoir;
    }

    /* Setters */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setLastTimeEvent(long lastTimeEvent) {
        this.lastTimeEvent = lastTimeEvent;
    }


    public void setImage(ImageElement image) {
        this.image = image;
    }


    public void setPosition(Case pos) {
        this.position = pos;
    }


    public void setReservoir(int reservoir) {
        this.reservoir = reservoir;
    } 

    /* Useful Methods */

    /**
     * @param map The Map 
     * @return the closest Case from where the Robot can fill its reservoir. 
     * ( For drone Robots we Override this method to check if it is actually on a water case and not just neighbor to a water case)
     */
    public Case findNearestWater(Map map) {
        PathToCase path = new PathToCase(this, map);
        ArrayList<Case> waterCases = map.getWaterCases();
        long minTimeToCase = (long) Math.pow(2 , 63) - 1;
        Case nearestWater = waterCases.get(0);
        for( Case waterCase : waterCases ){
            for( Direction direction : Direction.values()){
                Case src = this.getPosition();  
                long timetoCase = 0;
                if(map.neighborExistence(waterCase, direction)){
                    LinkedList<Direction> directions = path.Dijkstra(src, map.getNeighbor(waterCase,direction), map);
                    for ( Direction dir : directions){
                        if (src != null) {
                        timetoCase += this.timeToCase(src, dir, map);
                        src = map.getNeighbor(src, dir);
                        }
                    }
                    if ( timetoCase <= minTimeToCase ){
                        minTimeToCase = timetoCase;
                        nearestWater = waterCase;
                    }
                }
                
            }
        }
        return nearestWater;
    }

    /**
     * @return the Robot reservoir's volume. 
     */
    public abstract int getInterventionVol();

    /**
     * @return the time it takes for a Robot to make an Intervention (spillWater). 
     */
    public abstract long getInterventionTime();

    /**
     * @return the time it takes for a Robot to fill his reservoir (fillReservoir).
     */
    public abstract long getFillingTime();

    /**
     * @param nature LandNature type
     * @return the Robot's speed depending on the LandNature of the Case he is in.
     */
    public abstract double getSpeed(LandNature nature);

    public abstract void setSpeed(double speed);

    /**
     * The Robot makes one intervention on the given Fire by spilling Water
     *from its reservoir if he has any Water left.
     */
    public abstract void spillWater(Fire pos);

    /**
     * Fills the Robot's reservoir and makes him available after that.
     */
    public abstract void fillReservoir();

    /**
     * @param src The start Case 
     * @param dir The moving direction
     * @param map The Map
     * @return the time it takes for a Robot to go from src to the case in the given dir. 
     */
    public abstract long timeToCase(Case src, Direction dir, Map map);

    /**
     * This method changes the Robot position to the Case in his dir.
     * @param dir The moving direction
     * @param map The map
     */
    public abstract void goToCase(Direction dir, Map map);

}