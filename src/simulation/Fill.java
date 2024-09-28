package simulation;

public class Fill extends Event {
    
    Robot robot; // The robot whom reservoir to be filled

    /* Constructor */
    public Fill(long date, Robot robot) {
        super(max(date, robot.getLastTimeEvent()) + robot.getFillingTime());
        robot.setLastTimeEvent(max(date, robot.getLastTimeEvent()) + robot.getFillingTime());
        this.robot = robot;
        robot.setAvailable(false);
    }
    
    @Override
    /**
     * This event fills the robot's reservoir.
     */
    public void execute() {
        this.robot.fillReservoir();
    }
}