package ui;

import kmeans.Point;

import static javafx.scene.chart.XYChart.Data;
import static java.lang.Math.random;

/**
 * Random Points Generator
 * Created by zasam on 03.02.2017.
 */
public class PointConverter {


    public Data<Double, Double> getDataFromPoint(Point point) {
        return new Data<>(point.getX(), point.getY());
    }

    public Point[] generatePointList(int count) {
        Point[] points = new Point[count];
        int i = 0;
        while (i < count) {
            points[i] = new Point(random(), random());
            i++;
        }
        return points;
    }

    public Point[] generateCoreList(int count, Point[] points) {
        Point[] cores = new Point[count];
        for (int i = 0, length = points.length; i < count; i++) {
            cores[i] = points[(int) (random() * length)];
        }
        return cores;
    }

}
