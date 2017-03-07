package kmeans;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * interdace for me
 * Created by zasam on 03.02.2017.
 */
public interface IterativePatternRecognition extends Callable<List<Cluster>>,Shutdownable {
    boolean isHaveNextIteration();
    int getIterationIndex();
}
