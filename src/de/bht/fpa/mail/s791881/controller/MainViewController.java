
package de.bht.fpa.mail.s791881.controller;

import de.bht.fpa.mail.s791881.model.applicationLogic.ApplicationLogic;
import de.bht.fpa.mail.s791881.model.applicationLogic.ApplicationLogicIF;
import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.data.Folder;
import de.bht.fpa.mail.s791881.model.data.Component;
import de.bht.fpa.mail.s791881.model.data.Email;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
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
    
    // menu
    @FXML private MenuBar menuBar;
    @FXML private Menu menuOpenAccount;
    @FXML private Menu menuEditAccount;
    @FXML private MenuItem menuCreateAccount;

    // table    
    @FXML private TableView<Email> emailTable;
    @FXML private TableColumn<Email, String> importanceCol;
    @FXML private TableColumn<Email, String> receivedCol;
    @FXML private TableColumn<Email, String> readCol;
    @FXML private TableColumn<Email, String> senderCol;
    @FXML private TableColumn<Email, String> recipientsCol;
    @FXML private TableColumn<Email, String> subjectCol;    
    
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
    private final ObservableList<File> historyList;    ; //history of selected paths  
    private static final ObservableList<Email> tableData = FXCollections.observableArrayList(); //Emails in table
    
    // fassade for application logic
    private final ApplicationLogicIF MANAGER;
    
    
    /* =================== Constructor ================ */
    
    public MainViewController(){
        ICON_FOLDER_COLLAPSED = new Image(getClass().getResourceAsStream("folder-horizontal.png"));
        ICON_FOLDER_OPEN = new Image(getClass().getResourceAsStream("folder-horizontal-open.png"));
        historyList = FXCollections.observableArrayList(); 
        MANAGER = new ApplicationLogic(new File(ROOT_PATH));    
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
    public void configureTree(){  
        Folder topFolder = MANAGER.getTopFolder();
        if(topFolder == null){
            return;
        }
        MANAGER.loadContent(topFolder);        
        
        TreeItem<Component> rootTreeItem = new TreeItem<> (topFolder, new ImageView(ICON_FOLDER_COLLAPSED));   
        showFolders(rootTreeItem); 
        if (!rootTreeItem.getChildren().isEmpty()) {
            rootTreeItem.setExpanded(true);
        }
        
        // set handler for expand and collapse a treeItem
        rootTreeItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> expandItem(e));
        rootTreeItem.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeModificationEvent<Component> e) -> collapseItem(e)); 
        
        // add changeListener
        treeView.getSelectionModel().selectedItemProperty().addListener( 
                (oldValue, oldState, newState) -> selectFolder(newState) );
        
        treeView.setRoot(rootTreeItem); 
        tableData.clear();
    }
    
    /**
     * Iniate Menu and sets handler for menu items
     */
    private void configureMenue(){  
        
        loadAccountsToMenu();
        
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
        importanceCol.setCellValueFactory(new PropertyValueFactory<>("importance"));
        receivedCol.setCellValueFactory(new PropertyValueFactory<>("received"));
        readCol.setCellValueFactory(new PropertyValueFactory<>("read"));
        senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
        recipientsCol.setCellValueFactory(new PropertyValueFactory<>("receiverTo"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        
        /* default column sort by date */
        receivedCol.setComparator((date1,date2) -> compare(date1,date2));
        
        emailTable.getSortOrder().add(receivedCol);
        
        // add ChangeListener => selectEmail()
        emailTable.getSelectionModel().selectedItemProperty().addListener( 
                (ChangeListener) (oldValue, oldState, newState) -> selectEmail(newState) );
    }

    private void configureSearch(){
        
        // add listener for search text => search()
        searchField.textProperty().addListener((observable, oldValue, newValue) -> search(newValue));
    }

    
    /* =================== Methods ================ */

        
    /**
     * Reads all components from the given TreeItem and creates their TreeItems
     * and adds them to the given one
     * @param treeItem Item which content should be shown
     */
    private void showFolders(TreeItem<Component> treeItem){
       
        
        treeItem.getChildren().clear();
        
        List<Component> componentContent = treeItem.getValue().getComponents();        
        
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
    
    private void resetDetail(){
        //set Detail Textfields to selected Mail content
        senderText.setText("");
        subjectText.setText("");
        receivedText.setText("");
        receiverText.setText("");
        messageTextfield.clear();
    }
    

    /* =================== Methods for Event-Handling ================ */
    
    
        private void search(String input){
            List<Email> list = MANAGER.search(tableData,input);
            
            ObservableList fill = FXCollections.observableArrayList();
            for(Email e: list){
            fill.add(e);
            }
            emailTable.setItems(fill);
            searchLabel.setText("(" + fill.size() + ")");
    }
  
    
    /**
     * Handles selection of a treeItem
     * @param newState New Selected item
     */
    private void selectFolder(Object newState){
        
        // get new selected Treeitem and it's folder

        TreeItem<Component> selectedItem = (TreeItem<Component>) newState;  
        
        if(selectedItem == null)
            return;

        if (!(selectedItem.getValue() instanceof Folder)) {
            return;
        }
        
        Folder folder = (Folder) selectedItem.getValue();  
        
        // load emails into folder        
        MANAGER.loadEmails(folder);
        
        // add emails into table list
        tableData.clear();
        tableData.addAll(folder.getEmails());     

        
        emailTable.setItems(tableData);
        
        // set sorting by receiving date
        receivedCol.setSortType(TableColumn.SortType.ASCENDING);
        emailTable.getSortOrder().add(receivedCol);

        resetSearch(); //reset search items   
        resetDetail(); //reset Textfields of Detail
        
        // force TreeView to update
        selectedItem.setGraphic(new ImageView(ICON_FOLDER_COLLAPSED));
    }
    
    
    /**
     * Handles selection of a tableItem
     * @param newState New Selected item
     */
    private void selectEmail(Object newState){  
        if(newState == null)
            return;
        Email email = (Email) newState;
        
        //set Detail Textfields to selected Mail content
        senderText.setText(email.getSender());
        subjectText.setText(email.getSubject());
        receivedText.setText(email.getReceived());
        receiverText.setText(email.getReceiver());        
        messageTextfield.setText(email.getText());         
    }
    
    
    
    /**
     * Handles expansion of a treeItem
     * @param event Fired event from tree Item
     */
    private void expandItem(Event event){
        int focusIndex = treeView.getFocusModel().getFocusedIndex();
        
        // get treeItem that fired event and it's component
        TreeItem<Component> item = (TreeItem<Component>)event.getSource();
        Folder folder = (Folder) item.getValue();
        Component component = item.getValue();
                
        // load content of Component if hasen't done yet
        if(component.getComponents().isEmpty()){
            MANAGER.loadContent(folder);
            showFolders(item); 
        }
        
        // change icon of treeItem
        item.setGraphic(new ImageView(ICON_FOLDER_OPEN));
        treeView.getFocusModel().focusNext();
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
                
            case("menuCreateAccount"):
                showCreateAccount();
                break;
            case("menuOpen"):
                choosePathView();
                break;
            case("menuHistory"):
                showHistoryView();
                break;
            case("menuSave"):
                saveEmails();
                break;
        }
    }
    
    
    public void loadAccountsToMenu(){
        
        menuOpenAccount.getItems().clear();
        menuEditAccount.getItems().clear();
        
        for (String account : MANAGER.getAllAccounts()) {

            // create MenuItems and set Account as data
            MenuItem accountOpenItem = new MenuItem(account);
            MenuItem accountEditItem = new MenuItem(account);    

            // set eventListener
            accountOpenItem.setOnAction((ActionEvent event) -> openAccount(account));
            accountEditItem.setOnAction((ActionEvent event) -> showEditAccount(account));
            
            // add items to menu
            menuOpenAccount.getItems().add(accountOpenItem);
            menuEditAccount.getItems().add(accountEditItem);
        }
    }
    
    
    private void openAccount(String account){        
        MANAGER.openAccount(account); 
        configureTree();
    }

    
    private void showEditAccount(String account){
        Account editAccount = MANAGER.getAccount(account);
        
        // create new Stage
        Stage createStage = new Stage(StageStyle.UTILITY);
        createStage.setTitle("Update Account");        
        URL location = getClass().getResource("/de/bht/fpa/mail/s791881/view/AccountForm.fxml");
        
        // creates FXML loader to load HistoryView
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(new EditAccountViewController(MANAGER, editAccount));
        
        // load view and set scene in stage
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            createStage.setScene(myScene);
            createStage.show();
        } catch (IOException ex) {
            System.out.println("geht nich");
            ex.printStackTrace();
        } 
    }
    
    private void showCreateAccount(){
        // create new Stage
        Stage createStage = new Stage(StageStyle.UTILITY);
        createStage.setTitle("New Account");        
        URL location = getClass().getResource("/de/bht/fpa/mail/s791881/view/AccountForm.fxml");
        
        // creates FXML loader to load HistoryView
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(new CreateAccountViewController(this));
        
        // load view and set scene in stage
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            createStage.setScene(myScene);
            createStage.show();
        } catch (IOException ex) {
            System.out.println("geht nich");
            ex.printStackTrace();
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
        File currentDirectory = new File(ROOT_PATH);
        chooser.setInitialDirectory(currentDirectory);

        // initialize Dialog
        Stage chooserStage = new Stage(StageStyle.UTILITY);
        File selectedDirectory = chooser.showDialog(chooserStage);
        
        if(selectedDirectory == null){
            return;
        }
        // sets selected directory as root and adds it to history list
        MANAGER.changeDirectory(selectedDirectory);
        configureTree();
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
    /**
     * Opens dialog choosing a directory to save the actual emails in that directory
     */
    private void saveEmails(){
        // create Directory chooser
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Save E-Mails");
        
        // set current directory as inital directory of chooser
        File currentDirectory = new File(ROOT_PATH);
        chooser.setInitialDirectory(currentDirectory);
        
        // initialize Dialog
        Stage chooserStage = new Stage(StageStyle.UTILITY);
        File selectedDirectory = chooser.showDialog(chooserStage);
        
        if(selectedDirectory != null){
            MANAGER.saveEmails(tableData, selectedDirectory);     
        }
    }
    
    /**
     * Getter for Fassade
     */
    public ApplicationLogicIF getAppManager(){
        return MANAGER;
    }
    
    // converts strings from received column to dates, to be sorted by date
    private int compare(String s1, String s2) {
        DateFormat FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.GERMANY);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = FORMAT.parse(s1);
            date2 = FORMAT.parse(s2);
        } catch (ParseException ex2) {
        }
        return date1.compareTo(date2);
    }
    
}
