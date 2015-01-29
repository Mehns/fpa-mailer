/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model.applicationLogic.imap;

import de.bht.fpa.mail.s791881.model.applicationLogic.FolderManagerIF;
import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Luke
 */
public class IMapFolderManager implements FolderManagerIF{
    
    Account account;
    Folder topFolder;
  
    
    public IMapFolderManager(Account acc){
        this.account = acc;
        this.topFolder = new Folder();
        loadFolder();
    }
    
    @Override
    public Folder getTopFolder() {
        return topFolder;
    }

    @Override
    public void loadContent(Folder f) {
        javax.mail.Folder tempFolder;         
        
        try {    
            Store store = IMapConnectionHelper.connect(account);
            tempFolder = store.getFolder(f.getName());
            
            tempFolder.open(javax.mail.Folder.READ_ONLY);
            
            
            // create Folder for every SubFolder
            for(javax.mail.Folder subFolder : tempFolder.list()){     
                Folder child = new Folder();
                child.setName(subFolder.getName());
                child.setExpandable(subFolder.list().length > 0);
            }
            tempFolder.close(false);
            store.close();            
        } catch (MessagingException e) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, e);            
        }
    }
    
    
    //loads and sets topFolder and imapFolder
    private void loadFolder(){        
        javax.mail.Folder tempFolder;         
        try {    
            Store store = IMapConnectionHelper.connect(account);
            tempFolder = store.getFolder("INBOX");
            
            topFolder.setName(tempFolder.getName());
            store.close();
            
        } catch (MessagingException e) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, e);            
        }
    }
}
