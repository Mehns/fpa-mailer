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
    }
    
    
    
    
    @Override
    public Folder getTopFolder() {
        javax.mail.Folder tempFolder;
        
        try {
            Store store = IMapConnectionHelper.connect(account);
            tempFolder = store.getDefaultFolder();
            IMapFolder topFolder = new IMapFolder(tempFolder);
            topFolder.setName(account.getName());
            store.close();            
            return topFolder;
        } catch (MessagingException ex) {
            System.out.print("\ngetTopFolder is not worling: "+ex);
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

 
    
     @Override
    public void loadContent(Folder f) {
        IMapFolder imapFolder = (IMapFolder) f;
        javax.mail.Folder tempFolder = imapFolder.getRoot();         
        
        try {    
            Store store = IMapConnectionHelper.connect(account);
            
            javax.mail.Folder[] tempFolderChildren = tempFolder.list();
            for (javax.mail.Folder storeFolderChild : tempFolderChildren) {
                Folder child = new IMapFolder(storeFolderChild);
                imapFolder.addComponent(child);
            }

            store.close();            
        } catch (MessagingException e) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, e);            
        }
    }
}
