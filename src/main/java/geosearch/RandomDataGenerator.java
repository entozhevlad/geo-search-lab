package geosearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class RandomDataGenerator {
    private RandomDataGenerator() {
    }

    public static List<GeoPoint> generatePoints(int count, long seed) {
        Random random = new Random(seed);
        List<GeoPoint> points = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double lat = randomBetween(random, Quadtree.WORLD_MIN_LAT, Quadtree.WORLD_MAX_LAT);
            double lng = randomBetween(random, Quadtree.WORLD_MIN_LNG, Quadtree.WORLD_MAX_LNG);
            points.add(new GeoPoint(i, lat, lng));
        }
        return points;
    }

    private static double randomBetween(Random random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }
}
