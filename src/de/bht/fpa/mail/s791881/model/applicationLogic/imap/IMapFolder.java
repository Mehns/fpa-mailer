
package de.bht.fpa.mail.s791881.model.applicationLogic.imap;

import de.bht.fpa.mail.s791881.model.data.Folder;
import javax.mail.MessagingException;

/**
 *
 * @author Christian Mehns <s55689@beuth-hochschule.de>
 */
public class IMapFolder extends Folder{
    
    private javax.mail.Folder root;
    
    public IMapFolder(javax.mail.Folder root) throws MessagingException{
        super();
        this.root = root;
        setName(root.getName());
        if(root.list().length > 0){
            this.setExpandable(true);
        }
    }
    
    public javax.mail.Folder getRoot() {
        return root;
    }
}
