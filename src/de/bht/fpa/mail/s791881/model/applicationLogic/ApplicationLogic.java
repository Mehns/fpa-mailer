/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791881.model.applicationLogic;

import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.util.List;

/**
 *
 * @author Christian Mehns <s55689@beuth-hochschule.de>
 */
public class ApplicationLogic  implements ApplicationLogicIF{
    
    private final EmailManagerIF emailManager;
    private FolderManagerIF folderManager;

    
    public ApplicationLogic(File directory) {
        this.folderManager = new FileManager(directory);
        this.emailManager = new XmlEMailManager(folderManager.getTopFolder());
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
    
}