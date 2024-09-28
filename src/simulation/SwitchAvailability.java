package simulation;

public class SwitchAvailability extends Event{
    
    Robot robot;  // The Robot which availabilty to be updated
    
    /* Constructor */
    public SwitchAvailability(Robot robot, long dateSimulation) {
        super(robot.getLastTimeEvent());
        this.robot = robot;
    }

    @Override
    /**
     * This event makes a Robot available
     */
    public void execute() {
        this.robot.setAvailable(true);
    }
}
