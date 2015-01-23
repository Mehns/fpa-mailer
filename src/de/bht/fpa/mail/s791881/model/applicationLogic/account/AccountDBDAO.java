
package de.bht.fpa.mail.s791881.model.applicationLogic.account;

import de.bht.fpa.mail.s791881.model.data.Account;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Christian Mehns <s55689@beuth-hochschule.de>
 */
public class AccountDBDAO implements AccountDAOIF{
    
    EntityManagerFactory emf;

    public AccountDBDAO() {
        TestDBDataProvider.createAccounts();
        emf = Persistence.createEntityManagerFactory("fpa");
    }    
    

    @Override
    public List<Account> getAllAccounts() {
        EntityManager em = emf.createEntityManager();
        
        Query query = em.createQuery("SELECT a FROM Account a");        
        List<Account> accounts = query.getResultList();
        em.close();
        
        return accounts;
    }

    @Override
    public Account saveAccount(Account acc) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        File homeDirectory = new File("TestData");
        Folder top = new Folder(new File(homeDirectory, acc.getName()), true);
        acc.setTop(top);
        
        trans.begin();
        em.persist(top);
        em.persist(acc);        
        trans.commit();
        em.close();
        
        return acc;
    }

    @Override
    public boolean updateAccount(Account acc) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        trans.begin();
        em.merge(acc);
        trans.commit();
        em.close();
        
        return true;
    }    
}
