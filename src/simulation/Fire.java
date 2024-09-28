package simulation;
public class Fire {
    
    private Case position;    // The Case where the fire is.
    private int neededWater;  // The amount of water to extinguish the fire.

    /* Constructor */
    public Fire(Case position, int neededWater) {
        this.neededWater = neededWater;
        this.position = position;
        position.setIsOnFire(true);

    }

    /* Getters */
    public Case getPosition() {
        return this.position;
    }
    
    public int getNeededWater() {
        return this.neededWater;
    }
    
    /* Setters */
    public void setPosition(Case position) {
        this.position = position;
    }
    
    public void setNeededWater(int neededWater) {
        this.neededWater = neededWater;
    }
}
