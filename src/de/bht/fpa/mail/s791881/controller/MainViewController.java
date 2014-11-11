
package de.bht.fpa.mail.s791881.controller;

import de.bht.fpa.mail.s791881.applicationLogic.EmailManagerIF;
import de.bht.fpa.mail.s791881.applicationLogic.FileManager;
import de.bht.fpa.mail.s791881.applicationLogic.FolderManagerIF;
import de.bht.fpa.mail.s791881.applicationLogic.XmlEMailManager;
import de.bht.fpa.mail.s791881.model.Folder;
import de.bht.fpa.mail.s791881.model.Component;
import de.bht.fpa.mail.s791881.model.Email;
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
 *
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
    
    private final String ROOT_PATH = "K:\\MedienInformatik\\FPA\\Workspace\\FPA Mailer\\Account";

    // Icons
    private final Image ICON_FOLDER_COLLAPSED = new Image(getClass().getResourceAsStream("folder-horizontal.png"));
    private final Image ICON_FOLDER_OPEN = new Image(getClass().getResourceAsStream("folder-horizontal-open.png"));
    
    // list of previously choosen paths
    private final ObservableList<File> historyList = FXCollections.observableArrayList();    
    
    private FolderManagerIF folderManager;
    private final EmailManagerIF emailManager;
    
    
    /* =================== Constructor ================ */
    
    public MainViewController(){
        emailManager = new XmlEMailManager();
    }
    
    
    
    /* =================== Initialisatiion ================ */
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
        configureMenue();      
    }    
    

    /* =================== Configuration Methods ================ */
    
    
    private void configureTree(){  
        
        setRoot(new File(ROOT_PATH));
        
        // add changeListener
        treeView.getSelectionModel().selectedItemProperty().addListener( 
                (ChangeListener) (obsValue, oldState, newState) -> changeSelection(newState) );
    }
    
    private void configureMenue(){    
        menuOpen.setOnAction((ActionEvent event) -> handleMenu(event));
        menuHistory.setOnAction((ActionEvent event) -> handleMenu(event));
    }

    
    
    /* =================== Methods ================ */
    
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
        
    // reads all components from the given TreeItem and creates their TreeItems
    // and adds them to the given one
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
        
    public ObservableList<File> getHistoryList(){
        return historyList;
    }
    
    
    

    /* =================== Methods for Event-Handling ================ */
    
    
    // handles selection of a treeItem
    private void changeSelection(Object newState){
        TreeItem<Component> selectedItem = (TreeItem<Component>) newState;        
        
        Folder folder = (Folder) selectedItem.getValue();  
                        
        System.out.println("\nSelected Directory: " + folder.getPath());
        emailManager.loadContent(folder);
        int numberOfEmails = folder.getEmails().size();
        System.out.println("Number of emails: " + numberOfEmails);
        if (numberOfEmails > 0) {
            List<Email> mailList= folder.getEmails();

            for(Email email: mailList) {
                System.out.println(email);
            }
        }     
    }
    
    // handles expansion of a treeItem
    private void expandItem(Event event){
        TreeItem<Component> item = (TreeItem<Component>)event.getSource();
        Folder folder = (Folder) item.getValue();
        Component component = item.getValue();
        item.setGraphic(new ImageView(ICON_FOLDER_OPEN));
        if(component.getComponents().isEmpty()){
            folderManager.loadContent(folder);
            showFolders(item); 
        }
    }
    
    // handles collapse of a treeItem
    private void collapseItem(Event event){
        ((TreeItem)event.getSource()).setGraphic(new ImageView(ICON_FOLDER_COLLAPSED));
    }
    
    
    
    // handles all menu items
    private void handleMenu(ActionEvent event){
        MenuItem source = (MenuItem)event.getSource();        
        if (source == menuOpen) {
            choosePathView();        
        }        
        else if (source == menuHistory) {            
            showHistoryView();     
        }  
    }
    
    
    // opens dialog for choosing another directory as root
    private void choosePathView(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open Basic Directory");
        File currentDirectory = new File(folderManager.getTopFolder().getPath());
        chooser.setInitialDirectory(currentDirectory);

        Stage chooserStage = new Stage(StageStyle.UTILITY);
        File selectedDirectory = chooser.showDialog(chooserStage);

        setRoot(selectedDirectory);  
        historyList.add(selectedDirectory);        
    }
    
    
    // open dialog that shows history of previously selected Paths
    private void showHistoryView(){
        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.setTitle("Select Base Directory");
        URL location = getClass().getResource("/de/bht/fpa/mail/s791881/view/HistoryView.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);

        fxmlLoader.setController(new HistoryViewController(this));

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
