package de.bht.fpa.mail.s791881.model.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 
 * @author Simone Strippgen
 */

@Entity
public class Folder extends Component implements Serializable{
    
    @Id
    @GeneratedValue
    private Long id;

    private final boolean expandable;
    
    @Transient
    private final transient List<Component> content;
    
    @Transient
    private transient List<Email> emails;
    
    public Folder(){
        this.expandable = false;
        this.content = new ArrayList<>();
    }

    public Folder(File path, boolean expandable) {
        super(path);
        this.expandable = expandable;
        content = new ArrayList<>();
        emails = new ArrayList<>();
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent(Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void addEmail(Email message) {
        emails.add(message);
    }
    
    public Long getId(){
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }
    
    @Override
    public String toString() {
        String text = this.getName();
        if(!this.emails.isEmpty())
            text += " (" +this.emails.size()+ ")";
        return text;
    }
 }