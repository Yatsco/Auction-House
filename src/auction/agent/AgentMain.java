/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.agent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class AgentMain extends Application {

    @Override
    /**
     * starts of the gui window
     */
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Agent");

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("AgentView.fxml"));

        Pane root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
