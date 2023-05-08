package World;

import Organisms.Animals.Human;
import Organisms.Organism;
import Utlis.ORGANISM_TYPE;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static Utlis.Utlis.ArePointsInDistance;
import static Utlis.Utlis.RandomPoint2D;
import Organisms.OrganismFactory;

public class World
{
    private static World worldInstance;

    private Controller controller;
    private Displayer displayer;

    private Point2D dimentions;

    private ArrayList<Organism> organisms;

    private Human player;

    private World() {
        organisms = new ArrayList<Organism>();
        player = null;

        controller = null;
        displayer = null;
    }

    private void SetSize(Point2D dimentions) {

    }

    private void CreateOrganism(ORGANISM_TYPE organismType) {

        Point2D position = RandomPoint2D(new Point2D.Double(0, 0), dimentions);

        // FIX - will crash for more entities on the map
        Organism existing = GetOrganismAtPosition(position);
        while (existing != null)
        {
            position = RandomPoint2D(new Point2D.Double(0, 0), dimentions);
            existing = GetOrganismAtPosition(position);
        }

        Organism organism = OrganismFactory.Create(organismType, position);
        organisms.add(organism);
    }

    private void CreateSpecies(int entities, ORGANISM_TYPE organismType) {
        for (int i = 0; i < entities; i++)
        {
            CreateOrganism(organismType);
        }
    }

    // private void CreateHuman();


    private void SortOrganisms() {

    }

    private void MakeTurn() {
        for (int i = organisms.size() - 1; i >= 0; i--)
        {
            Organism current = organisms.get(i);

            current.Action();
        }
    }

    private void CleanDeadOrganisms() {
        for (int i = organisms.size() - 1; i >= 0; i--)
        {
            Organism current = organisms.get(i);

            if(!current.IsAlive()) {
                organisms.remove(i);
            }
        }
    }

    public boolean IsWithinBorders(Point2D position) {
        if(position.getX() >= dimentions.getX() || position.getX() < 0) {
            return false;
        }

        if(position.getY() >= dimentions.getY() || position.getY() < 0) {
            return false;
        }
        return true;
    }

    public static World GetInstance() {
        if(worldInstance == null) {
            worldInstance = new World();
        }

        return worldInstance;
    }

    public void Initialize(Point2D dimentions) {

    }

    public void Simulate() {
        System.out.println("Simulating world!");
    }


    ArrayList<Point2D> GetFieldsInRadius(Point2D position, int radius) {
        ArrayList<Point2D> fields = new ArrayList<Point2D>();

        for (int i = 0; i < organisms.size(); i++)
        {
            Point2D current = organisms.get(i).GetPosition();

            if(ArePointsInDistance(current, position, radius)) {
                fields.add(current);
            }
        }

        return fields;
    }
    ArrayList<Point2D> GetFreeNeighbouringFields(Point2D position) {
        ArrayList<Point2D> free = new ArrayList<Point2D>();
        free.add(new Point2D.Double(position.getX() + 1, position.getY()));
        free.add(new Point2D.Double(position.getX() - 1, position.getY()));
        free.add(new Point2D.Double(position.getX(), position.getY() + 1));
        free.add(new Point2D.Double(position.getX(), position.getY() - 1));

        for (int i = free.size() - 1; i >= 0; i--)
        {
            Organism current = GetOrganismAtPosition(free.get(i));

            if(current == null) {
                free.remove(i);
            }
        }

        return free;
    }


    Point2D GetDimentions() {
        return dimentions;
    }

    ArrayList<Organism> GetOrganisms() {
        return organisms;
    }

    Organism GetOrganismAtPosition(Point2D position) {
        for (int i = 0; i < organisms.size(); i++)
        {
            Organism current = organisms.get(i);

            if(current.GetPosition() == position) {
                return current;
            }
        }

        return null;
    }

    Controller GetController() {
        return controller;
    }

    Displayer GetDisplayer() {
        return displayer;
    }


    void SetPlayer(Human newPlayer) {
        player = newPlayer; // FIX
    }

    Human GetPlayer() {
        return player;
    }


//    void SaveToFile();

//    void LoadFromFile();
};


