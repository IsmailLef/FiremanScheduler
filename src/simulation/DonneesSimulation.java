package simulation;

import java.util.HashSet;
import java.util.Set;

public class DonneesSimulation {

    protected Map map;
    protected Set<Fire> onFireCases;
    protected Robot[] robots;

    /* Getters */
    public Map getMap() {
        return this.map;
    }
    
    public Set<Fire> getOnFireCases() {
        return this.onFireCases;
    }

    public Robot[] getRobots() {
        return this.robots;
    }

    /* Setters */
    public void setMap(int size, int nbLine, int nbCol) {
        this.map = new Map(size, nbLine, nbCol);
    }


    public void setOnFireCases(int nbIncendies) {
        this.onFireCases = new HashSet<Fire>();
    }


    public void setRobots(int nbRobots) {
        this.robots = new Robot[nbRobots];
    }
}
