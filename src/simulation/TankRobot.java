package simulation;
public class TankRobot extends Robot {
    
    protected final int TankRobotReservoir = 2000; // The TankRobot's reservoir capacity in L.
    protected final long fillingTime = 5*60;       // The time it takes for a TankRobot to fill its reservoir in sec.
    protected final long interventionTime = 8;     // The time it takes for a TankRobot to make on intervention in sec.
    protected final int interventionVol = 1000000; // The amount of water the TankRobot spills in one intervention in L.
    protected double defaultSpeed = 60; // The Robot's default speed.
    protected double speed; // The Robot's speed.
    
    /* Constructor if using the file's speed */
    public TankRobot(Case position, double speed) {
        super(position, 2000);
        if ( speed > 80 ){
            throw new IllegalArgumentException(" A TANK ROBOT'S SPEED CAN NOT BE OVER 80");
        }
        this.defaultSpeed = speed;
        if ( position.getNature() == LandNature.FOREST ){
            this.speed = speed * 0.5;
        }
        this.speed = speed;
        if ( (position.getNature() == LandNature.WATER) || (position.getNature() == LandNature.ROCK)){
            throw new IllegalArgumentException("A TANK ROBOT CAN NOT BE CREATED ON WATER OR ROCK");
        }
    }

    /* Constructor if using the default speed */
    public TankRobot(Case position) {
        super(position, 2000);
        if ( speed > 80 ){
            throw new IllegalArgumentException(" A TANK ROBOT'S SPEED CAN NOT BE OVER 80");
        }
        if ( position.getNature() == LandNature.FOREST ){
            this.speed = defaultSpeed * 0.5;
        }
        this.speed = defaultSpeed;
        if ( (position.getNature() == LandNature.WATER) || (position.getNature() == LandNature.ROCK)){
            throw new IllegalArgumentException("A TANK ROBOT CAN NOT BE CREATED ON WATER OR ROCK");
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
        if ( nature == LandNature.FOREST ){
            return this.defaultSpeed * 0.5;
        }
        return this.defaultSpeed;
    }

    /* Setters */
    public void setSpeed(double speed) {
        this.speed = speed;
    }


    /* Useful Methods */
    public void spillWater(Fire pos) {
        if (this.position.getIsOnFire()) {
            int neededVol = pos.getNeededWater();
            if (interventionVol < neededVol) { // Reservoir does not contain enough to exctinguish the fire
                pos.setNeededWater(neededVol - interventionVol);
            }
            else { // Reservoir contains enough to exctinguish the fire
                pos.setNeededWater(0);
                this.position.setIsOnFire(false); // Fire is extinguished - Changing its status
            }
            this.setReservoir(this.reservoir - interventionVol);    // Updating reservoir
            if (this.reservoir == 0) this.available = false;
        }
        else { // Should not spill since it's not on fire
            throw new IllegalAccessError("TankRobot is not on fire case");
        }
    }

    public long timeToCase(Case src, Direction dir, Map map) { // No need to average the speed for tankRobot. Same speed
        Case goCase = map.getNeighbor(src, dir);
        double averageSpeed = (getSpeed(src.getNature()) + getSpeed(goCase.getNature())) / 2;
        return (long) (map.getSize() / averageSpeed);
    }

    public void fillReservoir() { // Instant Filling - Need to wait 5min ?
        if (this.position.getNature() == LandNature.WATER) {
            this.setReservoir(TankRobotReservoir);
            this.available = true;
        }
        else {
            throw new IllegalAccessError("Can't fill reservoir in a case that doesn't contain water");
        }
    }

    public void goToCase(Direction dir, Map map) {
        Case dest = map.getNeighbor(this.position, dir);
        if (dest == null) throw new IllegalAccessError("The tankrobot is out of the map"); 
        if (dest != this.position) {
            if (dest.getNature() != LandNature.WATER || dest.getNature() != LandNature.ROCK)
                this.position = dest;
            else throw new IllegalAccessError("tankRobot cannot go through ");
        }

    }
}
