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
    private Point2D windowSize;

    private int cellSize;
    private Point2D boardMargin;
    private Point2D boardPadding;

    private ArrayList<String> logs;


    Displayer() {
        windowSize = new Point2D.Double(1400, 1000);
        boardMargin = new Point2D.Double(400, 0);
        boardPadding = new Point2D.Double(100, 100);

        int rangeX = (int) Math.abs(windowSize.getX() - boardMargin.getX() - 2 * boardPadding.getX());
        int rangeY = (int) Math.abs(windowSize.getY() - boardMargin.getY() - 2 * boardPadding.getY());

        World world = World.GetInstance();
        Point2D worldSize = world.GetInstance().GetDimentions();

        // We choose higher pixels per cell ratio
        cellSize = Math.floorDiv(rangeY, (int)worldSize.getY());
        if(rangeY / worldSize.getY() >= rangeX / worldSize.getX()) {
            cellSize = Math.floorDiv(rangeX, (int)worldSize.getX());
        }

        window = new JFrame("Paweł Blicharz | s193193 | World Simulation");
        window.setSize((int) windowSize.getX(), (int) windowSize.getY());
        window.setLayout(null);
        DrawBoard();

        Controller controller = world.GetController();
        window.addKeyListener(controller);

        window.setVisible(true);

        logs = new ArrayList<String>();
    }


    private void DrawBoard() {
        cells = new ArrayList<>();

        Point2D worldSize = World.GetInstance().GetDimentions();

        for (int x = 0; x < worldSize.getX(); x++) {
            for (int y = (int) (worldSize.getY() - 1); y >= 0; y--) {
                int posX = (int) (boardMargin.getX() + boardPadding.getX()) + x * cellSize;
                int posY = (int) (boardMargin.getY() + boardPadding.getY()) + y * cellSize;

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

        int cellIndex = x * (int)world.GetDimentions().getX() + y;

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
        int sizeX = (int)world.GetDimentions().getX();
        int sizeY = (int)world.GetDimentions().getY();

        for (int x = 0; x < sizeX; x++) {
            for (int y = sizeY - 1; y >= 0; y--) {
                UpdateCell(x, y);
            }
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
    }

    public void AddLog(String s) {
    }

    public void ResetLogs() {
        logs.clear();
    }
}
