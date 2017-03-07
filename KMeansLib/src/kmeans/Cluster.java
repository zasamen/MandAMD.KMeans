package kmeans;

import java.util.*;

import static kmeans.Point.MAX_POINT;

/**
 * Created by zasam on 03.02.2017.
 * Class for clusters, should contain list of image
 */
public class Cluster {
    private Point core;
    private final ArrayList<Point> images;
    private double meanSquare;
    private boolean coreChanged;

    public Cluster(Point core,int startCapacity) {
        this.core = core;
        this.images = new ArrayList<>(startCapacity);
        meanSquare = 0;
        coreChanged = true;
    }

    public Cluster() {
        this(MAX_POINT,0);
    }

    public Point getCore() {
        return core;
    }

    boolean add(Point point) {
        boolean result;
        synchronized (this) {
            result = images.add(point);
        }
        if (result) {
            meanSquare += core.calculateDistance(point);
        }
        return result;
    }

    public void clear() {
        images.clear();
        meanSquare = 0;
        coreChanged = true;
    }

    double getDistanceToTheCore(Point point) {
        return core.calculateDistance(point);
    }

    public boolean isCoreChanged() {
        return coreChanged;
    }

    public boolean getNewCores() {
        coreChanged = false;
        for (Point presumableCore : images) {
            double meanSquare = getMeanSquare(presumableCore);
            if (meanSquare < this.meanSquare) {
                coreChanged = true;
                this.meanSquare = meanSquare;
                core = presumableCore;
            }
        }
        return coreChanged;
    }

    public ArrayList<Point> getImages() {
        return images;
    }

    private double getMeanSquare(Point core) {
        double result = 0;
        for (Point point : images) {
            result += core.calculateDistance(point);
        }
        return result;
    }

}
