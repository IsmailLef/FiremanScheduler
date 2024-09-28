package simulation;

public class Move extends Event{
    
    protected Robot robot;    // The Robot to move
    protected Direction dir;  // The moving direction
    protected Map map;        // The map

    /** Constructor  
     * @param date here is current date given by the simulator
     * @param robot
     * @param dir
     * @param map
     */
    public Move(long date, Robot robot, Direction dir, Map map) { 
        super(max(date, robot.getLastTimeEvent()) + robot.timeToCase(robot.getPosition(), dir, map));
        //we update when it's the last time the robot has moved
        robot.setLastTimeEvent(max(date, robot.getLastTimeEvent()) + robot.timeToCase(robot.getPosition() , dir, map));
        this.robot = robot;
        this.dir = dir;
        this.map = map;
    }

    @Override
    /**
     * This event changes the robot position
     */
    public void execute() {
        this.robot.goToCase(this.dir, this.map);
    }

    /* Getters */
    public Robot getRobot() {
        return this.robot;
    }

    public Direction getDir() {
        return dir;
    }
}
