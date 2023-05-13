package World;

import Organisms.Organism;
import Utils.ORGANISM_TYPE;
import Utils.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Displayer {

    private JFrame window;
    ArrayList<JPanel> cells;
    DefaultListModel<String> logsListModel;

    private Point2D windowSize;

    private int cellSize;

    private Point2D windowPadding;
    private Point2D logListSize;

    private Point2D boardSize;

    private int windowElementsGap;

    private ArrayList<String> logs;


    Displayer() {
        windowSize = new Point2D.Double(1400, 1000);
        windowPadding = new Point2D.Double(50, 50);
        windowElementsGap = 50;

        logListSize = new Point2D.Double(400, 500);

        int boardRangeX = (int) Math.abs(windowSize.getX() - logListSize.getX() - 2 * windowPadding.getX() - windowElementsGap);
        int boardRangeY = (int) Math.abs(windowSize.getY() - 2 * windowPadding.getY());

        World world = World.GetInstance();
        Point2D worldSize = world.GetInstance().GetDimensions();

        // We choose higher pixels per cell ratio
        cellSize = Math.floorDiv(boardRangeY, (int)worldSize.getY());
        if(boardRangeY / worldSize.getY() >= boardRangeX / worldSize.getX()) {
            cellSize = Math.floorDiv(boardRangeX, (int)worldSize.getX());
        }
        boardSize = new Point2D.Double(cellSize * worldSize.getX(), cellSize * worldSize.getY());

        window = new JFrame("Paweł Blicharz | s193193 | World Simulation");
        window.setSize((int) windowSize.getX(), (int) windowSize.getY());
        window.setLayout(null);
        DrawBoard();

        Controller controller = world.GetController();
        window.addKeyListener(controller);

        window.setVisible(true);

        logs = new ArrayList<String>();

        logsListModel = new DefaultListModel<String>();

        JList logsList = new JList<String>(logsListModel);
        logsList.setLayout(null);
        logsList.setSize((int) logListSize.getX(), (int) logListSize.getY());
        logsList.setLocation((int) windowPadding.getX(), (int) windowPadding.getY());

        window.add(logsList);
    }


    private void DrawBoard() {
        cells = new ArrayList<>();

        Point2D worldSize = World.GetInstance().GetDimensions();

        for (int x = 0; x < worldSize.getX(); x++) {
            for (int y = (int) (worldSize.getY() - 1); y >= 0; y--) {

                int posX = (int) (windowSize.getX() - boardSize.getX() - windowPadding.getX()) + x * cellSize;
                int posY = (int) (windowPadding.getY()) + y * cellSize;

                int finalX = x;
                int finalY = y;
                JPanel cell = new JPanel() {
                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawRect(0, 0, cellSize - 1, cellSize - 1);

                        // FIX - images disappear after resizing
                    }

                };
                cell.setLocation(posX, posY);
                cell.setSize(cellSize, cellSize);
                cell.setLayout(null);

                cells.add(cell);
                window.add(cell);
            }
        }
    }

    private void UpdateCell(int x, int y) {
        World world = World.GetInstance();

        int cellIndex = x * (int)world.GetDimensions().getX() + y;

        Organism atPosition = world.GetOrganismAtPosition(new Point2D.Double(x, y));

        JPanel cell = cells.get(cellIndex);
        Graphics cellGraphisc = cell.getGraphics();
        cellGraphisc.clearRect(1, 1, cellSize-2, cellSize-2);
        if(atPosition != null) {
            String iconPath = GetImagePathByOrganismType(atPosition.GetType());
            File file = new File(iconPath);
            try
            {
                BufferedImage image = ImageIO.read(file);

                cellGraphisc.drawImage(image, 1, 1, cellSize-2, cellSize-2, null);
            }
            catch (IOException e)
            {
                System.out.print("Error -> IOException");
            }
        }
    }

    private void UpdateBoard() {
        World world = World.GetInstance();
        int sizeX = (int)world.GetDimensions().getX();
        int sizeY = (int)world.GetDimensions().getY();

        for (int x = 0; x < sizeX; x++) {
            for (int y = sizeY - 1; y >= 0; y--) {
                UpdateCell(x, y);
            }
        }
    }

    private void UpdateLogsDisplay() {
        logsListModel.clear();
        for (int i = 0; i < logs.size(); i++) {
            logsListModel.addElement(logs.get(i));
        }
    }
    private String GetImagePathByOrganismType(ORGANISM_TYPE organismType) {
        String publicPart = "/public";
        String suffix = null;
        switch (organismType) {
            case SHEEP: suffix = "/sheep.png"; break;
            case WOLF: suffix = "/wolf.jpg"; break;
            case FOX: suffix = "/fox.png"; break;
            case TURTLE: suffix = "/turtle.png"; break;
            case ANTILOPE: suffix = "/antelope.png"; break;

            case GRASS: suffix = "/grass.png"; break;
            case SOW_THISTLE: suffix = "/sow_thistle.jpg"; break;
            case GUARANA: suffix = "/guarana.png"; break;
            case BELLADONNA: suffix = "/belladonna.png"; break;
            case SOSNOWSKYS_HOGWEED: suffix = "/sosnowskys_hogweed.jpg"; break;

            case HUMAN: suffix = "/human.png"; break;
        }

        if(suffix == null) {
            return null;
        }

        return Utils.GetPath(publicPart + suffix);
    }
    public void UpdateInterface() {
        UpdateBoard();
        UpdateLogsDisplay();
    }

    public void AddLog(String message) {
        logs.add(message);
    }

    public void ResetLogs() {
        logs.clear();
    }
}
