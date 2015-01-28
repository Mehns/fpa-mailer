/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model.applicationLogic.imap;

import de.bht.fpa.mail.s791881.model.applicationLogic.FolderManagerIF;
import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.data.Folder;

/**
 *
 * @author Luke
 */
public class IMapFolderManager implements FolderManagerIF{
    
    Account account;
    
    public IMapFolderManager(Account acc){
        this.account = acc;
    }
    @Override
    public Folder getTopFolder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadContent(Folder f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
