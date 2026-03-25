package geosearch.benchmark;

import geosearch.GeoPoint;
import geosearch.NaiveGeoIndex;
import geosearch.Quadtree;
import geosearch.RandomDataGenerator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 300, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 300, timeUnit = TimeUnit.MILLISECONDS)
@Fork(0)
public class GeoIndexBenchmark {

    @State(Scope.Thread)
    public static class BuildState {
        @Param({"1000", "10000", "100000"})
        public int size;

        public List<GeoPoint> points;

        @Setup(Level.Trial)
        public void setup() {
            points = RandomDataGenerator.generatePoints(size, 1_000L + size);
        }
    }

    @State(Scope.Benchmark)
    public static class SearchState {
        public static final int QUERY_COUNT = 200;

        @Param({"1000", "10000", "100000"})
        public int size;

        @Param({"0.1", "1.0", "10.0"})
        public double radius;

        public Quadtree quadtree;
        public NaiveGeoIndex naive;
        public List<GeoPoint> queryCenters;

        @Setup(Level.Trial)
        public void setup() {
            List<GeoPoint> points = RandomDataGenerator.generatePoints(size, 2_000L + size);
            queryCenters = RandomDataGenerator.generatePoints(QUERY_COUNT, 3_000L + size);

            quadtree = new Quadtree();
            naive = new NaiveGeoIndex();

            for (GeoPoint point : points) {
                quadtree.insert(point);
                naive.insert(point);
            }
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int buildQuadtree(BuildState state) {
        Quadtree quadtree = new Quadtree();
        for (GeoPoint point : state.points) {
            quadtree.insert(point);
        }
        return quadtree.size();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int buildNaive(BuildState state) {
        NaiveGeoIndex naive = new NaiveGeoIndex();
        for (GeoPoint point : state.points) {
            naive.insert(point);
        }
        return naive.size();
    }

    @Benchmark
    @OperationsPerInvocation(SearchState.QUERY_COUNT)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public int searchQuadtree(SearchState state) {
        int total = 0;
        for (GeoPoint query : state.queryCenters) {
            total += state.quadtree.searchInRadius(query.lat, query.lng, state.radius).size();
        }
        return total;
    }

    @Benchmark
    @OperationsPerInvocation(SearchState.QUERY_COUNT)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public int searchNaive(SearchState state) {
        int total = 0;
        for (GeoPoint query : state.queryCenters) {
            total += state.naive.searchInRadius(query.lat, query.lng, state.radius).size();
        }
        return total;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            org.openjdk.jmh.Main.main(new String[]{"GeoIndexBenchmark"});
            return;
        }
        org.openjdk.jmh.Main.main(args);
    }
}
