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
    * saves xml files in given file
    * @param emailList to save
    * @param file where emails should be saved
    */
    void saveEmails(ObservableList<Email> emailList, File file);
    
    /**
    * filters all emails into list
    * @param emailList to search
    * @param pattern to search for
    * @return filtered list
    */
    ObservableList filterEmails(ObservableList<Email> emailList, String pattern);

}
