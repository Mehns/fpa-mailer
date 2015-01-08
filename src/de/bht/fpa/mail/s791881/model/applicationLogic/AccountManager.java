/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model.applicationLogic;

import de.bht.fpa.mail.s791881.model.applicationLogic.account.AccountDAOIF;
import de.bht.fpa.mail.s791881.model.applicationLogic.account.AccountFileDAO;
import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.applicationLogic.account.AccountManagerIF;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Christian Mehns <s55689@beuth-hochschule.de>
 */
public class AccountManager implements AccountManagerIF{
    
    AccountDAOIF dao = new AccountFileDAO();

    @Override
    public Account getAccount(String name) {
        Account account = null;
        ListIterator allAccounts = getAllAccounts().listIterator();
        
        while(allAccounts.hasNext()){
            account = (Account) allAccounts.next();
            if(name.equals(account.getName())){
                return account;
            }
        }
        return null;        
    }

    @Override
    public List<Account> getAllAccounts() {
        return dao.getAllAccounts();
    }

    @Override
    public boolean saveAccount(Account acc) {
        Account savedAccount = dao.saveAccount(acc);
        return savedAccount.equals(acc);
    }

    @Override
    public boolean updateAccount(Account account) {
        return dao.updateAccount(account);
    }
}