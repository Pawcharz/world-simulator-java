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


    Displayer() {
        windowSize = new Point2D.Double(800, 600);
        boardMargin = new Point2D.Double(200, 0);
        boardPadding = new Point2D.Double(50, 50);

        int rangeX = (int) Math.abs(windowSize.getX() - boardMargin.getX() - 2 * boardPadding.getX());
        int rangeY = (int) Math.abs(windowSize.getY() - boardMargin.getY() - 2 * boardPadding.getY());

        Point2D worldSize = World.GetInstance().GetDimentions();

        // We choose higher pixels per cell ratio
        cellSize = Math.floorDiv(rangeY, (int)worldSize.getY());
        if(rangeY / worldSize.getY() >= rangeX / worldSize.getX()) {
            cellSize = Math.floorDiv(rangeX, (int)worldSize.getX());
        }

        window = new JFrame("Paweł Blicharz | s193193 | World Simulation");
        window.setSize((int) windowSize.getX(), (int) windowSize.getY());
        window.setLayout(null);
        DrawBoard();

        window.setVisible(true);
    }


    private void DrawBoard() {
        cells = new ArrayList<>();

        Point2D worldSize = World.GetInstance().GetDimentions();

        for (int x = 0; x < worldSize.getX(); x++) {
            for (int y = 0; y < worldSize.getY(); y++) {
                int posX = (int) (boardMargin.getX() + boardPadding.getX()) + x * cellSize;
                int posY = (int) (boardMargin.getY() + boardPadding.getY()) + y * cellSize;

                JPanel cell = new JPanel() {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
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

        if(atPosition != null) {
            String iconPath = GetImagePathByOrganismType(atPosition.GetType());
            JPanel cell = cells.get(cellIndex);
            File file = new File(iconPath);
            try
            {
                BufferedImage image = ImageIO.read(file);

                cell.getGraphics().drawImage(image, 0, 0, cellSize, cellSize, null);
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
            for (int y = 0; y < sizeY; y++) {
                UpdateCell(x, y);
            }
        }
    }
    private String GetImagePathByOrganismType(ORGANISM_TYPE organismType) {
        String publicPart = "/public";
        String suffix = null;
        switch (organismType) {
            case SHEEP: suffix = "/sheep.jpg"; break;
            case WOLF: suffix = "/wolf.jpg"; break;
            case FOX: suffix = "/fox.jpg"; break;
            case TURTLE: suffix = "/turtle.jpg"; break;
            case ANTILOPE: suffix = "/antilope.jpg"; break;

            case GRASS: suffix = "/grass.jpg"; break;
            case SOW_THISTLE: suffix = "/sow_thistle.jpg"; break;
            case GUARANA: suffix = "/guarana.jpg"; break;
            case BELLADONNA: suffix = "/belladonna.jpg"; break;
            case SOSNOWSKYS_HOGWEED: suffix = "/sosnowskys_hogweed.jpg"; break;
            case HUMAN: suffix = "/human.jpg"; break;
        }

        if(suffix == null) {
            return null;
        }

        return Utils.GetPath(publicPart + suffix);
    }
    public void UpdateInterface() {
        UpdateBoard();
    }
}
