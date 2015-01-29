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
import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian Mehns <s55689@beuth-hochschule.de>
 */
public class ApplicationLogic  implements ApplicationLogicIF{
    
    private final AccountManagerIF accountManager;
    private final EmailManagerIF emailManager;
    private FolderManagerIF folderManager;

    
    public ApplicationLogic(File directory) {
        Account acc = new Account();
        this.folderManager = new IMapFolderManager(acc);
        this.emailManager = new IMapEmailManager(acc);
        this.accountManager = new AccountManager();
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
    public List<Email> search(String pattern) {
        emailManager.updateEmailListFiltered(pattern);
        return emailManager.getEmailListFiltered();
    }

    @Override
    public void loadEmails(Folder folder) {
        emailManager.loadEmails(folder);
        emailManager.updateEmailList(folder);
    }

    @Override
    public void changeDirectory(File file) {
        this.folderManager = new FileManager(file);
    }

    @Override
    public void saveEmails(File file) {
        emailManager.saveEmails(file);
    }

    @Override
    public void openAccount(String name) {
        changeDirectory(new File(getAccount(name).getTop().getPath()));
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
