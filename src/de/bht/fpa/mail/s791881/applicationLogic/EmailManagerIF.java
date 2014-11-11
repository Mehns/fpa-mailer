/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.applicationLogic;

import de.bht.fpa.mail.s791881.model.Folder;

/**
 * This is the interface for classes that manages emails.
 * 
 * @author Christian Mehns
 */
public interface EmailManagerIF {
    
    /**
     * Loads all emails in the directory path of a folder
     * into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    void loadContent(Folder f);     
    
    
}
