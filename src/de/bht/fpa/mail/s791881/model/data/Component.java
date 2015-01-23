package de.bht.fpa.mail.s791881.model.data;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/*
 * This is the component part of a composite pattern.
 * 
 * @author Simone Strippgen
 */

@Entity
@Inheritance
public abstract class Component implements Serializable{
    
    @Id
    @GeneratedValue
    private long id;
    
    // absolute directory path to this component
    private String path;
    // name of the component (without path)
    private String name;
    
    
    public Component(){
    }

    public Component(File path) {
        this.path = path.getAbsolutePath();
        this.name = path.getName();
    }

    public void addComponent(Component comp) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Component> getComponents() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // @return is the component expandable
    public abstract boolean isExpandable();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String p) {
        path = p;
    }

    public String getPath() {
        return path;
    }
    
    public Long getId(){
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    public String toString() {
        return name;
    }
}
