package World;

import Organisms.Organism;
import Utlis.ORGANISM_TYPE;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
                        g.drawRect(0, 0, cellSize-1, cellSize-1);
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

    private void UpdateBoard() {
        World world = World.GetInstance();
        int sizeX = (int)world.GetDimentions().getX();
        int sizeY = (int)world.GetDimentions().getY();

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                int cellIndex = x * sizeX + y * sizeY;

                Organism atPosition = world.GetOrganismAtPosition(new Point2D.Double(x, y));

                if(atPosition != null) {
                    String iconPath = GetImagePathByOrganismType(atPosition.GetType());
                    JPanel cell = cells.get(cellIndex);
                    try
                    {
                        BufferedImage image = ImageIO.read(new File(iconPath));

                        cell.getGraphics().drawImage(image, 0, 0, null);
                    }
                    catch (IOException e)
                    {
                        //Not handled.
                    }


                }

            }
        }
    }
    private String GetImagePathByOrganismType(ORGANISM_TYPE organismType) {
        switch (organismType) {
            case SHEEP: return "dsds";
            case WOLF: return "dsds";
            case FOX: return "dsds";
            case TURTLE: return "dsds";
            case ANTILOPE: return "dsds";

            case GRASS: return "dsds";
            case SOW_THISTLE: return "dsds";
            case GUARANA: return "dsds";
            case BELLADONNA: return "dsds";
            case SOSNOWSKYS_HOGWEED: return "dsds";
            case HUMAN: return "dsds";
            default: return null;
        }
    }
    public void UpdateInterface() {
        UpdateBoard();
    }
}
