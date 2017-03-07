package kmeans;

/**
 * the entity of image and mathematical point
 * Created by zasam on 03.02.2017.
 */
public class Point {
    public double x;
    public double y;

    public static final Point MAX_POINT = new Point();

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double calculateDistance(Point point){
        double dx=point.x-this.x;
        double dy=point.y-this.y;
        return dx*dx+dy*dy;
    }

    public Point() {
        this(Double.MAX_EXPONENT,Double.MAX_EXPONENT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;

    }

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
