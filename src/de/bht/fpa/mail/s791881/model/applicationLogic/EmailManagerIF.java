package de.bht.fpa.mail.s791881.model.applicationLogic;

import de.bht.fpa.mail.s791881.model.data.Folder;

/**
 * This is the interface for classes that manages emails.
 * 
 * @author Christian Mehns
 */
public interface EmailManagerIF {
    
    /**
     * Loads all emails in the directory path of a folder
     * into the folder.
     * @param f the folder into which the Emails of the corresponding 
     *          directory should be loaded
     */
    void loadEmails(Folder f);     
    
    
}
