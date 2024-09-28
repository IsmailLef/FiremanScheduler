package simulation;
import java.util.ArrayList;

public class Map {

    private int caseSize;    // Each case is a square of size ( caseSize x caseSize )
    private int nbLine;      // The Map's height
    private int nbCol;       // The Map's width
    private Case[][] cases;  // The Matrix 

    /* Constructor */
    public Map(int size, int nbline, int nbcol) {
        this.caseSize = size;
        this.nbLine = nbline;
        this.nbCol = nbcol;
        this.cases = new Case[nbline][nbcol];
    }

    /* Getters */
    public int getNbLine() {
        return nbLine;
    }

    public int getNbCol() {
        return nbCol;
    }

    public int getSize() {
        return caseSize;
    }
    
    public Case getCase(int line, int col) {
        return cases[line][col];
    }

    /* Setters */
    public void setCase(int line, int col, LandNature nature) {
        cases[line][col] = new Case(line, col, nature);
    }
    
    /*Useful Methods */

    /**
     * @return if it exists, the neigbor Case of the Case src in the Direction dir. 
     */
    public boolean neighborExistence(Case src, Direction dir) {
        // Check if the Case src has a neighbor in the given Direction dir. 
        int line = src.getLine();
        int col = src.getCol();
        switch (dir) {
            case NORTH:
                return line - 1 >= 0;
            case SOUTH:
                return line + 1 < nbLine;
            case EAST:
                return col + 1 <= nbCol - 1;
            case WEST:
                return col - 1 >= 0;
            default:
                return false;
        }
    }

    /**
     * @return if it exists, the neigbor Case of the Case src in the Direction dir. 
     */
    public Case getNeighbor(Case src, Direction dir) { 
        boolean verif = neighborExistence(src, dir);
        if (verif) {
            int i = src.getLine();
            int j = src.getCol();
            switch (dir) {
                case NORTH:
                    return this.getCase(i - 1, j);
                case SOUTH:
                    return this.getCase(i + 1,j);
                case EAST:
                    return this.getCase(i, j + 1);
                case WEST:
                    return this.getCase(i, j - 1);
                default:
            }
        }
        return null;
    }

    /**
     * @return an array of Water type cases that the Map contains.
     */
    public ArrayList<Case> getWaterCases() {
        ArrayList<Case> waterCases = new ArrayList<Case>();
        for( int i = 0; i < nbLine; i++){
            for( int j = 0; j < nbCol; j++){
                if ( this.getCase(i,j).getNature() == LandNature.WATER ){
                    waterCases.add(this.getCase(i,j));
                }
            }
        }
    return waterCases;
}
}
