package World;

import Organisms.Animals.Human;
import Organisms.Organism;
import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static Utils.Utils.ArePointsInDistance;
import static Utils.Utils.RandomPoint2D;
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
        dimentions = new Point2D.Double(0, 0);

        organisms = new ArrayList<Organism>();
        player = null;
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

    private void CreatePlayer() {
        Point2D position = RandomPoint2D(new Point2D.Double(0, 0), dimentions);

        // FIX - will crash for more entities on the map
        Organism existing = GetOrganismAtPosition(position);
        while (existing != null)
        {
            position = RandomPoint2D(new Point2D.Double(0, 0), dimentions);
            existing = GetOrganismAtPosition(position);
        }

        player = new Human(position);
        organisms.add(player);
    }


    private void SortOrganisms() {
        ArrayList<Organism> sorted = new ArrayList<Organism>();

        for (int i = 0; i < organisms.size(); i++) {
            Organism best = getQuickestOrganism(organisms, sorted);

            sorted.add(best);
        }

        organisms = sorted;
    }

    private Organism getQuickestOrganism(ArrayList<Organism> all, ArrayList<Organism> excluded) {
        int length = all.size();

        Organism best = null;

        for (int i = 0; i < length; i++)
        {
            Organism newOrganism = all.get(i);

            // CHECK - not sure about filter
            boolean includes = excluded.stream().filter(organism -> {
                return organism == newOrganism;
            }).count() != 0;

            if (!includes) {
                if (best == null) {
                    best = newOrganism;
                }
                else {
                    int currentInitiative = best.GetInitiative();
                    int newInitiative = newOrganism.GetInitiative();

                    if (newInitiative > currentInitiative) {
                        best = newOrganism;
                    }
                    else if (newInitiative == currentInitiative && newOrganism.GetAge() > best.GetAge()) {

                        best = newOrganism;
                    }
                }
            }
        }

        return best;
    }

    private void MakeTurn() {
        SortOrganisms();
        int initialOrganismsCount = organisms.size();

        for (int i = 0; i < initialOrganismsCount; i++)
        {
            Organism current = organisms.get(i);

            current.Action();
        }

        CleanDeadOrganisms();
    }

    private void CleanDeadOrganisms() {
        for (int i = organisms.size() - 1; i >= 0; i--)
        {
            Organism current = organisms.get(i);

            if(!current.IsAlive()) {
                organisms.remove(i);

                if(current.GetType() == ORGANISM_TYPE.HUMAN) {
                    player = null;
                }
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

    public void Initialize(Point2D _dimentions) {

        dimentions = _dimentions;

        int WOLFS_COUNT = 4;
        int SHEEPS_COUNT = 5;
        int FOXES_COUNT = 3;
        int TURTLES_COUNT = 3;
        int ANTELOPES_COUNT = 3;


        int GRASS_COUNT = 3;
        int SOW_THISTLE_COUNT = 1;
        int GUARANA_COUNT = 2;
        int BELLADONNA_COUNT = 3;
        int SOSNOWSKYS_HOGWEED_COUNT = 2;

        CreatePlayer();


//        CreateSpecies(1, ORGANISM_TYPE.SOSNOWSKYS_HOGWEED);
        CreateSpecies(2, ORGANISM_TYPE.SHEEP);

        CreateSpecies(WOLFS_COUNT, ORGANISM_TYPE.WOLF);
        CreateSpecies(SHEEPS_COUNT, ORGANISM_TYPE.SHEEP);
        CreateSpecies(FOXES_COUNT, ORGANISM_TYPE.FOX);
        CreateSpecies(TURTLES_COUNT, ORGANISM_TYPE.TURTLE);
        CreateSpecies(ANTELOPES_COUNT, ORGANISM_TYPE.ANTILOPE);

        CreateSpecies(GRASS_COUNT, ORGANISM_TYPE.GRASS);
        CreateSpecies(SOW_THISTLE_COUNT, ORGANISM_TYPE.SOW_THISTLE);
        CreateSpecies(GUARANA_COUNT, ORGANISM_TYPE.GUARANA);
        CreateSpecies(BELLADONNA_COUNT, ORGANISM_TYPE.BELLADONNA);
        CreateSpecies(SOSNOWSKYS_HOGWEED_COUNT, ORGANISM_TYPE.SOSNOWSKYS_HOGWEED);

        controller = new Controller();
        displayer = new Displayer();
    }

    public void Simulate() {
        displayer.UpdateInterface();

        while (player != null) {
            MakeTurn();
            displayer.UpdateInterface();
        }

        displayer.AddLog("Human got killed - GAME OVER");

        displayer.UpdateInterface();
    }


    public ArrayList<Point2D> GetFieldsInRadius(Point2D position, int radius) {
        ArrayList<Point2D> fields = new ArrayList<Point2D>();

        for (int x = 0; x < dimentions.getX(); x++) {
            for (int y = 0; y < dimentions.getY(); y++) {
                Point2D current = new Point2D.Double(x, y);

                if(ArePointsInDistance(current, position, radius) && !current.equals(position)) {
                    fields.add(current);
                }
            }
        }

        return fields;
    }
    public ArrayList<Point2D> GetFreeNeighbouringFields(Point2D position) {
        ArrayList<Point2D> free = new ArrayList<Point2D>();
        free.add(new Point2D.Double(position.getX() + 1, position.getY()));
        free.add(new Point2D.Double(position.getX() - 1, position.getY()));
        free.add(new Point2D.Double(position.getX(), position.getY() + 1));
        free.add(new Point2D.Double(position.getX(), position.getY() - 1));

        for (int i = free.size() - 1; i >= 0; i--)
        {
            Organism current = GetOrganismAtPosition(free.get(i));

            if(current != null) {
                free.remove(i);
            }
        }

        return free;
    }


    public Point2D GetDimensions() {
        return dimentions;
    }

    public ArrayList<Organism> GetOrganisms() {
        return organisms;
    }

    public Organism GetOrganismAtPosition(Point2D position) {
        for (int i = 0; i < organisms.size(); i++)
        {
            Organism current = organisms.get(i);

            if(current.GetPosition().equals(position)) {
                return current;
            }
        }

        return null;
    }

    public Controller GetController() {
        return controller;
    }

    public Displayer GetDisplayer() {
        return displayer;
    }


    public void SetPlayer(Human newPlayer) {
        player = newPlayer; // FIX
    }

    public Human GetPlayer() {
        return player;
    }



//    void SaveToFile();

//    void LoadFromFile();
};


