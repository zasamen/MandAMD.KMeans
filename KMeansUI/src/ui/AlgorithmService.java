package ui;

import kmeans.Cluster;
import kmeans.IterativePatternRecognition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * implements Service for special task
 * Created by zasam on 05.02.2017.
 */
class AlgorithmService extends Service<List<Cluster>> {

    private IterativePatternRecognition iterativePatternRecognition;

    private boolean neverStarted = true;

    @Override
    protected Task<List<Cluster>> createTask() {
        return new Task<List<Cluster>>() {
            @Override
            protected List<Cluster> call() throws Exception {
                neverStarted = false;
                return iterativePatternRecognition.call();
            }
        };
    }

    synchronized void restart(IterativePatternRecognition ipr) {
        try {
            if (iterativePatternRecognition != null)
                iterativePatternRecognition.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        iterativePatternRecognition = ipr;
        if (neverStarted && !ipr.isHaveNextIteration()) start();
        else restart();
    }

    @Override
    public boolean cancel() {
        try {
            iterativePatternRecognition.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.cancel();
    }

    boolean isNextIterationExist() {
        return iterativePatternRecognition.isHaveNextIteration();
    }

    int getIterationIndex() {
        return iterativePatternRecognition != null
                ? iterativePatternRecognition.getIterationIndex()
                : -1;
    }
}
