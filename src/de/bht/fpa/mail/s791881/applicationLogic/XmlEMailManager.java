
package de.bht.fpa.mail.s791881.applicationLogic;

import de.bht.fpa.mail.s791881.model.Email;
import de.bht.fpa.mail.s791881.model.Folder;
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
 *
 * @author Christian Mehns
 */
public class XmlEMailManager implements EmailManagerIF{
    
    private final FilenameFilter xmlFilter = new XMLFilter();
    private final File SCHEMA_LOCATION = new File("K:\\MedienInformatik\\FPA\\Workspace\\FPA Mailer\\src\\de\\bht\\fpa\\mail\\s791881\\model\\email.xsd");

    @Override
    public void loadContent(Folder f) {   
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
    
    public Email readMail(File file) {
        return JAXB.unmarshal(file, Email.class);
    }
    
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
