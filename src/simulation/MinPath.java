package simulation;

// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.LinkedList;
// import java.util.Set;


import java.util.*;  
  

public class MinPath {

    Robot robot;
    Map map;
    LinkedList<Case> path;
    HashMap<Case, SuccessorNTime> memoHashMap; // To each case we associate the case with the shortest path to

    public MinPath(Robot robot, Map map) {
        this.robot = robot;
        this.map = map;
        this.path = new LinkedList<Case>();
        this.memoHashMap = new HashMap<Case, SuccessorNTime>();
    }


    public void getPath(Case src, Case dest) {
        Case currentCase = src;
        while (currentCase != dest) {
            path.add(currentCase);
            currentCase = memoHashMap.get(currentCase).getSuccessor();
        }
        path.add(currentCase);
    }

    public Case findShortestPath(Case src, Case dest) {
        
        if (this.memoHashMap.containsKey(src)) return this.memoHashMap.get(src).getSuccessor();

        if (! map.neighborExistence(src, Direction.NORTH) && ! map.neighborExistence(src, Direction.SOUTH) 
                && ! map.neighborExistence(src, Direction.EAST) && ! map.neighborExistence(src, Direction.WEST)) {
                    this.memoHashMap.put(src, null);
                    return null;
        }

        
        if (map.getNeighbor(src, Direction.NORTH) == dest) {
            SuccessorNTime finalSuccessor;
            finalSuccessor = new SuccessorNTime(dest, robot.timeToCase(src, Direction.NORTH, map));
            this.memoHashMap.put(src, finalSuccessor);
            return dest;
        }
        else if (map.getNeighbor(src, Direction.SOUTH) == dest) {
            SuccessorNTime finalSuccessor;
            finalSuccessor = new SuccessorNTime(dest, robot.timeToCase(src, Direction.SOUTH, map));
            this.memoHashMap.put(src, finalSuccessor);
            return dest;
        }
        else if (map.getNeighbor(src, Direction.EAST) == dest) {
            SuccessorNTime finalSuccessor;
            finalSuccessor = new SuccessorNTime(dest, robot.timeToCase(src, Direction.EAST, map));
            this.memoHashMap.put(src, finalSuccessor);
            return dest;
        }
        else if (map.getNeighbor(src, Direction.WEST) == dest) {
            SuccessorNTime finalSuccessor;
            finalSuccessor = new SuccessorNTime(dest, robot.timeToCase(src, Direction.WEST, map));
            this.memoHashMap.put(src, finalSuccessor);
            return dest;
        }


        Case northCase = findShortestPath(map.getNeighbor(src, Direction.NORTH), dest);
        Case southCase = findShortestPath(map.getNeighbor(src, Direction.SOUTH), dest);
        Case westCase = findShortestPath(map.getNeighbor(src, Direction.WEST), dest);
        Case eastCase = findShortestPath(map.getNeighbor(src, Direction.EAST), dest);

        SuccessorNTime nearestSuccessor = minSuccessors(src, northCase, southCase, westCase, eastCase);
        this.memoHashMap.put(src, nearestSuccessor);
        return nearestSuccessor.getSuccessor();
    }

    
    public SuccessorNTime minSuccessors(Case src, Case northCase, Case southCase, Case westCase, Case eastCase) {

        if (northCase != null) long northTime = robot.timeToCase(src, Direction.NORTH, map) + memoHashMap.get(northCase).getTime();
        if (northCase != null) long southTime = robot.timeToCase(src, Direction.SOUTH, map) + memoHashMap.get(southCase).getTime();
        long eastTime = robot.timeToCase(src, Direction.EAST, map) + memoHashMap.get(eastCase).getTime();
        long westTime = robot.timeToCase(src, Direction.WEST, map) + memoHashMap.get(westCase).getTime();

        Case minTimeCase = northCase;
        long minTime = northTime;
        if (southTime < minTime) {
            minTime = southTime;
            minTimeCase = southCase;
        }
        if (eastTime < minTime) {
            minTime = eastTime;
            minTimeCase = eastCase;
        }
        if (westTime < minTime) {
            minTime = westTime;
            minTimeCase = westCase;
        }
        
        SuccessorNTime successor = new SuccessorNTime(minTimeCase, minTime);
        return successor;
    }
}





class SuccessorNTime {

    Case successor;
    long time;

    public SuccessorNTime(Case successor, long time) {
        this.successor = successor;
        this.time = time;
    }

    public Case getSuccessor() {
        return successor;
    }

    public void setSuccessor(Case successor) {
        this.successor = successor;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }   
}