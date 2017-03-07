package kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zasam on 20.02.2017.
 */
public class ConcurrentPointRouter extends PointRouter implements Shutdownable {

    private ExecutorService service;

    public ConcurrentPointRouter(ExecutorService service){
        super();
        this.service = service;
    }

    public ConcurrentPointRouter() {
        this(Executors.newCachedThreadPool());
    }

    @Override
    public void shutdown() {
        service.shutdown();
    }

    protected Callable<Boolean> getCallableClusterForPoint(Point point, List<Cluster> clusters) {
        return ()->super.getClusterForPoint(point, clusters).add(point);
    }

    @Override
    public void movePointsToClusters(Point[] points, List<Cluster> clusters) throws InterruptedException {
        invokeCallables(getCallableList(points,clusters));
    }

    private List<Callable<Boolean>> getCallableList(Point[] points,List<Cluster> clusters){
        List<Callable<Boolean>> callables = new ArrayList<>(points.length);
        for (Point point: points){
            callables.add(getCallableClusterForPoint(point,clusters));
        }
        return callables;
    }

    private <T> void invokeCallables(List<Callable<T>> callables) throws InterruptedException {
        service.invokeAll(callables, 20, TimeUnit.SECONDS);
    }

}
