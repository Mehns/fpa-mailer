
package de.bht.fpa.mail.s791881.controller;

import de.bht.fpa.mail.s791881.model.applicationLogic.EmailManagerIF;
import de.bht.fpa.mail.s791881.model.applicationLogic.FileManager;
import de.bht.fpa.mail.s791881.model.applicationLogic.FolderManagerIF;
import de.bht.fpa.mail.s791881.model.applicationLogic.XmlEMailManager;
import de.bht.fpa.mail.s791881.model.data.Folder;
import de.bht.fpa.mail.s791881.model.data.Component;
import de.bht.fpa.mail.s791881.model.data.Email;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller for main application
 * @author Christian Mehns
 */
public class MainViewController implements Initializable {
    
    
    /* =================== FXML Variables ================ */
    
    @FXML //  fx:id="treeView"
    private TreeView<Component> treeView; 
    
    @FXML // fx:id="menuAccount"
    private MenuItem menuOpen;
    
    @FXML // fx:id="menuHistory"
    private MenuItem menuHistory;
    
    
    
    /* =================== Other Variables ================ */
    
    private final String ROOT_PATH = "../FPA Mailer/Account";

    // Icons
    private final Image ICON_FOLDER_COLLAPSED;
    private final Image ICON_FOLDER_OPEN;
    
    // list of previously choosen paths
    private final ObservableList<File> historyList = FXCollections.observableArrayList();    
    
    private FolderManagerIF folderManager;
    private final EmailManagerIF emailManager;
    
    
    /* =================== Constructor ================ */
    
    public MainViewController(){
        ICON_FOLDER_COLLAPSED = new Image(getClass().getResourceAsStream("folder-horizontal.png"));
        ICON_FOLDER_OPEN = new Image(getClass().getResourceAsStream("folder-horizontal-open.png"));
        emailManager = new XmlEMailManager();
    }
    
    
    
    /* =================== Initialisatiion ================ */
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
        configureMenue();      
    }    
    

    /* =================== Configuration Methods ================ */
    
    /**
     * Initiate TreeView and sets handler for selected item
     */
    private void configureTree(){  
        
        setRoot(new File(ROOT_PATH));
        
        // add changeListener
        treeView.getSelectionModel().selectedItemProperty().addListener( 
                (ChangeListener) (obsValue, oldState, newState) -> changeSelection(newState) );
    }
    
    /**
     * Iniate Menu and sets handler for menu items
     */
    private void configureMenue(){  
        
        // sets handler for menu items
        menuOpen.setOnAction((ActionEvent event) -> handleMenu(event));
        menuHistory.setOnAction((ActionEvent event) -> handleMenu(event));
    }

    
    
    /* =================== Methods ================ */

    /**
     * Sets the root directory of the explorer and adds event handler
     * @param root
     */
    public void setRoot(File root){     
        folderManager = new FileManager(root);       
        
        TreeItem<Component> rootItem = new TreeItem<> (folderManager.getTopFolder(), new ImageView(ICON_FOLDER_COLLAPSED));   
        
        // set handler for expand and collapse a treeItem
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), event -> expandItem(event));
        rootItem.addEventHandler(TreeItem.branchCollapsedEvent(), event -> collapseItem(event)); 

        treeView.setRoot(rootItem); 
        showFolders(rootItem); 
        rootItem.setExpanded(true);        
    }
        
    /**
     * Reads all components from the given TreeItem and creates their TreeItems
     * and adds them to the given one
     * @param treeItem Item which content should be shown
     */
    private void showFolders(TreeItem<Component> treeItem){
       
        List<Component> componentContent = treeItem.getValue().getComponents();        
        treeItem.getChildren().clear();
        
        for(Component component: componentContent){            
            if (component instanceof Folder) {
                TreeItem<Component> item = new TreeItem<>(component);
                item.setGraphic(new ImageView(ICON_FOLDER_COLLAPSED));

                if(component.isExpandable()){
                    item.getChildren().add(new TreeItem<>(component));
                }            
                treeItem.getChildren().add(item); 
            }    
        }      
    }
        
    /**
     * Get the history of previously selected paths
     * @return lsit of previously selected paths
     */
    public ObservableList<File> getHistoryList(){
        return historyList;
    }
    
    
    

    /* =================== Methods for Event-Handling ================ */
    
    
    
    
    /**
     * Handles selection of a treeItem
     * @param newState New Selected item
     */
    private void changeSelection(Object newState){
        
        // get new selected Treeitem and it's folder
        TreeItem<Component> selectedItem = (TreeItem<Component>) newState;      
        Folder folder = (Folder) selectedItem.getValue();  
         
        // prints Email content
        System.out.println("\nSelected Directory: " + folder.getPath());
        emailManager.loadEmails(folder);
        int numberOfEmails = folder.getEmails().size();
        System.out.println("Number of emails: " + numberOfEmails);
        if (numberOfEmails > 0) {
            List<Email> mailList= folder.getEmails();

            for(Email email: mailList) {
                System.out.println(email);
            }
        }     
    }
    
    
    
    /**
     * Handles expansion of a treeItem
     * @param event Fired event from tree Item
     */
    private void expandItem(Event event){
        
        // get treeItem that fired event and it's component
        TreeItem<Component> item = (TreeItem<Component>)event.getSource();
        Folder folder = (Folder) item.getValue();
        Component component = item.getValue();
                
        // load content of Component if hasen't done yet
        if(component.getComponents().isEmpty()){
            folderManager.loadContent(folder);
            showFolders(item); 
        }
        
        // change icon of treeItem
        item.setGraphic(new ImageView(ICON_FOLDER_OPEN));
    }
    
    
    
    /**
     * Handles collapse of a treeItem
     * @param event Fired event from tree Item
     */
    private void collapseItem(Event event){
        
        // change icon of treeItem
        ((TreeItem)event.getSource()).setGraphic(new ImageView(ICON_FOLDER_COLLAPSED));
    }
    
    
    
    /**
     * Handles all menu items
     * 
     * @param event Fired event from menu
     */
    private void handleMenu(ActionEvent event){
        // get source of event
        MenuItem source = (MenuItem)event.getSource();  
        
        // call method depending on selected menu item
        if (source == menuOpen) {
            choosePathView();        
        }        
        else if (source == menuHistory) {            
            showHistoryView();     
        }  
    }
    
    
    /**
     * Opens dialog for choosing another directory to be the root directory
     */
    private void choosePathView(){
        
        // create Directory chooser
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open Basic Directory");
        
        // set current directory as inital directory of chooser
        File currentDirectory = new File(folderManager.getTopFolder().getPath());
        chooser.setInitialDirectory(currentDirectory);

        // initialize Dialog
        Stage chooserStage = new Stage(StageStyle.UTILITY);
        File selectedDirectory = chooser.showDialog(chooserStage);
        
        // sets selected directory as root and adds it to history list
        setRoot(selectedDirectory);  
        historyList.add(selectedDirectory);        
    }
    
    
    /**
     * Opens dialog that shows history of previously selected Paths
     * and let user choose one
     */
    private void showHistoryView(){
        // create new Stage
        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.setTitle("Select Base Directory");
        
        URL location = getClass().getResource("/de/bht/fpa/mail/s791881/view/HistoryView.fxml");
        
        // creates FXML loader to load HistoryView
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);

        fxmlLoader.setController(new HistoryViewController(this));
        
        // load view and set scene in stage
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            dialogStage.setScene(myScene);
            dialogStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }            
    }
    
}
