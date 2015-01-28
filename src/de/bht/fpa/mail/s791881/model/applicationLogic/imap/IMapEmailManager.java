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
import javafx.collections.ObservableList;

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
