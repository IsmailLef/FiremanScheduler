package simulation;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class SillyChief {
    
    protected DonneesSimulation donnees;
    protected PriorityQueue<Event> listEvents;
    protected long dateSimulation;
    protected Simulation sim;

    /* Constructor */
    public SillyChief(DonneesSimulation donnees, PriorityQueue<Event> listEvents, long dateSimulation, Simulation sim) {
        this.donnees = donnees;
        this.listEvents = listEvents;
        this.dateSimulation = dateSimulation;
        this.sim = sim;
    }

    /**
     * Updating time from simulation to stay in sync.
     * @param dateSimulation
     */
    public void updateDateFromSimulation(long dateSimulation) { 
        this.dateSimulation = dateSimulation;
    }
    
    /**
     * Sends Robots while there is a fire in the map.
     */
    public void extinguishAllFire() {
        Set<Fire> casesOnFire = donnees.getOnFireCases();
        Set<Fire> stillToCease = new HashSet<Fire>(); // Making a deepcopy from casesOnFire
        for (Fire fire : casesOnFire) stillToCease.add(fire);
        while (! stillToCease.isEmpty()) {       
            for (Fire fire : stillToCease) {
                if (fire.getPosition().getIsOnFire()) {
                    long date = sendRobot(fire);
                    if (date != 0) {stillToCease.remove(fire);
                    break;
                    }
                }
            }
        }
    }

    /**
     * @param fire
     * @return the time when the robot finishes his task ( it stays at 0 if no tasks done ). 
     */
    public long sendRobot(Fire fire) {
        Map map = this.donnees.getMap();
        long dateFinish = 0;
        for (Robot robot : donnees.getRobots()) {
            if (robot.getAvailable()) {
                PathToCase pathConstructor = new PathToCase(robot, map);
                if (pathConstructor.getEvenementsToCase(fire.getPosition(), robot, listEvents, dateSimulation)) { //Checks also if Path exists
                    for (int i = 0; i < countNbOfIntervention(robot, fire); i++) {
                        listEvents.add(new Intervention(dateSimulation, robot, fire, sim));
                        // The following code line is to fill
                        // goFill(robot, fire);   
                    }
                    dateFinish = robot.getLastTimeEvent();
                    listEvents.add(new CeaseFire(sim, dateFinish, fire, robot, donnees.getOnFireCases()));
                }
                else continue;
                break;
            }
        }
        return dateFinish;
    }

    /**
     * The Robot fills his reservoir from the nearest case.
     * @param robot
     * @param fire
     */
    public void goFill(Robot robot, Fire fire) {
        Case posWater = robot.findNearestWater(donnees.getMap()); // if posWater is accessible
        PathToCase pathConstructor = new PathToCase(robot, donnees.getMap());
        pathConstructor.getEvenementsToCase(posWater, robot, listEvents, dateSimulation);
        listEvents.add(new Fill(dateSimulation, robot));
    }

    /**
     * @param robot
     * @param fire
     * @return how many intervention the robot needs to extinguish the fire.
     */
    public int countNbOfIntervention(Robot robot, Fire fire) {
        int neededWater = fire.getNeededWater();
        int interventionVol = robot.getInterventionVol();
        if (neededWater <= interventionVol) return 1;
        else return neededWater / interventionVol;
    }
}
