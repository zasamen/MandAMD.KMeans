import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kmeans.IPRFactory;
import kmeans.KMeansFactory;
import org.jetbrains.annotations.Contract;
import ui.ChartController;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            FXMLLoader loader = setLoader();
            initStage(loader,primaryStage);
            initController(loader,primaryStage,new KMeansFactory());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    protected void initStage(FXMLLoader loader, Stage stage) throws IOException {
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @Contract(" -> !null")
    protected FXMLLoader setLoader(){
        return new FXMLLoader(ChartController.class.getResource("chart.fxml"));
    }

    protected void initController(FXMLLoader loader, Stage stage, IPRFactory factory){
        ((ChartController)loader.getController()).initialize(stage,factory);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
