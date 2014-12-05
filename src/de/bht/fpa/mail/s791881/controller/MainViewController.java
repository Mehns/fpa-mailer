
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
    
    @FXML // fx:id="menuBar" 
    private MenuBar menuBar;

    // table    
    @FXML private TableView emailTable;
    @FXML private TableColumn importanceCol;
    @FXML private TableColumn receivedCol;
    @FXML private TableColumn readCol;
    @FXML private TableColumn senderCol;
    @FXML private TableColumn recipientsCol;
    @FXML private TableColumn subjectCol;    
    
    // searchfield
    @FXML private TextField searchField;
    @FXML private Label searchLabel;
    
    // detail view
    @FXML private Text senderText;
    @FXML private Text subjectText;
    @FXML private Text receivedText;
    @FXML private Text receiverText;
    @FXML private TextArea messageTextfield;
    
    
    /* =================== Other Variables ================ */
    
    private final String ROOT_PATH = System.getProperty("user.home");

    // Icons
    private final Image ICON_FOLDER_COLLAPSED;
    private final Image ICON_FOLDER_OPEN;
    
    // lists 
    private final ObservableList<File> historyList; //history of selected paths  
    private final ObservableList<Email> tableData; //Emails in table
    private final FilteredList<Email> filteredData; //filtered Emails of search
    
    private FolderManagerIF folderManager;
    private final EmailManagerIF emailManager;
    
    
    /* =================== Constructor ================ */
    
    public MainViewController(){
        ICON_FOLDER_COLLAPSED = new Image(getClass().getResourceAsStream("folder-horizontal.png"));
        ICON_FOLDER_OPEN = new Image(getClass().getResourceAsStream("folder-horizontal-open.png"));
        
        // initialize lists
        historyList = FXCollections.observableArrayList();    
        tableData = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(tableData, p -> true); //wrap tableData
        
        emailManager = new XmlEMailManager();
    }
    
    
    
    /* =================== Initialisatiion ================ */
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
        configureMenue();   
        configureTable();
        configureSearch();
    }    
    

    /* =================== Configuration Methods ================ */
    
    /**
     * Initiate TreeView and sets handler for selected item
     */
    private void configureTree(){  
        
        setRoot(new File(ROOT_PATH));
        
        
        // add changeListener
        treeView.getSelectionModel().selectedItemProperty().addListener( 
                (ChangeListener) (obsValue, oldState, newState) -> selectFolder(newState) );
    }
    
    /**
     * Iniate Menu and sets handler for menu items
     */
    private void configureMenue(){  
        for(Menu menu : menuBar.getMenus()){
            for(MenuItem item : menu.getItems()){
                // sets handler for menu items
                item.setOnAction((ActionEvent event) -> handleMenu(event));
            }
        }
    }

    /**
     * Iniate EMail-table 
     */
    private void configureTable(){
        
        // set Cell Value Factories
        importanceCol.setCellValueFactory(
            new PropertyValueFactory<Email,String>("importance"));
        receivedCol.setCellValueFactory(
            new PropertyValueFactory<Email,String>("received"));
        readCol.setCellValueFactory(
            new PropertyValueFactory<Email,String>("read"));
        senderCol.setCellValueFactory(
            new PropertyValueFactory<Email,String>("sender"));
        recipientsCol.setCellValueFactory(
            new PropertyValueFactory<Email,String>("receiverTo"));
        subjectCol.setCellValueFactory(
            new PropertyValueFactory<Email,String>("subject"));
     
    }

    private void configureSearch(){
        
        // add listener for search text
        searchField.textProperty().addListener(
                    (observable, oldValue, newValue) -> search(newValue));
        
        
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
    
    
    private void resetSearch(){
        // set search text to default
        searchField.setText("");
        // set search label to default
        searchLabel.setText("(0)"); 
    }
    

    /* =================== Methods for Event-Handling ================ */
    
    private void search(Object newValue){
        
        // cast input
        String searchText = (String) newValue;
        
        // set filter predicate to search text input
        // lambda expression with inner class
        filteredData.setPredicate(email -> {
        
            // If filter text is empty, display all emails
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            // lower case
            String lowerCaseFilter = searchText.toLowerCase();
            
            // checks all email attributes for input text
            // Filter matches subject
            if (email.getSubject().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            } 
            // Filter matches sender
            if (email.getSender().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            } 
            // Filter matches text
            else if (email.getText().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            // Filter matches received Date
            else if (email.getReceived().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            // Filter matches sent Date
            else if (email.getSent().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            // Filter matches receiver
            else if (email.getReceiverListTo().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            
            return false; // input does not match        
        });
        
        // Wrap the FilteredList in a SortedList. 
        SortedList<Email> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(emailTable.comparatorProperty());
        
        // set the filtered, sorted list in table
        emailTable.setItems(filteredData);
        
        // set label to item count
        String itemCount = String.valueOf(filteredData.size());
        searchLabel.setText("("+itemCount+")");       
        
    }
    
    
    /**
     * Handles selection of a treeItem
     * @param newState New Selected item
     */
    private void selectFolder(Object newState){
        
        // get new selected Treeitem and it's folder
        TreeItem<Component> selectedItem = (TreeItem<Component>) newState;      
        Folder folder = (Folder) selectedItem.getValue();  
        
        // load emails into folder
        emailManager.loadEmails(folder);
        
        // add emails into table list
        tableData.clear();
        tableData.addAll(folder.getEmails());        
        emailTable.setItems(tableData);
        
        // set sorting by receiving date
        receivedCol.setSortType(TableColumn.SortType.ASCENDING);
        emailTable.getSortOrder().add(receivedCol);

        resetSearch(); //reset search items            
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
        
        switch(source.getId()){
            case("menuOpen"):
                choosePathView();
                break;
            case("menuHistory"):
                showHistoryView();
                break;
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
        
        if(selectedDirectory == null){
            return;
        }
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
