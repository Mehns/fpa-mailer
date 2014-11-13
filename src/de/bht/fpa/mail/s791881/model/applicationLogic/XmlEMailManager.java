
package de.bht.fpa.mail.s791881.model.applicationLogic;

import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * This is the class that manages Emails in XML format
 * 
 * @author Christian Mehns
 */
public class XmlEMailManager implements EmailManagerIF{
    
    private final FilenameFilter xmlFilter = new XMLFilter();
    private final File SCHEMA_LOCATION = new File("src/de/bht/fpa/mail/s791881/model/data/email.xsd");

    @Override
    public void loadEmails(Folder f) {   
        /* if folder has no Email-objects yet, 
            then check if folder contains any files, read all the xml-files */     
        if (f.getEmails().isEmpty()) {
            File root = new File(f.getPath());        
            File[] content = root.listFiles(xmlFilter);
            
            /* if folder contains xml files, check if xml format is valid 
                and convert them to Email objects */
            if(content.length > 0){
                for(File item: content){
                    
                    if (isValid(item)) {
                        f.addEmail(readMail(item));             
                    }                 
                }
            }
        }    
    }
    
    /**
     * Reads an email as File and converts it to an Email object
     * 
     * @param file represents the email
     * @return Email object
     */
    private Email readMail(File file) {
        return JAXB.unmarshal(file, Email.class);
    }
    
    /**
     * Method checks if the xml file is in the right format to convert into 
     * an Email object
     * @param file xml file that should be checked
     * @return boolean wheter file is valid or not
     */
    private boolean isValid(File file) {
        
        // Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        // Check the document
        
        try {
            // schema is loaded from a java.io.File        
            Schema schema = factory.newSchema(SCHEMA_LOCATION);

            // Get a validator from the schema
            Validator validator = schema.newValidator();

            // Parse the document you want to check.
            Source source = new StreamSource(file);

            validator.validate(source);
            return true;
        }
        
        catch (IOException ex){
            System.out.println("File does not exist");
            return false;
        }
        
        catch (SAXException ex) {
            return false;
        } 
    } 
}
