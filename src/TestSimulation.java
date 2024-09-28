
import simulation.Simulation;
import io.LecteurDonnees;
import simulation.DonneesSimulation;
import simulation.Move;
import simulation.Direction;
import simulation.Map;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;


public class TestSimulation {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation donnees = LecteurDonnees.lire(args[0]); // We read the file and get all the data we need : map, robots, fires, etc.
            GUISimulator gui = new GUISimulator(1920, 1080, Color.BLACK); // We create the GUI simulator with parameters which fit well in full screen on computer
            new Simulation(gui, donnees, 1850, 950, 0, args[0]); // we create the simulation, with the best length for the user
            
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
