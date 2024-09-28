package simulation;

import io.LecteurDonnees;
import gui.GUISimulator;
import gui.GraphicalElement;
import gui.ImageElement;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.sql.Time;

public class Simulation implements Simulable {
    
    private GUISimulator gui;                
    protected DonneesSimulation donnees;   
    protected int lengthScreenX;
    protected int lengthScreenY;
    protected int lengthOblongSimulatorX;
    protected int lengthOblongSimulatorY;
    protected long dateSimulation; 
    protected PriorityQueue<Event> listEvents;
    protected SillyChief chief;
    protected String fileName;

    /* Constuctor */
    public Simulation(GUISimulator gui, DonneesSimulation donnees, int lengthscreenx, int lengthscreeny, long dateSimulation, String filename) {
        this.gui = gui;
        this.gui.setSimulable(this);
        this.donnees = donnees;
        this.lengthScreenX = lengthscreenx;
        this.lengthScreenY = lengthscreeny;
        this.lengthOblongSimulatorY = lengthScreenY / donnees.getMap().getNbLine();
        this.lengthOblongSimulatorX = lengthScreenX / donnees.getMap().getNbCol();
        this.dateSimulation = dateSimulation;
        this.fileName = filename;
        this.listEvents = new PriorityQueue<Event>();
        this.chief = new SillyChief(donnees, listEvents, dateSimulation, this);
        drawMap();
        drawFire();
        drawRobots();
        chief.extinguishAllFire();
    }

    /* The next method */
    public void next() {
        int oblongX = this.getLengthOblongSimulatorX();
        int oblongY = this.getLengthOblongSimulatorY();
        Event evenement = this.listEvents.peek();
        Iterator<Event> it = listEvents.iterator();
        while (! this.simulationTerminee() &&evenement.getDate() <= this.dateSimulation) { //we look if there is still an event at the current date
            evenement = this.listEvents.poll(); //we remove the event from the list
            evenement.execute(); //we execute the event
            if (evenement.getClass() == Move.class) {
                Move move = (Move) evenement;
                Robot robot = move.getRobot();
                Direction dir = move.getDir();
                translateRobot(robot, dir, oblongX, oblongY);
                evenement = this.listEvents.peek();
            }
        }
        this.incrementeDate();
        chief.updateDateFromSimulation(dateSimulation);
        return ;
    }

    /* The restart method */
    public void restart() {
        try {
            this.dateSimulation = 0;
            this.donnees = LecteurDonnees.lire(this.fileName);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + this.fileName + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + this.fileName + " invalide: " + e.getMessage());
        }
        this.listEvents.clear();
        gui.reset();
        drawMap();
        drawFire();
        drawRobots();
    }


    /**
     * It translates the robot in the given dir horizontally by translatedx and vertically by translatedy.
     * @param robot
     * @param dir
     * @param translatedx
     * @param translatedy
     */
    public void translateRobot(Robot robot, Direction dir,  int translatedx, int translatedy) { 
        switch (dir) {
            case NORTH:
                robot.getImage().translate(0, -translatedy);
                break;
            case SOUTH:
                robot.getImage().translate(0, translatedy);
                break;
            case EAST:
                robot.getImage().translate(translatedx, 0);
                break;
            case WEST:
                robot.getImage().translate(-translatedx, 0);
                break;
        }
    }

    /**
     * Draws the Map
     */
    public void drawMap() {
        Map map = donnees.getMap(); // target is to construct the differents cases
        int nbLine = map.getNbLine();
        int nbCol = map.getNbCol(); 
        int oblongX = getLengthOblongSimulatorX();
        int oblongY = getLengthOblongSimulatorY();
        for (int i = 0; i < nbLine; i++ ) {
            for (int j = 0; j < nbCol; j++) {
                Case caseij = map.getCase(i, j);
                int x = j * oblongX;
                int y = i * oblongY;
                switch (caseij.getNature()) { //we draw the case according to its nature
                    case WATER:
                        gui.addGraphicalElement(new ImageElement(x, y, "mapimage/watertexture.gif", oblongX, oblongY, null));
                        break;
                    case FOREST:
                        gui.addGraphicalElement(new ImageElement(x, y, "mapimage/forest.jpg", oblongX, oblongY, null));
                        break;
                    case ROCK:
                        gui.addGraphicalElement(new ImageElement(x, y, "mapimage/rock.gif", oblongX, oblongY, null));
                        break;
                    case FREE_LAND:
                        gui.addGraphicalElement(new ImageElement(x, y, "mapimage/grassthumb.png", oblongX, oblongY, null));
                        break;
                    case HABITAT:
                        gui.addGraphicalElement(new ImageElement(x, y, "mapimage/house.gif", oblongX, oblongY, null));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Draws the Fires
     */
    public void drawFire() {
        Set<Fire> fires = donnees.getOnFireCases();
        int oblongX = getLengthOblongSimulatorX();
        int oblongY = getLengthOblongSimulatorY();
        for (Fire fire : fires) {
            int x = fire.getPosition().getCol() * oblongX;
            int y = fire.getPosition().getLine() * oblongY;
            gui.addGraphicalElement(new ImageElement(x, y, "mapimage/lava.gif", oblongX, oblongY, null));
        }
    }

    /**
     * Draws the Robots
     */
    public void drawRobots() {
        Robot[] robots = donnees.getRobots();
        int oblongX = getLengthOblongSimulatorX();
        int oblongY = getLengthOblongSimulatorY();
        for (int i = 0; i < robots.length; i++) {
            int x = robots[i].getPosition().getCol() * oblongX + oblongX / 2;
            int y = robots[i].getPosition().getLine() * oblongY + oblongY / 2;
            ImageElement image;
            switch(robots[i].getClass().getSimpleName()) { //we draw the robot according to its type
                case "Drone":
                    image = new ImageElement(x - oblongX / 6, y - oblongY / 6, "robotimage/Drone.jpg", oblongX / 3, oblongY / 3, null);
                    robots[i].setImage(image);
                    gui.addGraphicalElement(robots[i].getImage());
                    break;
                case "TankRobot":
                    image = new ImageElement(x + oblongX / 6, y - oblongY / 6, "robotimage/Tank.jpg", oblongX / 3, oblongY / 3, null);
                    robots[i].setImage(image);
                    gui.addGraphicalElement(robots[i].getImage());
                    break;
                case "WheelRobot":
                    image = new ImageElement(x - oblongX / 6, y + oblongY / 6, "robotimage/Wheel.png", oblongX / 3, oblongY / 3, null);
                    robots[i].setImage(image);
                    gui.addGraphicalElement(robots[i].getImage());
                    break;
                case "FootRobot":
                    image = new ImageElement(x + oblongX / 6, y + oblongY / 6, "robotimage/Foot.jpg", oblongX / 3, oblongY / 3, null);
                    robots[i].setImage(image);
                    gui.addGraphicalElement(robots[i].getImage());
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * Add the event e in the listEvents
     * @param e
     */
    public void addEvenement(Event e) {
        listEvents.add(e);
    }

    /**
     *  Increments the simulation date
     */
    public void incrementeDate() {
        this.dateSimulation += 1;
    }

    /**
     * @return True if there is no event to executed in the listEvents
     */
    public boolean simulationTerminee() {
        if (listEvents.peek() == null) return true;
        return false;
    }
    /* Getters */
    public long getDateSimulation() {
        return this.dateSimulation;
    }

    public GUISimulator getGui(GUISimulator gui) {
        return this.gui;
    }

    public DonneesSimulation getDonnees() {
        return donnees;
    }


    public int getLengthScreenX() {
        return lengthScreenX;
    }


    public int getLengthScreenY() {
        return lengthScreenY;
    }


    public int getLengthOblongSimulatorX() {
        return lengthOblongSimulatorX;
    }


    public int getLengthOblongSimulatorY() {
        return lengthOblongSimulatorY;
    }

    /* Setters */
    public void setGui(GUISimulator gui) {
        this.gui = gui;
    }


    public void setDonnees(DonneesSimulation donnees) {
        this.donnees = donnees;
    }


    public void setLengthScreenX(int lengthScreenX) {
        this.lengthScreenX = lengthScreenX;
    }


    public void setLengthScreenY(int lengthScreenY) {
        this.lengthScreenY = lengthScreenY;
    }


    public void setLengthOblongSimulatorX(int lengthOblongSimulatorX) {
        this.lengthOblongSimulatorX = lengthOblongSimulatorX;
    }


    public void setLengthOblongSimulatorY(int lengthOblongSimulatorY) {
        this.lengthOblongSimulatorY = lengthOblongSimulatorY;
    }

    
}
