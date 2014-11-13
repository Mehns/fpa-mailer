
package de.bht.fpa.mail.s791881.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to start Application
 * 
 * @author Christian Mehns
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/de/bht/fpa/mail/s791881/view/MainView.fxml"));   
        
        Scene scene = new Scene(root); 
        
        stage.setScene(scene);
        stage.setTitle("FPA Mailer");
        stage.show();
 
    }

    /**
     * Start method
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }   
}
