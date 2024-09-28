package simulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

public class PathToCase {

    protected java.util.Map<Case, Long> time;
    protected Robot robot;
    protected Set<Case> Q;
    protected java.util.Map<Case, Case> predecessors;
    protected Map map;


    public PathToCase(Robot robot, Map map) {
        this.time = new HashMap<Case, Long>();
        this.robot = robot;
        this.map = map;
        this.Q = new HashSet<Case>();
        this.predecessors = new HashMap<Case, Case>();
    }


    public boolean getEvenementsToCase(Case dest, Robot robot, PriorityQueue<Event> listEvenements, long dateSimulation) {
        LinkedList<Direction> listDirection = this.Dijkstra(this.robot.getPosition(), dest, map);
        
        boolean bool = listDirection.isEmpty();
        if (bool) return ! bool;  // listDirection is empty, there's no path to dest -- Returning false

        Iterator<Direction> it = listDirection.descendingIterator(); // Iterating the other way because dijkstra retrieves dest to src path. 
                                                                     // But we need src to path..
        while (it.hasNext()) {
            Direction polled = it.next();
            listEvenements.add(new Move(dateSimulation, robot, polled, map));   // Adding necessary moves to listEvenements
        }
        listEvenements.add(new SwitchAvailability(robot, dateSimulation)); // When robot arrives in Case he's free
        return ! bool;
    }
    

    public LinkedList<Direction> Dijkstra(Case src, Case dest, Map map) {

        String robotName = robot.getClass().getSimpleName();
        for (int i = 0; i < map.getNbLine(); i++) {     // Initialization of Graph
            for (int j = 0; j < map.getNbCol(); j++) {

                Case pos = map.getCase(i, j);
                LandNature nature = pos.getNature(); // Having nature in order to store only the cases that are accessible by each robot
                switch (robotName) {
                    case "Drone" :
                        addVertexToConfiguration(src, pos); // Adding weights. See fct addVertexToConfiguration
                        break;
                    case "WheelRobot" :
                        if (nature == LandNature.FREE_LAND || nature == LandNature.HABITAT) {
                            addVertexToConfiguration(src, pos);
                        }
                        break;
                    case "TankRobot" :
                        if (nature != LandNature.WATER && nature != LandNature.ROCK) {
                            addVertexToConfiguration(src, pos);
                        }
                        break;
                    case "FootRobot" :
                        if (nature != LandNature.WATER) {
                            addVertexToConfiguration(src, pos);
                        }
                        break;
                    default :
                        throw new IllegalArgumentException();
                }
            }
        }


        while ( ! this.Q.isEmpty()) {
            Case minDistVertex = this.getMinVertex();

            if (minDistVertex == dest) break;   // If vertex is our destination, we might break the loop to avoid calculating shortest paths for all
                                                // the vertices of the graph

            this.Q.remove(minDistVertex);   // When treated removing it from Q in order to avoid doing it twice


            HashMap<Case, Direction> list = this.getNeighborsNotInSet(minDistVertex, Q); // List of neighbors vertices of minDistVertex and contained in Q
            for (Case pos : list.keySet()) {    // This is Djikstra's algorithm. Check https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
                long length = this.time.get(minDistVertex) + this.robot.timeToCase(minDistVertex, list.get(pos), this.map);
                
                if (length < this.time.get(pos)) {
                    this.time.replace(pos, length);
                    predecessors.replace(pos, minDistVertex);
                }
            }
        }

        
        LinkedList<Direction> shortestPath = new LinkedList<Direction>();   // Retrieving the linkedlist of Directions that robot should take
        if (predecessors.get(dest) != null || src == dest) {
            Case target = dest;
            Case suivOfTarget = dest;
            target = predecessors.get(target);
            while (target != null) {
                shortestPath.add(getDirFromCase(target, suivOfTarget));
                suivOfTarget = target;
                target = predecessors.get(target);
            }
        }
        

        return shortestPath;
    }




    public Case getMinVertex() {    // Iterate over Q's elements and retrieves the Case with the least meaningful weight 
        Iterator<Case> it = this.Q.iterator();
        Case min = it.next();
        while (it.hasNext()) {
            Case current = it.next();
            if (this.time.get(current) < this.time.get(min)) min = current;
        }
        return min;
    }





    public HashMap<Case, Direction> getNeighborsNotInSet(Case pos, Set<Case> Q) { // Get neighbors of @param pos that are not in the set of the beginning but are in Q
        HashMap<Case, Direction> list = new HashMap<Case, Direction>();

        Case northNeighbor =  this.map.getNeighbor(pos, Direction.NORTH); // Loading neighbors
        Case southNeighbor =  this.map.getNeighbor(pos, Direction.SOUTH);
        Case eastNeighbor =  this.map.getNeighbor(pos, Direction.EAST);
        Case westNeighbor =  this.map.getNeighbor(pos, Direction.WEST);

        Iterator<Case> it = this.Q.iterator();
        Case current;
        while (it.hasNext()) { // Searching for the src case in Q's set
            current = it.next();

            if (current == northNeighbor) {
                list.put(current, Direction.NORTH);
                continue;
            }
            if (current == southNeighbor) {
                list.put(current, Direction.SOUTH);
                continue;
            }
            if (current == eastNeighbor) {
                list.put(current, Direction.EAST);
                continue;
            }
            if (current == westNeighbor) {
                list.put(current, Direction.WEST);
                continue;
            }
        }
        
        return list;
    }

    public void addVertexToConfiguration(Case src, Case pos) { // Having three collections as configuration. Hashmap : time and predecessors because we
                                                               // need fast access to its elements. Hashset : Q to store without doubling all the vertices
                                                               // While looping over the vertices
        if (pos != src) this.time.put(pos, Long.MAX_VALUE);
        else this.time.put(pos,(long) 0);   // if vertex is src its weight might be 0
        this.predecessors.put(pos, null);
        this.Q.add(pos);
    }


    public Direction getDirFromCase(Case src, Case dest) { // Giving two neighbor cases and retrieves which direction to take from src in order to arrive to dest
        // Assume that src and pos are neighbors
        if (src.getLine() - dest.getLine() == 1) return Direction.NORTH;
        else if (src.getLine() - dest.getLine() == -1) return Direction.SOUTH;
        else {
            if (src.getCol() - dest.getCol() == 1) return Direction.WEST;
            else return Direction.EAST;
        }

    }
}
