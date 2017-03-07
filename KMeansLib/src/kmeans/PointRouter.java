package kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zasam on 20.02.2017.
 */
public class PointRouter implements Shutdownable{

    public PointRouter() {

    }


    protected Cluster getClusterForPoint(Point point,List<Cluster> clusters) {
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

    public void movePointsToClusters(Point[] points,List<Cluster> clusters) throws InterruptedException {
        List<Callable<Boolean>> taskList
                = new ArrayList<>(points.length);
        for (Point point : points) {
            getClusterForPoint(point,clusters).add(point);
        }
    }

    @Override
    public void shutdown() {

    }
}
