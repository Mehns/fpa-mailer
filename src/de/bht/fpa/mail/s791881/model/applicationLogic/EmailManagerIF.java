package de.bht.fpa.mail.s791881.model.applicationLogic;

import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import javafx.collections.ObservableList;

/**
 * This is the interface for classes that manages emails.
 * 
 * @author Christian Mehns
 */
public interface EmailManagerIF{
    
    /**
     * Loads all emails in the directory path of a folder
     * into the folder.
     * @param f the folder into which the Emails of the corresponding 
     *          directory should be loaded
     */
    void loadEmails(Folder f);     
    /**
     * Saves actual emails in the directory path of a folder
     * into the folder.
     * @param f the folder into which the Emails of the corresponding 
     *          directory should be saved
     */
    void saveEmails(File f);
    
    public void updateEmailList(Folder folder);
    
    public void updateEmailListFiltered(String pattern);
    
    public ObservableList<Email> getEmailList();
    
    public ObservableList<Email> getEmailListFiltered();
}
