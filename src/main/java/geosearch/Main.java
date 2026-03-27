package geosearch;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Quadtree quadtree = new Quadtree();

        GeoPoint point1 = new GeoPoint(1, 55.7522, 37.6156);
        GeoPoint point2 = new GeoPoint(2, 55.7539, 37.6208);
        GeoPoint point3 = new GeoPoint(3, 55.7649, 37.5931);
        GeoPoint point4 = new GeoPoint(4, 55.7100, 37.5400);
        GeoPoint point5 = new GeoPoint(5, 59.9386, 30.3141);

        quadtree.insert(point1);
        quadtree.insert(point2);
        quadtree.insert(point3);
        quadtree.insert(point4);
        quadtree.insert(point5);

        System.out.println("Точек в дереве " + quadtree.size());
        System.out.println();

        System.out.println("Точки в радиусе");
        List<GeoPoint> radiusPoints = quadtree.searchInRadius(55.7522, 37.6156, 0.02);
        printPoints(radiusPoints);
        System.out.println();

        System.out.println("Точки в области");
        List<GeoPoint> boxPoints = quadtree.searchInBox(55.74, 37.59, 55.77, 37.63);
        printPoints(boxPoints);
    }

    private static void printPoints(List<GeoPoint> points) {
        for (GeoPoint point : points) {
            System.out.println(point.id + " " + point.lat + " " + point.lng);
        }
    }
}