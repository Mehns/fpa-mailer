/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model.applicationLogic.imap;

import de.bht.fpa.mail.s791881.model.applicationLogic.EmailManagerIF;
import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Luke
 */
public class IMapEmailManager implements EmailManagerIF{
    
    Account account;
    
    public IMapEmailManager(Account acc){
        this.account = acc;
    }
    @Override
    public void loadEmails(Folder f) {
                Store store = IMapConnectionHelper.connect(account);
//        
//        try {            
////            if(imapFolder.getMessageCount()==0){
////                return;
////            };
//            Message[] messages = imapFolder.getMessages();
//            
//            for(int i=0; i < messages.length; i++){
//                
//            }
//            
//            
//            topFolder.setName(imapFolder.getFullName());
//            store.close();
//            
//        } catch (MessagingException e) {
//            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, e);            
//        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveEmails(File f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateEmailList(Folder folder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateEmailListFiltered(String pattern) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList<Email> getEmailList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList<Email> getEmailListFiltered() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
