/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.controller;

import de.bht.fpa.mail.s791881.model.applicationLogic.ApplicationLogicIF;
import de.bht.fpa.mail.s791881.model.data.Account;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Christian Mehns <s55689@beuth-hochschule.de>
 */
public class CreateAccountViewController implements Initializable{


    @FXML private TextField accountNameField;
    @FXML private TextField accountHostField;
    @FXML private TextField accountUsernameField;
    @FXML private TextField accountPasswordField;
    
    @FXML private Label accountLabelMessage;

    @FXML private Button accountButtonCancel;
    @FXML private Button accountButtonSubmit;
    
    private final ApplicationLogicIF manager;
    
    
    
    public CreateAccountViewController(ApplicationLogicIF manager){
        this.manager = manager;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountLabelMessage.setVisible(false);
        accountButtonCancel.setOnAction(event  -> closeWindow());
        accountButtonSubmit.setOnAction(event  -> submitAccount());
    }
    
    
    private void closeWindow(){
        Stage window = (Stage) accountButtonCancel.getScene().getWindow();
        window.close();
    }
    
    private void submitAccount(){
        if (evaluateForm()) {
            submitForm();
            closeWindow();
        }
    }
    
    private boolean evaluateForm(){
        StringBuilder message = new StringBuilder();
        accountLabelMessage.setVisible(false);
        
        if(accountNameField.getText().length() <= 0)
            message.append("Account name is missing. ");
        if(accountHostField.getText().length() <= 0)
            message.append("Account host is missing. ");
        if(accountUsernameField.getText().length() <= 0)
            message.append("Account username is missing. ");
        if(accountPasswordField.getText().length() <= 0)
            message.append("Account password is missing. ");
        if(message.length() > 0){
            accountLabelMessage.setText(message.toString());
            accountLabelMessage.setVisible(true);
            return false;
        }
        return true;
    }
    
    private void submitForm(){
        String name,host,username,password;
        Account newAccount;
        
        name = accountNameField.getText();
        host = accountHostField.getText();
        username = accountUsernameField.getText();
        password = accountPasswordField.getText();
        
        newAccount = new Account(name, host, username, password);
                
        manager.saveAccount(newAccount);
    }
    
    
}
