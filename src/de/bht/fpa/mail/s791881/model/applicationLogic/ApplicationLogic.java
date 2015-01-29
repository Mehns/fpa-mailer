/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model.applicationLogic;

import de.bht.fpa.mail.s791881.model.applicationLogic.xml.FileManager;
import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.applicationLogic.account.AccountManagerIF;
import de.bht.fpa.mail.s791881.model.applicationLogic.imap.IMapEmailManager;
import de.bht.fpa.mail.s791881.model.applicationLogic.imap.IMapFolderManager;
import de.bht.fpa.mail.s791881.model.applicationLogic.xml.XmlEMailManager;
import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Christian Mehns <s55689@beuth-hochschule.de>
 */
public class ApplicationLogic  implements ApplicationLogicIF{
    
    private final AccountManagerIF accountManager;
    private EmailManagerIF emailManager;
    private FolderManagerIF folderManager;

    
    public ApplicationLogic(File directory) {
        this.accountManager = new AccountManager();                
        this.folderManager = new FileManager(directory);
        this.emailManager = new XmlEMailManager();   
    }
    

    @Override
    public Folder getTopFolder() {
        return folderManager.getTopFolder();
    }

    @Override
    public void loadContent(Folder folder) {
        folderManager.loadContent(folder);
    }
    
    @Override
    public List<Email> search(ObservableList<Email> emailList, String pattern) {
        return emailManager.filterEmails(emailList, pattern);
    }

    @Override
    public void loadEmails(Folder folder) {
        emailManager.loadEmails(folder);
    }

    @Override
    public void changeDirectory(File file) {
        this.folderManager = new FileManager(file);
        this.emailManager = new XmlEMailManager();
    }

    @Override
    public void saveEmails(ObservableList<Email> emailList, File file) {
        emailManager.saveEmails(emailList, file);
    }

    @Override
    public void openAccount(String name) {
        Account acc = getAccount(name);
        System.out.print(name);
        System.out.print(acc.getName());
        this.folderManager = new IMapFolderManager(acc);
        this.emailManager = new IMapEmailManager(acc);     
    }

    @Override
    public List<String> getAllAccounts() {
        List<String> accountNames = new ArrayList<>();
        List<Account> accounts = accountManager.getAllAccounts();
        
        for (Account account : accounts) {
            accountNames.add(account.getName());
        }        
        return accountNames;
    }

    @Override
    public Account getAccount(String name) {
        return accountManager.getAccount(name);
    }

    @Override
    public boolean saveAccount(Account account) {
        return accountManager.saveAccount(account);
    }

    @Override
    public void updateAccount(Account account) {
        accountManager.updateAccount(account);
    }
}
