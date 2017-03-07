package kmeans;

/**
 * Created by zasam on 20.02.2017.
 */
public class KMeansFactory implements IPRFactory {
    @Override
    public IterativePatternRecognition createIPR(Point[]... points) {
        return new KMeans(points[0],points[1]);
    }
}
