import World.World;

import java.awt.geom.Point2D;

public class Main {
    public static void main(String[] args) {

        World world = World.GetInstance();
        world.Initialize(new Point2D.Double(6, 6));

        world.Simulate();
    }
}