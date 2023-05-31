/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.house;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HouseMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("House");

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("HouseView.fxml"));

        Pane root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
