package World;

import Organisms.Animals.Human;
import Organisms.Organism;
import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;
import java.io.*;
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
            // Detects mid-turn saving etc.
            if(controller.GetMode() != SIMULATION_MODE.SIMULATION_PLAYING) {
                return;
            }

            Organism current = organisms.get(i);
            if (current != null && current.IsAlive()) {
                current.Action();
            }
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


//        CreateSpecies(1, ORGANISM_TYPE.TURTLE);
        CreateSpecies(2, ORGANISM_TYPE.SHEEP);

//        CreateSpecies(WOLFS_COUNT, ORGANISM_TYPE.WOLF);
//        CreateSpecies(SHEEPS_COUNT, ORGANISM_TYPE.SHEEP);
//        CreateSpecies(FOXES_COUNT, ORGANISM_TYPE.FOX);
//        CreateSpecies(TURTLES_COUNT, ORGANISM_TYPE.TURTLE);
//        CreateSpecies(ANTELOPES_COUNT, ORGANISM_TYPE.ANTELOPE);
//
//        CreateSpecies(GRASS_COUNT, ORGANISM_TYPE.GRASS);
//        CreateSpecies(SOW_THISTLE_COUNT, ORGANISM_TYPE.SOW_THISTLE);
//        CreateSpecies(GUARANA_COUNT, ORGANISM_TYPE.GUARANA);
//        CreateSpecies(BELLADONNA_COUNT, ORGANISM_TYPE.BELLADONNA);
//        CreateSpecies(SOSNOWSKYS_HOGWEED_COUNT, ORGANISM_TYPE.SOSNOWSKYS_HOGWEED);

        controller = new Controller();
        displayer = new Displayer();
    }

    public void Work() {
        while (true) {
            SIMULATION_MODE mode = controller.GetMode();
            if (mode == SIMULATION_MODE.SIMULATION_PLAYING) {

                if (player != null) {
                    MakeTurn();
                } else {
                    displayer.AddLog("Human got killed - GAME OVER");
                    displayer.UpdateInterface();
                    return;
                }

            } else if (mode == SIMULATION_MODE.FILE_SAVING) {
                SaveToFile("test.txt");
                controller.SetMode(SIMULATION_MODE.SIMULATION_PLAYING);
            } else if (mode == SIMULATION_MODE.FILE_LOADING) {
                LoadFromFile("test.txt");
                controller.SetMode(SIMULATION_MODE.SIMULATION_PLAYING);
            } else if (mode == SIMULATION_MODE.ADDING_ORGANISM) {
                GetDisplayer().DisplayAddingMenu();
            }
        }
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
            if(!IsWithinBorders(free.get(i))) {
                free.remove(i);
            }
            else {
                Organism current = GetOrganismAtPosition(free.get(i));

                if(current != null) {
                    free.remove(i);
                }
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
        player = newPlayer;
    }

    public Human GetPlayer() {
        return player;
    }


    String GetOrganismFileSaveContent(Organism organism) {
        String content = "";
        
        content += organism.GetStrength() + " ";
        content += organism.GetInitiative() + " ";
        content += organism.GetAge() + " ";

        content += (int)organism.GetPosition().getX() + " ";
        content += (int)organism.GetPosition().getY() + " ";

        ORGANISM_TYPE type = organism.GetType();
        content += type + " "; // FIX - can I do it like this?

        if(organism.IsAlive()) {
            content += "ALIVE" + " ";
        }
        else {
            content += "DEAD" + " ";
        }


        if (type == ORGANISM_TYPE.HUMAN) {
            Human asHuman = (Human)organism;
            content += asHuman.GetStrengthBuff() + " ";
            content += asHuman.GetAbilityCooldown() + " ";
        }

        return content;
    }
    private String PrepareFileContentToSave() {
        String content = "";

        content += (int)dimentions.getX() + " ";
        content += (int)dimentions.getY() + " ";

        for (int i = 0; i < organisms.size(); i++)
        {
            Organism current = organisms.get(i);

            content += GetOrganismFileSaveContent(current);
        }

        return content;
    }
    void SaveToFile(String fileName) {

        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(fileName);
            String content = PrepareFileContentToSave();
            writer.write(content);
            writer.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    int CreateOrganismFromBuffer(String[] tokens, int i) {

        int strength = Integer.parseInt(tokens[i]);
        int initiative = Integer.parseInt(tokens[i+1]);
        int age = Integer.parseInt(tokens[i+2]);

        int posX = Integer.parseInt(tokens[i+3]);
        int posY = Integer.parseInt(tokens[i+4]);

        ORGANISM_TYPE type = ORGANISM_TYPE.valueOf(tokens[i+5]);

        boolean alive = true;
        if(tokens[i+6] == "DEAD") {
            alive = false;
        }

        if (type == ORGANISM_TYPE.HUMAN) {
            int strengthBuff = Integer.parseInt(tokens[i+7]);
            int cooldown = Integer.parseInt(tokens[i+8]);

            Human human = new Human(new Point2D.Double(posX, posY));
            human.SetAge(age);
            human.SetAlive(alive);
            human.SetStrengthBuff(strengthBuff);
            human.SetAbilityCooldown(cooldown);

            organisms.add(human);
            player = human;
            return i + 9;
        }
        else {

            Organism organism = OrganismFactory.Create(type, new Point2D.Double(posX, posY));
            organism.SetAge(age);
            organism.SetAlive(alive);
            organism.SetStrength(strength);
            organism.SetInitiative(initiative);
            organisms.add(organism);
            return i + 7;
        }
    }
    void ProcessLoadedFileContent(String content) {
        String[] tokens = content.split(" ");
        System.out.println(tokens);

        int boardSizeX = Integer.parseInt(tokens[0]);
        int boardSizeY = Integer.parseInt(tokens[1]);

        organisms.clear();
        player = null;
        dimentions = new Point2D.Double(boardSizeX, boardSizeY);

        for (int i = 2; i < tokens.length;) {
            i = CreateOrganismFromBuffer(tokens, i);
        }

    }
    void LoadFromFile(String fileName) {
        int i;
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);

            String content = "";

            while ((i = reader.read()) != -1) {
                content += (char)i;
            }

            ProcessLoadedFileContent(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        displayer.ResetLogs();
        displayer.RedrawBoard();
    }
};


