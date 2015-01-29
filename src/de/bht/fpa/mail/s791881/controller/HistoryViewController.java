
package de.bht.fpa.mail.s791881.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 *
 * @author Christian Mehns
 */
public class HistoryViewController implements Initializable {
    
    @FXML // fx:id="dialogListView"    
    private ListView dialogListView;
    
    @FXML // fx:id="dialogButtonCancel"
    private Button dialogButtonCancel;
    
    @FXML // fx:id="dialogButtonCancel"
    private Button dialogButtonOK;
    
    MainViewController mainViewController;
     
    // no-Entry text
    private final ObservableList noEntry;
    Stage stage; 
    

    /* =================== Constructor ================ */ 
    
    public HistoryViewController(MainViewController mainViewController) {  
        this.mainViewController = mainViewController;
        noEntry = FXCollections.observableArrayList();
    }
    
    

    /* =================== Initialize ================ */ 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // history of choosen directories
        ObservableList historyList = mainViewController.getHistoryList();
        
        // set EventHandler
        dialogButtonOK.setOnAction(event  -> getSelection());
        dialogButtonCancel.setOnAction(event -> closeWindow());
        
        // write no-entry text if there's no path history
        if (historyList.isEmpty()) {            
            noEntry.add("No base directories in history");
            dialogListView.setItems(noEntry);            
            dialogButtonOK.setDisable(true);
        }
        else {
            dialogListView.setItems(historyList);
        }    
    } 
    
    
    /* =================== EventHandler ================ */   
    
    /**
     * Get selesction of choosen path and sets it as new root directory
     */
    private void getSelection(){
        // get selected file and set it as root in the mainView
        File selectedFile = (File) dialogListView.getSelectionModel().getSelectedItem();
        
        mainViewController.getAppManager().changeDirectory(selectedFile);
        mainViewController.configureTree();
        closeWindow();
    }
    
    /**
     * closes the dialog window
     */
    private void closeWindow(){
        // close window
        stage = (Stage) dialogListView.getScene().getWindow();
        stage.close();
    }   
}
