package simulation;
public class WheelRobot extends Robot {

    protected final int wheelRobotReservoir = 5000;    // The WheelRobot's reservoir capacity in L.
    protected final long fillingTime = 10*60;          // The time it takes for a wheelRobot to fill its reservoir in sec.
    protected final long interventionTime = 5;         // The time it takes for a wheelRobot to make on intervention in sec.
    protected final int interventionVol = 1000000;     // The amount of water the WheelRobot spills in one intervention in L.
    protected double speed;                            // The Robot's speed.
    
    
    /* Constructor if using the default speed */
    public WheelRobot(Case position) {
        super(position, 5000);
        this.speed = 80;
        if (position.getNature() != LandNature.HABITAT && position.getNature() != LandNature.FREE_LAND) {
            throw new IllegalArgumentException();
        }
    }
    
    /* Constructor if using the file's speed */
    public WheelRobot(Case position, double speed) {
        super(position, 5000);
        this.speed = speed;
        if (position.getNature() != LandNature.HABITAT && position.getNature() != LandNature.FREE_LAND) {
            throw new IllegalArgumentException();
        }
    }

    /* Getters */
    public int getInterventionVol() {
        return interventionVol;
    }

    public long getInterventionTime() {
        return this.interventionTime;
    }

    public long getFillingTime() {
        return this.fillingTime;
    }

    public double getSpeed(LandNature nature) {
        return this.speed;
    }

    /* Setters */
    public void setSpeed(double speed) {    // Doesn't handle speed. Doesn't change
        throw new IllegalCallerException();
    }

    /* Useful Methods */

    public void spillWater(Fire pos) {
        if (this.position.getIsOnFire()) {
            int neededVol = pos.getNeededWater();
            if (interventionVol < neededVol) { // interventionVol is not enough to exctinguish the fire
                pos.setNeededWater(neededVol - interventionVol);
            }
            else { // interventionVol is enough to exctinguish the fire
                pos.setNeededWater(0);
                this.position.setIsOnFire(false); // Fire is extinguished - Changing its status
            }
            this.setReservoir(this.reservoir - interventionVol);    // Updating reservoir
            if (this.reservoir == 0) this.available = false;
        }
        else { // Should not spill since it's not on fire
            throw new IllegalAccessError("WheelRobot is not on fire case");
        }
    }

    public long timeToCase(Case src, Direction dir, Map map) { // No need to average the speed for WheelRobot. Same speed
        return (long) ((map.getSize() * 3.6) / this.speed);
    }

    public void fillReservoir() { 
        if (this.position.getNature() == LandNature.WATER) {
            this.setReservoir(wheelRobotReservoir);
            this.available = true;  // Make Robot available
        }
        else {
            throw new IllegalAccessError("Can't fill reservoir in a case that doesn't contain water");
        }
    }

    public void goToCase(Direction dir, Map map) {
        Case dest = map.getNeighbor(this.position, dir);
        if (dest != this.position) {
            if (dest.getNature() == LandNature.FREE_LAND || dest.getNature() == LandNature.HABITAT)
                this.position = dest;
            else throw new IllegalAccessError("WheelRobot cannot go through ");
        }
        if (dest == null) throw new IllegalAccessError("The wheelRobot is out of the map"); 
    }
}
