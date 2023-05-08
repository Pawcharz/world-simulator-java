package Utlis;

import java.awt.geom.Point2D;

public class Utlis {

    public static Point2D RandomPoint2D(Point2D min, Point2D max) {
        int distX = (int) (max.getX() - min.getX());
        int distY = (int) (max.getY() - min.getY());

        int randX = (int)((Math.random() * distX) + min.getX());
        int randY = (int)((Math.random() * distY) + min.getY());

        return new Point2D.Float(randX, randY);
    }

    public static boolean ArePointsInDistance(Point2D posA, Point2D posB, int distance) {
        int offsetX = (int) Math.abs(posA.getX() - posB.getX());
        int offsetY = (int) Math.abs(posA.getY() - posB.getY());

        if(offsetX < distance && offsetY == 0) {
            return true;
        }

        if(offsetX == 0 && offsetY < distance) {
            return true;
        }

        return false;
    }
}
