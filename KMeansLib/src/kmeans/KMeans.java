package kmeans;

import java.util.*;
import java.util.concurrent.*;


/**
 * implements algorithm and contains iterationIndex results
 * Created by zasam on 03.02.2017.
 */
public class KMeans implements IterativePatternRecognition {

    protected final ArrayList<Cluster> clusters;
    protected final Point[] points;
    protected boolean haveNextIteration;
    protected ExecutorService service;
    protected int iterationIndex =0;
    protected PointRouter router;

    @Override
    public int getIterationIndex() {
        return iterationIndex;
    }
/*
    protected Cluster getClusterForPoint(Point point) {
        double minDistance = Double.MAX_EXPONENT;
        Cluster minCluster = new Cluster();
        for (Cluster cluster : clusters) {
            double distance = cluster.getDistanceToTheCore(point);
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = cluster;
            }
        }
        return minCluster;
    }



    protected void movePointsToClusters() throws InterruptedException {
        List<Callable<Boolean>> taskList
                = new ArrayList<>(points.length);
        for (Point point : points) {
            taskList.add(() -> getClusterForPoint(point).add(point));
        }
        service.invokeAll(taskList, 20, TimeUnit.SECONDS);
    }
*/
    protected void clearClusters(){
    clusters.forEach(Cluster::clear);
}

    protected void moveCores() throws InterruptedException {
        List<Callable<Boolean>> arrayList = new ArrayList<>(clusters.size());
        clusters.forEach((cluster)->arrayList.add(cluster::getNewCores));
        service.invokeAll(arrayList,20,TimeUnit.SECONDS);
    }

    protected void calculateIterationResult() {
        haveNextIteration = false;
        clearClusters();
        try {
            router.movePointsToClusters(points,clusters);
            moveCores();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Cluster> call() throws Exception {
        calculateIterationResult();
        System.out.println(iterationIndex++);
        return clusters;
    }

    public boolean isHaveNextIteration() {
        if (!haveNextIteration) {
            for (Cluster cluster : clusters) {
                haveNextIteration =
                        cluster.isCoreChanged() || haveNextIteration;
            }
        }
        return haveNextIteration;
    }

    @Override
    public void shutdown() throws InterruptedException {
        service.shutdownNow();
        service.awaitTermination(1,TimeUnit.NANOSECONDS);
    }



    public KMeans(Point[] points, Point[] cores) {
        haveNextIteration = true;
        this.points = points;
        service = Executors.newCachedThreadPool();
        router = new ConcurrentPointRouter(service);
        clusters = new ArrayList<>();
        for (Point core : cores) {
            clusters.add(new Cluster(core,
                    (int) (1.5 * points.length / cores.length)));
        }
    }
}
