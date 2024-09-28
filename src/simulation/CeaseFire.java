package simulation;

import java.util.Set;

public class CeaseFire extends Event {
    
    Simulation sim;
    Robot robot;      // The robot that ceased the fire
    Fire fire;        // The ceased fire  
    Set<Fire> fires;  // The fires on the simulation

    /* Constructor */
    public CeaseFire(Simulation sim, long dateSimulation, Fire fire, Robot robot, Set<Fire> fires) {
        super(dateSimulation);
        this.sim = sim;
        this.robot = robot;
        this.fire = fire;
        this.fires = fires;
    }

    @Override
    /**
     * This event updates the simulation when a fire is ceased by removing it from fires.
     */
    public void execute() {
        fires.remove(fire);
        if (fire.getPosition().getIsOnFire() == false) {
            sim.drawMap();
            sim.drawFire();
            sim.drawRobots();
        }
    }
}
