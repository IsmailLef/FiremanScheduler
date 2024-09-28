package simulation;
public class FootRobot extends Robot {
    
    protected final long interventionTime = 1;     // The time it takes for a footrobot to make on intervention in sec.
    protected final int interventionVol = 1000000; // The amount of water the footrobot spills in one intervention in L.
    protected double speed; // The Robot's speed
    
    /* Constructor if using the file's speed */
    public FootRobot(Case position, double speed) {
        super(position, 1); // Reservoir 1 because it can't be null.
        this.speed = speed;
        if (position.getNature() == LandNature.WATER) {
            throw new IllegalArgumentException();
        }
        available = true;
    }
    
    /* Constructor if using the default speed */
    public FootRobot(Case position) {
        super(position, 1); // Reservoir 1 because it can't be null.
        if (position.getNature() == LandNature.WATER) {
            throw new IllegalArgumentException();
        }
        available = true;
    }

    /* Getters */
    public int getInterventionVol() {
        return interventionVol;
    }

    public long getInterventionTime() {
        return interventionTime;
    }

    public long getFillingTime() {
        throw new IllegalAccessError("FootRobot has got no fillingTime : Infinite reservoir");
    }

    public double getSpeed(LandNature nature) {
        if (nature == LandNature.ROCK) {
            return 10;
        }
        else {
            return 30;
        }
    }

    /* Setters */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /* Useful Methods */

    public void spillWater(Fire pos) {
        if (this.position.getIsOnFire()) {
            int neededVol = pos.getNeededWater();
            if (interventionVol < neededVol) { 
                pos.setNeededWater(neededVol - interventionVol);
                this.available = true; //footrobot are always available!
            }
            else { // Intervention contains enough to exctinguish the fire
                pos.setNeededWater(0);
                this.position.setIsOnFire(false); // Fire is extinguished - Changing its status
            }
        }
        else { // Should not spill since it's not on fire
            throw new IllegalAccessError("FootRobot is not on fire case");
        }
    }

    public long timeToCase(Case src, Direction dir, Map map) {
        Case goCase = map.getNeighbor(src, dir);
        double averageSpeed = (getSpeed(src.getNature()) + getSpeed(goCase.getNature())) / 2;
        return (long) ((map.getSize() * 3.6) / averageSpeed);
    }
    
    public void fillReservoir() { 
        //no need to fill this type of robot
    }

    public void goToCase(Direction dir, Map map) {
        Case dest = map.getNeighbor(this.position, dir); 
        if (dest == null) throw new IllegalAccessError("The footrobot is out of the map"); 
        if (dest.getNature() == LandNature.WATER) {
            throw new IllegalAccessError("FootRobot can't go on water case");
        }
        this.position = dest; // existence and nature compatibility is verified here
    }
}
    
