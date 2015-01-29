
package de.bht.fpa.mail.s791881.model.applicationLogic.xml;

import de.bht.fpa.mail.s791881.model.applicationLogic.EmailManagerIF;
import de.bht.fpa.mail.s791881.model.data.Email;
import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
    
    public ObservableList<Email> emailList;
    private ObservableList<Email> emailListFiltered;
    
    
    private final FilenameFilter xmlFilter = new XMLFilter();
    private final File SCHEMA_LOCATION = new File("src/de/bht/fpa/mail/s791881/model/data/email.xsd");

    
    
    public XmlEMailManager(Folder folder) {
        this.emailList = FXCollections.observableArrayList();
        this.emailListFiltered = FXCollections.observableArrayList();
    }
    
    
    
    

    @Override
    public void loadEmails(Folder f) {   
        /* if folder has no Email-objects yet, 
            then check if folder contains any files, read all the xml-files */     
        if (f.getEmails().isEmpty()) {
            File root = new File(f.getPath());        
            File[] content = root.listFiles(xmlFilter);
            
            /* if folder contains xml files, check if xml format is valid 
                and convert them to Email objects */
            if(content != null){
                for(File item: content){
                    
                    if (isValid(item)) {
                        f.addEmail(readMail(item));             
                    }                 
                }
            }
        }    
    }
    
    @Override
    public void saveEmails(File destination){

        try {
            JAXBContext jc = JAXBContext.newInstance(Email.class);
            Marshaller marshaller = jc.createMarshaller();   
            
            System.out.println("Saving emails to " + destination);
            
            int count = 0;
            for(Email email: emailList){
                count++;
                File xmlEmail = new File(destination.getAbsolutePath() + "/Email" + count + ".xml");
                
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(email, xmlEmail);
            }
            System.out.println("Number of saved Emails: " + count);
        } catch (JAXBException ex) {
            System.out.println("SAVE ERROR");
            return;
        }
            
    }

    @Override
    public void updateEmailList(Folder folder) {
        emailList.clear();
        emailList.addAll(folder.getEmails());
    }

    
    
    @Override
    public void updateEmailListFiltered(String pattern) {
        emailListFiltered.clear();
        for(Email email: emailList){
            if(matchesPattern(email, pattern))
                emailListFiltered.add(email);
        }
    }

    @Override
    public ObservableList<Email> getEmailList() {
        return emailList;
    }

    
    @Override
    public ObservableList<Email> getEmailListFiltered() {
        return emailListFiltered;
    }
    
    private boolean matchesPattern(Email email, String pattern){
        
            // If filter text is empty, display all emails
            if (pattern == null || pattern.isEmpty()) {
                return true;
            }
            
            // lower case
            String lowerCaseFilter = pattern.toLowerCase();
            
            // checks all email attributes for input text
            // Filter matches subject
            if (email.getSubject().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            } 
            // Filter matches sender
            if (email.getSender().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            } 
            // Filter matches text
            else if (email.getText().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            // Filter matches received Date
            else if (email.getReceived().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            // Filter matches sent Date
            else if (email.getSent().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            // Filter matches receiver
            else if (email.getReceiverListTo().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; 
            }
            
            return false; // input does not match 
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