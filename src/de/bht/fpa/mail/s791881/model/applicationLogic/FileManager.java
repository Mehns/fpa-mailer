package de.bht.fpa.mail.s791881.model.applicationLogic;

import de.bht.fpa.mail.s791881.model.data.Folder;
import java.io.File;




/*
 * This class manages a hierarchy of folders and their content which is loaded 
 * from a given directory path.
 * 
 * @author Simone Strippgen
 */
public class FileManager implements FolderManagerIF {

    //top Folder of the managed hierarchy
    private final Folder topFolder;
    
    

    /**
     * Constructs a new FileManager object which manages a folder hierarchy, 
     * where file contains the path to the top directory. 
     * The contents of the  directory file are loaded into the top folder
     * @param file File which points to the top directory
     */
    public FileManager(final File file) {
        // check if file is empty
        boolean isExpandable = (file.listFiles().length > 0);
               
        topFolder = new Folder (file,isExpandable);
        if(isExpandable){
            loadContent(topFolder);
        }
    }
    
    /**
     * Loads all relevant content in the directory path of a folder
     * object into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    @Override
    public void loadContent(Folder f) {
        // hier kommt Ihr Code hin.
        File root = new File(f.getPath());        
        File[] content = root.listFiles();
        
        //creates Files or Folders and adds them to component,
        //checks of directory is expandable
        
        
        for(File item: content){
            if(item.isDirectory()){
                f.addComponent(new Folder(item, containsDirectory(item)));
            }                       
        } 
    }

    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
  
    private boolean containsDirectory(File file){
        File[] content = file.listFiles();
        if (content.length > 0) {
            for(File currentFile: content){
                if (currentFile.isDirectory()) {
                    return true;
                }
            }
        } 
        return false;     
    }  
}
