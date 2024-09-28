package simulation;

public class Intervention extends Event{
    
    Fire fire;      // The fire to be extinguished
    Robot robot;    // The robot that makes the intervention
    Simulation sim; 

    /* Constructor */
    public Intervention(long date, Robot robot, Fire fire, Simulation sim) {
        super(max(date, robot.getLastTimeEvent()) + robot.getInterventionTime());
        robot.setLastTimeEvent(max(date, robot.getLastTimeEvent()) + robot.getInterventionTime());
        robot.setAvailable(false);
        this.sim = sim;
        this.fire = fire;
        this.robot = robot;
    }

    @Override
    /**
     * This event makes the robot spill water on the fire. 
     */
    public void execute() {
        this.robot.spillWater(this.fire);
    }
}
