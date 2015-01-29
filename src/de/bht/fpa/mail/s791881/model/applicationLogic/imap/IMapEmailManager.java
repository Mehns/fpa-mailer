/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model.applicationLogic.imap;

import de.bht.fpa.mail.s791881.model.applicationLogic.EmailManagerIF;
import static de.bht.fpa.mail.s791881.model.applicationLogic.imap.IMapEmailConverter.convertMessage;
import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Luke
 */
public class IMapEmailManager implements EmailManagerIF{
    
    Account account;
    public ObservableList<Email> emailList;
    private ObservableList<Email> emailListFiltered;
    
    public IMapEmailManager(Account acc){
        this.account = acc;
        this.emailList = FXCollections.observableArrayList();
        

    }
    
        
    @Override
    public void loadEmails(Folder f) {
        
        //in case content is already loaded
        if(!f.getEmails().isEmpty())
            return;
        
        IMapFolder imapFolder = (IMapFolder) f;
        javax.mail.Folder tempFolder = imapFolder.getRoot();
        ArrayList<Email> emails = new ArrayList();
        
        try {  
            Store store = IMapConnectionHelper.connect(account);
            
            tempFolder.open(javax.mail.Folder.READ_ONLY);
            Message[] messages  = tempFolder.getMessages();
            
            System.out.println("Load Mails\n");

            for(Message message : messages){
                Email email = convertMessage(message);
                System.out.println("Mail: "+email.getSubject());
                f.addEmail(email);
                
            }          
            tempFolder.close(false);
            store.close();
            
        } catch (MessagingException e) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, e);            
        }
    }

    @Override
    public void saveEmails(ObservableList<Email> emailList, File destination) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Email.class);
            Marshaller marshaller = jc.createMarshaller();   
            
            System.out.println("Saving emails to " + destination);
            
            int count = 0;
            for(Email email: emailList){
                count++;
                File xmlEmail = new File(destination.getAbsolutePath() + "/Email" + count + ".xml");
                
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(email, xmlEmail);
            }
            System.out.println("Number of saved Emails: " + count);
        } catch (JAXBException ex) {
            System.out.println("SAVE ERROR");
        }
    }
    
    
    @Override
    public ObservableList filterEmails(ObservableList<Email> emailList, String pattern) {
        ObservableList filtered = FXCollections.observableArrayList();
        for(Email email: emailList){
            if(matchesPattern(email,pattern)){
                filtered.add(email);
            }
        }
        return filtered;
    }
    
    private boolean matchesPattern(Email email, String pattern){      
            // If filter text is empty, display all emails
            if (pattern == null || pattern.isEmpty()) {
                return true;
            }
            
            // lower case
            String lowerCaseFilter = pattern.toLowerCase();
            
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
    }
}
