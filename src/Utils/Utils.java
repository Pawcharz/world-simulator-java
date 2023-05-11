package Utils;

import java.awt.geom.Point2D;

public class Utils {

    public static int RandomInteger(int min, int max) {
        int dist = (int) (max - min);

        return (int)((Math.random() * dist) + min);
    }
    public static Point2D RandomPoint2D(Point2D min, Point2D max) {
        int x = RandomInteger((int) min.getX(), (int) max.getX());
        int y = RandomInteger((int) min.getY(), (int) max.getY());

        return new Point2D.Float(x, y);
    }

    public static boolean ArePointsInDistance(Point2D posA, Point2D posB, int distance) {
        int offsetX = (int) Math.abs(posA.getX() - posB.getX());
        int offsetY = (int) Math.abs(posA.getY() - posB.getY());

        if(offsetX <= distance && offsetY == 0) {
            return true;
        }

        if(offsetX == 0 && offsetY <= distance) {
            return true;
        }

        return false;
    }

    public static String GetPath(String path) {
        String absolutePath = System.getProperty("user.dir");
        return absolutePath + "/src" + path;
    }
}
