package simulation;

public class Case {
    
    private int line;                  // The case position
    private int col;                   // The case position
    private LandNature nature;         // The case land nature
    private boolean isOnFire = false;  // The case fire state

    /* Constructor */
    public Case(int line, int col, LandNature nature) {
        this.line = line;
        this.col = col;
        this.nature = nature;
    }

    /* Getters */
    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public LandNature getNature() {
        return nature;
    }

    public boolean getIsOnFire() {
        return this.isOnFire;
    }
    
    /* Setters */
    public void setIsOnFire(boolean status) {
        this.isOnFire = status;
    }

    /* Useful methods */

    /**
     * @return True if the case is not of type Water. 
     */
    public boolean isAccessible() {
        return this.nature != LandNature.WATER;
    }

    /**
     * @return True if the case is not of type Rock. 
     */
    public boolean isSafe() {
        return this.nature != LandNature.ROCK;
    }

    /**
     * @return True if the case is of type Habitat. 
     */
    public boolean isHabitable() {
        return this.nature == LandNature.HABITAT;
    }

}
